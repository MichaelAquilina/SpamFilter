package text;

import classification.Email;

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
    public static Pattern emptyLinePattern = Pattern.compile("^\\s+$");
    public static Pattern msgIdPattern = Pattern.compile("^Message-Id:\\s+");
    public static Pattern subjectPattern = Pattern.compile("^Subject:\\s+");

    private boolean separateMetadata = true;

    public void setSeparateMetadata(boolean separateMetadata) {
        this.separateMetadata = separateMetadata;
    }

    public Email parseFile(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder data = new StringBuilder();
        String line = null;
        ArrayList<String> words = new ArrayList<String>();
        while((line = reader.readLine()) != null) {
            data.append(line);
            data.append(' ');
        }
        reader.close();

        String remainder = "";
        String dataString = data.toString();
        if (separateMetadata) {
            // Do fancy stuff
            // Step 1: Check if we have metadata in this E-Mail, at least one of
            // Subject or Message-Id will be defined if there is
            Matcher subjectMatcher = subjectPattern.matcher(dataString);
            Matcher msgIdMatcher = msgIdPattern.matcher(dataString);

            // Step2: If there is metadata, split the string in half
            if (subjectMatcher.find() || msgIdMatcher.find()) {
                // Parse Metadata
                Matcher splitMatcher = emptyLinePattern.matcher(dataString);
                if (splitMatcher.find()) {
                    remainder = dataString.substring(splitMatcher.start());
                    String metadataStr = dataString.substring(0, splitMatcher.start());
                } else {
                    // We could not find a split, so we possibly do not
                    // have any metadata
                    remainder = dataString;
                }
            } else {
                remainder = dataString;
            }
        } else {
            // Just use everything
            remainder = dataString;
        }
        words.addAll(Arrays.asList(remainder.split("\\s+")));

        return new Email(words);
    }
}
