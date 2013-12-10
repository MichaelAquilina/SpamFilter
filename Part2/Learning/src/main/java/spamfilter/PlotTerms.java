package spamfilter;

import classification.Email;
import classification.EmailClassifier;
import invertedindex.InvertedIndex;
import text.Parser;

import java.io.File;
import java.io.IOException;

/**
 * Created by michaela on 10/12/13.
 */
public class PlotTerms {

    public static void usage() {
        System.out.println("PlotTerms <folderpath> <outputpath> <usepreprocessing>");
        System.exit(1);
    }

    public static void main(String args[]) throws IOException {
        if(args.length != 3) {
            usage();
        }

        File folderPath = new File(args[0]);
        if(folderPath.exists()) {

            boolean useTextPreProcessing = Boolean.valueOf(args[2]);

            InvertedIndex invertedIndex = new InvertedIndex();
            Parser parser = new Parser();

            // Part 1: Parsing and pre-processing of text
            for(File trainingFile : folderPath.listFiles()) {

                Email email = parser.parseFile(trainingFile);
                for(String term : email.getWords()) {
                    String alteredTerm = useTextPreProcessing? EmailClassifier.performTextPreProcessing(term) : term;

                    if(alteredTerm != null)
                        invertedIndex.add(alteredTerm, trainingFile.getName());
                }
            }

            invertedIndex.writeTermData(args[1]);
        }
    }
}
