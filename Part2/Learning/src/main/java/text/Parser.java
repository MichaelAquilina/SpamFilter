package text;

import classification.Email;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
    private final static Pattern msgIdPattern = Pattern.compile("(?m)^Message-Id:\\s+");
    private final static Pattern subjectPattern = Pattern.compile("(?m)^Subject:\\s+");
    private final static Pattern boundaryPattern = Pattern.compile("boundary=\"([^\"]*)\"");
    private final static Pattern contentTypePattern = Pattern.compile("(?m)^Content-Type:\\s*([^;]*);");

    private static final Set<String> textContentTypes = new HashSet<String>(Arrays.asList(
                new String[] { "text/plain" }
                ));
    private static final Set<String> unusedContentTypes = new HashSet<String>(Arrays.asList(
                new String[] { "image/bmp" }
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

            // Step 1.2: If there is metadata, split the string in half
            if (subjectMatcher.find() || msgIdMatcher.find()) {
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
                && remainder.startsWith("This is a multi-part message in MIME format.")
                && metadataStr.contains("Content-Type: multipart/mixed")
                && metadataStr.contains("boundary=\"")) {
            // This message is split into multiple parts,
            // extract all their (relevant) information.
            Matcher m = boundaryPattern.matcher(metadataStr);
            if (m.find()) {
                String boundary = m.group(1);
                String[] parts = remainder.split(boundary);
                // Clear remainder as we will refill it now with the relevant stuff
                remainder = "";
                // Skip first part, this is the "This is a multi-part message in MIME format." string
                // Skip last part too as this is not read by the mail client
                for (int i = 1; i < parts.length - 1; i++) {
                    Matcher ctm = contentTypePattern.matcher(parts[i]);
                    if (ctm.find()) {
                        String contentType = ctm.group(1);
                        String content = "";
                        Matcher splitMatcher = emptyLinePattern.matcher(parts[i]);
                        if (splitMatcher.find()) {
                            content = data.substring(splitMatcher.end() + 1);
                            // metadata = data.substring(0, splitMatcher.start());
                            // TODO@Uwe: Add metadata parsing here
                        } else {
                            // We could not find a split, so we possibly do not
                            // have any metadata
                            content = data;
                        }

                        if (textContentTypes.contains(contentType)) {
                            remainder += content + " ";
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

        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(remainder.split("[\\n\\s]+")));

        return words;
    }
}
