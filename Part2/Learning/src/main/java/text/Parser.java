package text;

import classification.Email;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse emails, return plain texts and metadata, strip html.
 *
 * Missing features:
 *
 * charset="iso-8859-1"
 */
public class Parser {
    // Patterns for metadata handling
    private final static Pattern emptyLinePattern = Pattern.compile("(?m)^\\s*$");
    private final static Pattern msgIdPattern = Pattern.compile("(?m)^Message-Id:\\s+", Pattern.CASE_INSENSITIVE);
    private final static Pattern subjectPattern = Pattern.compile("(?m)^Subject:\\s+", Pattern.CASE_INSENSITIVE);
    private final static Pattern boundaryPattern = Pattern.compile("boundary=\"([^\"]*)\"", Pattern.CASE_INSENSITIVE);
    private final static Pattern contentTypePattern = Pattern.compile("(?m)^Content-Type:\\s*([^\\n;]*)(;.*)?$", Pattern.CASE_INSENSITIVE);
    private final static Pattern contentTransferEncoding = Pattern.compile("(?m)^Content-Transfer-Encoding:(.*)$", Pattern.CASE_INSENSITIVE);
    private final static Pattern charsetPattern = Pattern.compile("charset\\s*=\\s*\"([^\"]+)\"");

    private static final Set<String> passContentEncoding = new HashSet<String>(Arrays.asList(
                new String[] { "7bit", "binary", "8bit" }
                ));
    private static final Set<String> textContentTypes = new HashSet<String>(Arrays.asList(
                new String[] { "text/plain", "text/html", "multipart/alternative" }
                ));
    private static final Set<String> unusedContentTypes = new HashSet<String>(Arrays.asList(
                new String[] { "image/bmp", "application/x-pkcs7-signature", "image/gif", "image/jpeg", "application/pgp-signature", "application/octet-stream", "application/ms-tnef" }
                ));

    // Patterns for content recognition
    private final static Pattern htmlPattern = Pattern.compile("(?i)\\<html\\>");

    private boolean separateMetadata = true;
    private boolean splitMultipart = true;
    private boolean stripHtml = true;

    public void setSeparateMetadata(boolean separateMetadata) {
        this.separateMetadata = separateMetadata;
    }

    public void setSplitMultipart(boolean splitMultipart) {
        this.splitMultipart = splitMultipart;
    }

    public void setStripHtml(boolean stripHtml) {
        this.stripHtml = stripHtml;
    }

    public static String join(Collection<?> col) {
        String delim = " ";
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = col.iterator();
        if (iter.hasNext())
            sb.append(iter.next().toString());
        while (iter.hasNext()) {
            sb.append(delim);
            sb.append(iter.next().toString());
        }
        return sb.toString();
    }

    public Email parseFile(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder data = new StringBuilder();
        String line = null;
        while((line = reader.readLine()) != null) {
            data.append(line);
            data.append('\n');
        }
        reader.close();

        return new Email(file, parseString(data.toString()));
    }

    public ArrayList<String> parseString(String data) {
        String metadataStr = "";
        String remainder = "";
        // Step 1: Remove metadata
        if (separateMetadata) {
            // Do fancy stuff
            // Step 1.1: Check if we have metadata in this E-Mail, at least one of
            // Subject or Message-Id will be defined if there is
            Matcher subjectMatcher = subjectPattern.matcher(data);
            Matcher msgIdMatcher = msgIdPattern.matcher(data);
            Matcher contentTypeMatcher = contentTypePattern.matcher(data);

            // Step 1.2: If there is metadata, split the string in half
            if (subjectMatcher.find() || msgIdMatcher.find() || contentTypeMatcher.find()) {
                // Parse Metadata
                Matcher splitMatcher = emptyLinePattern.matcher(data);
                if (splitMatcher.find()) {
                    remainder = data.substring(splitMatcher.end() + 1);
                    metadataStr = data.substring(0, splitMatcher.start());

                    // TODO@Uwe: Add metadata parsing here
                } else {
                    // We could not find a split, so we possibly do not
                    // have any metadata
                    remainder = data;
                }
            } else {
                remainder = data;
            }
        } else {
            // Just use everything
            remainder = data;
        }

        // Step 2: Split mulitpart
        if (splitMultipart 
                && metadataStr.contains("Content-Type: multipart/")
                && metadataStr.contains("boundary=\"")) {
            // This message is split into multiple parts,
            // extract all their (relevant) information.
            remainder.replace("This is a multi-part message in MIME format.", "");
            Matcher m = boundaryPattern.matcher(metadataStr);
            if (m.find()) {
                String boundary = m.group(1);
                String[] parts = remainder.split(boundary);
                // Clear remainder as we will refill it now with the relevant stuff
                remainder = "";
                // Skip first part, this is the "This is a multi-part message in MIME format." string
                for (int i = 1; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                    Matcher ctm = contentTypePattern.matcher(parts[i]);
                    if (ctm.find()) {
                        String contentType = ctm.group(1);
                        String content = "";
                        String header = "";
                        Matcher splitMatcher = emptyLinePattern.matcher(parts[i]);
                        if (splitMatcher.find()) {
                            header = parts[i].substring(0, splitMatcher.end()).trim();
                            content = parts[i].substring(splitMatcher.end() + 1);
                            // TODO@Uwe: Add metadata parsing here
                        } else {
                            // We could not find a split, so we possibly do not
                            // have any metadata
                            content = parts[i];
                        }

                        if (textContentTypes.contains(contentType)) {
                            Matcher cteMatcher = contentTransferEncoding.matcher(header);
                            if (cteMatcher.find()) {
                                String cte = cteMatcher.group(1).trim();
                                if (!passContentEncoding.contains(cte)) {
                                    Matcher charsetMatcher = charsetPattern.matcher(header);
                                    if (cte.equals("quoted-printable") && charsetMatcher.find()) {
                                        String charset = charsetMatcher.group(1).trim();
                                        try {
                                            content = (new QuotedPrintableCodec(charset)).decode(content);
                                        } catch (org.apache.commons.codec.DecoderException e){
                                            // We could not convert, so just leave it.
                                        }
                                    } else if (cte.equals("quoted-printable")) {
                                        // No charset -> no care.
                                    } else if (cte.equals("base64")) {
                                        content = StringUtils.newStringUtf8(Base64.decodeBase64(StringUtils.getBytesUtf8(content)));
                                    } else {
                                        // Unknown ContentTypeEncoding, do nothing
                                    }
                                }
                            }
                            if (contentType.equals("text/html") && stripHtml) {
                                remainder += Jsoup.parse(content).text();
                            } else if (contentType.equals("multipart/alternative")) {
                                // Recursion \o/
                                remainder += join(parseString(parts[i]));
                            } else {
                                remainder += content + " ";
                            }
                        } else if (unusedContentTypes.contains(contentType)) {
                            // Just dismiss this content
                            // TODO@Uwe: Save this event in the Email class
                        } else {
                            // TODO@Uwe: This could be HTML which needs separate parsing
                            remainder += content + " ";
                        }
                    } else {
                        remainder += parts[i];
                    }
                }
            }
        }

        // Step 3: Remove HTML
        if (stripHtml && htmlPattern.matcher(remainder).find()) {
            // Oh, this is nasty HTML, get rid of it!
            remainder = Jsoup.parse(remainder).text();
        }

        ArrayList<String> words = new ArrayList<String>();
        words.addAll(Arrays.asList(remainder.split("[\\n\\s]+")));

        return words;
    }
}
