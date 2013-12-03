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

public class Parser {
    private final static Pattern emptyLinePattern = Pattern.compile("^\\s+$");
    private final static Pattern msgIdPattern = Pattern.compile("^Message-Id:\\s+");
    private final static Pattern subjectPattern = Pattern.compile("^Subject:\\s+");

    private final static Pattern spamFilePattern = Pattern.compile("spam\\d+\\.txt");
    private final static Pattern hamFilePattern = Pattern.compile("ham\\d+\\.txt");

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
            data.append(' ');
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
        if (separateMetadata) {
            // Do fancy stuff
            // Step 1: Check if we have metadata in this E-Mail, at least one of
            // Subject or Message-Id will be defined if there is
            Matcher subjectMatcher = subjectPattern.matcher(data);
            Matcher msgIdMatcher = msgIdPattern.matcher(data);

            // Step2: If there is metadata, split the string in half
            if (subjectMatcher.find() || msgIdMatcher.find()) {
                // Parse Metadata
                Matcher splitMatcher = emptyLinePattern.matcher(data);
                if (splitMatcher.find()) {
                    remainder = data.substring(splitMatcher.start());
                    String metadataStr = data.substring(0, splitMatcher.start());
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

        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(remainder.split("\\s+")));

        Email mail = new Email(words);
        mail.setEmailClass(_class);
        return mail;
    }
}
