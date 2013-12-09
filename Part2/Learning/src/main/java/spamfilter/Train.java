package spamfilter;

import classification.*;

import java.io.IOException;

public class Train {
    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile>");
    }

    public static void testClassifier(String trainingPath, EmailClassifier emailClassifier) throws IOException {
        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(10);
        cv.getCombinedConfusion().print();
        System.out.format("StdDev: %f\n", cv.getStdDev());
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

        // Classifier classifier = new J48()
        Classifier classifier = new NaiveBayes();
        EmailClassifier emailClassifier = new EmailClassifier(classifier, weightingMethod, true, true);
        //emailClassifier.getParser().setSeparateMetadata(false);
        //emailClassifier.getParser().setSplitMultipart(false);
        //emailClassifier.getParser().setStripHtml(false);
        testClassifier(trainingPath, emailClassifier);

        EmailClassifier.save(emailClassifier, stateFilePath);
        System.out.format("Saved model to %s\n", stateFilePath);
    }
}
