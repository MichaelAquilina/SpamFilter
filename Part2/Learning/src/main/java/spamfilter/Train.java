package spamfilter;

import classification.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import weka.J48;

public class Train {
    private static final int NO_FOLDS = 10;

    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile> [<lowerPercentile> <upperPercentile>] [<cross-validation>]");
    }

    public static String find(HashMap<String, Integer> map, int index) {
        for(String key : map.keySet())
            if(map.get(key) == index)
                return key;

        return null;
    }

    public static void displayRepresentativeFeatures(EmailClassifier emailClassifier) {
        Classifier classifier = emailClassifier.getClassifier();

        // Returns the index of the best features in the vector in terms of representation of positive and negative
        ArrayList<Integer> orderedPosProbs = classifier.getHighestPositiveFeatures();
        ArrayList<Integer> orderedNegProbs = classifier.getHighestNegativeFeatures();

        // Only run if Classifier has implemented the relevant function
        if(orderedNegProbs != null) {
            HashMap<String, Integer> termIndexMap = emailClassifier.getTermIndexMap();

            System.out.println("\nMost representative POSITIVE features:");
            for(int i=0; i<20 ; ++i)
                System.out.print(find(termIndexMap, orderedPosProbs.get(i)) + ", ");
            System.out.println();

            System.out.println("\nMost representative NEGATIVE features:");
            for(int i=0; i<20 ; ++i)
                System.out.print(find(termIndexMap, orderedNegProbs.get(i)) + ", ");
            System.out.println();
        }
    }

    public static void testClassifier(String trainingPath, EmailClassifier emailClassifier, double lowerPercentile, double upperPercentile) throws IOException {
        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(NO_FOLDS, lowerPercentile, upperPercentile);
        cv.getCombinedConfusion().print();
        System.out.format("StdDev: %f\n", cv.getStdDev());
    }

    public static void main(String[] args) throws IOException {
        double lowerPercentile = EmailClassifier.DEFAULT_LOWER_PERCENTILE;
        double upperPercentile = EmailClassifier.DEFAULT_UPPER_PERCENTILE;
        if (args.length != 2 && args.length != 4) {
            usage();
            return;
        } else if (args.length == 4) {
            lowerPercentile = Double.parseDouble(args[2]);
            upperPercentile = Double.parseDouble(args[3]);
        }

        String stateFilePath = args[1];
        String trainingPath = args[0];

        FeatureWeighting weightingMethod;
        //weightingMethod = new TfidfWeighting();
        weightingMethod = new FrequencyWeighting();

        //Classifier classifier = new J48();
        Classifier classifier = new NaiveBayes();

        EmailClassifier emailClassifier = new EmailClassifier(classifier, weightingMethod, true, true);
        //emailClassifier.getParser().setSeparateMetadata(false);
        //emailClassifier.getParser().setSplitMultipart(false);
        //emailClassifier.getParser().setStripHtml(false);

        System.out.format("Using %s\n", classifier.getClass().getName());
        System.out.format("Performing cross-validation on %d folds...\n", NO_FOLDS);
        testClassifier(trainingPath, emailClassifier, lowerPercentile, upperPercentile);

        System.out.println("Finished Testing, performing final train step...");
        File trainingDir = new File(trainingPath);

        emailClassifier.train(Arrays.asList(trainingDir.listFiles()), lowerPercentile, upperPercentile);

        System.out.format("Feature Dimensions = %d\n", emailClassifier.getTermCount());

        displayRepresentativeFeatures(emailClassifier);

        EmailClassifier.save(emailClassifier, stateFilePath);
        System.out.format("'\nSaved model to %s\n", stateFilePath);
    }
}
