package text;

import classification.Email;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Parser {
    public Email parseFile(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        ArrayList<String> words = new ArrayList<String>();
        while((line = reader.readLine()) != null) {
            words.addAll(Arrays.asList(line.split(" ")));
        }
        reader.close();
        return new Email(words);
    }
}
