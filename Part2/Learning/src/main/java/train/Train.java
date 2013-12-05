package train;

import classification.NaiveBayes;

import java.io.IOException;

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
        String trainingPath = args[0];

        FeatureWeighting weightingMethod;
        //weightingMethod = new TfidfWeighting();
        weightingMethod = new FrequencyWeighting();

        // Load the list of files and select 0.9 as training data and 0.1 as test data.
        EmailClassifier emailClassifier = new EmailClassifier(new NaiveBayes(), weightingMethod, true);

        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(10);
        cv.getCombinedConfusion().print();
    }
}
