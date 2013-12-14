package spamfilter;

import classification.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import weka.J48;

public class Wilcoxon {
    private static final int NO_FOLDS = 10;

    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -cp spamfilter-learning.jar spamfilter.Wilcoxon <traindata>");
    }
    
    public static void main(String[] args) throws IOException {
        double lowerPercentile = 0.017;
        double upperPercentile = 0.19;
        int minInstances = 2;
        if (args.length != 1) {
            usage();
            return;
        }

        String trainingPath = args[0];

        FeatureWeighting weightingMethod;
        //weightingMethod = new TfidfWeighting();
        weightingMethod = new FrequencyWeighting();

        Classifier classifier = new J48(minInstances);
        EmailClassifier emailClassifier2 = new EmailClassifier(classifier, weightingMethod, true, true);
        classifier = new NaiveBayes();

        EmailClassifier emailClassifier = new EmailClassifier(classifier, weightingMethod, true, true);
        //emailClassifier.getParser().setSeparateMetadata(false);
        //emailClassifier.getParser().setSplitMultipart(false);
        //emailClassifier.getParser().setStripHtml(false);

        System.out.format("Using %s\n", classifier.getClass().getName());
        System.out.format("Using Feature Weighting Scheme %s\n", weightingMethod.getClass().getName());
        System.out.format("Performing cross-validation on %d folds...\n", NO_FOLDS);
        DualCrossValidation cv = new DualCrossValidation(trainingPath, emailClassifier, emailClassifier2);
        cv.fold(NO_FOLDS, lowerPercentile, upperPercentile);
    }
}
