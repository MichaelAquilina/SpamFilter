package train;

import classification.Classifier;
import classification.NaiveBayes;

import java.io.IOException;

import weka.Perceptron;
import weka.J48;

public class Train {
    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile>");
    }

    public static void testClassifier(String trainingPath, EmailClassifier emailClassifier) throws IOException {
        CrossValidation cv = new CrossValidation(trainingPath, emailClassifier);
        cv.fold(10);
        cv.getCombinedConfusion().print();
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

        Classifier classifier = new J48();//NaiveBayes();
        EmailClassifier emailClassifier = new EmailClassifier(classifier, weightingMethod, true);
        testClassifier(trainingPath, emailClassifier);
    }
}
