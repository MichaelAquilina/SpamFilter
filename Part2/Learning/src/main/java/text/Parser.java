package text;

import classification.Email;
import classification.EmailClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Parse emails, return plain texts and metadata, strip html.
 *
 * Missing features:
 *
 * Strip multipart declarations like
 *
 * This is a multi-part message in MIME format.
 *
 * ------=_NextPart_000_0358_01C247F9.FBE93A30
 *  Content-Type: text/plain;
 *          charset="iso-8859-1"
 *          Content-Transfer-Encoding: 7bit
 *
 */
public class Parser {
    private final static Pattern emptyLinePattern = Pattern.compile("(?m)^\\s*$");
    private final static Pattern msgIdPattern = Pattern.compile("(?m)^Message-Id:\\s+");
    private final static Pattern subjectPattern = Pattern.compile("(?m)^Subject:\\s+");

    private final static Pattern spamFilePattern = Pattern.compile("spam\\d+\\.txt");
    private final static Pattern hamFilePattern = Pattern.compile("ham\\d+\\.txt");

    private final static Pattern htmlPattern = Pattern.compile("(?i)\\<html\\>");

    private boolean separateMetadata = true;

    public void setSeparateMetadata(boolean separateMetadata) {
        this.separateMetadata = separateMetadata;
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

        EmailClass emailClass = EmailClass.Unknown;
        if (spamFilePattern.matcher(file.getName()).find()) {
            emailClass = EmailClass.Spam;
        } else if (hamFilePattern.matcher(file.getName()).find()) {
            emailClass = EmailClass.Ham;
        }

        return parseString(data.toString(), emailClass);
    }

    public Email parseString(String data, EmailClass _class) {
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
                    remainder = data.substring(splitMatcher.start());
                    String metadataStr = data.substring(0, splitMatcher.start());

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

        // Step 2: Remove HTML
        if (htmlPattern.matcher(remainder).find()) {
            // Oh, this is nasty HTML, get rid of it!
            remainder = Jsoup.parse(remainder).text();
        }

        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(remainder.split("[\\n\\s]+")));

        Email mail = new Email(words);
        mail.setEmailClass(_class);
        return mail;
    }
}
