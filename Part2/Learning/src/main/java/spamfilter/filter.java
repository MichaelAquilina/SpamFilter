package spamfilter;

import classification.EmailClass;
import classification.EmailClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class filter {

    private static final String DEFAULT_MODEL_PATH = "model.json";

    public static void usage() {
        System.out.println("filter <targetFile>");
        System.exit(1);
    }

    public static String classify(File targetFile) throws IOException {
        EmailClassifier emailClassifier = EmailClassifier.load(DEFAULT_MODEL_PATH);

        EmailClass emailClass = emailClassifier.classify(targetFile);
        return emailClass.toString();
    }

    public static void main(String args[]) throws FileNotFoundException {
        if(args.length != 1)
            usage();

        File modelFile = new File(DEFAULT_MODEL_PATH);
        File targetFile = new File(args[0]);

        if(modelFile.exists()) {

            try {
                System.out.println(classify(targetFile));
            } catch(IOException e) {
                System.err.format("Unable to classify file \"%s\"\n", targetFile.getName());
                e.printStackTrace();
            }
        }
        else {
            System.err.format("No Default model (%s) found. Have you performed training yet?\n", DEFAULT_MODEL_PATH);
            System.exit(1);
        }
    }
}
