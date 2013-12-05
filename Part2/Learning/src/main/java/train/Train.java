package train;

import classification.ConfusionMatrix;
import classification.EmailClass;
import classification.NaiveBayes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Train {
    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile>");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            usage();
            return;
        }

        String stateFilePath = args[1];

        // Load the list of files and select 0.9 as training data and 0.1 as test data.
        String trainingPath = args[0];
        EmailClassifier emailClassifier = new EmailClassifier(new NaiveBayes(), true);
        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(10);
        cv.getCombinedConfusion().print();

        // try {
        //     emailClassifier.train(cv.getTraining());

        //     System.out.format("Classifier trained with %d emails\n", emailClassifier.getDocumentCount());
        //     System.out.format("Classifier Dimensions = %d\n", emailClassifier.getTermCount());
        // } catch(IOException e) {
        //     System.err.println("An Error occurred while trying to perform training");
        //     e.printStackTrace();
        //     return;
        // }
    }
}
