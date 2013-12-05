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

    public static void main(String[] args) {
        if (args.length != 2) {
            usage();
            return;
        }

        String stateFilePath = args[1];
        
        // Load the list of files and select 0.9 as training data and 0.1 as test data.
        String trainingPath = args[0];
        CrossValidation cv = new CrossValidation(trainingPath);
       
        EmailClassifier emailClassifier = new EmailClassifier(new NaiveBayes(), true);

        try {
            emailClassifier.train(cv.getTraining());

            System.out.format("Classifier trained with %d emails\n", emailClassifier.getDocumentCount());
            System.out.format("Classifier Dimensions = %d\n", emailClassifier.getTermCount());
        } catch(IOException e) {
            System.err.println("An Error occurred while trying to perform training");
            e.printStackTrace();
            return;
        }

        // TODO@Uwe: Cross-Validation possibly a better approach?
        // TODO@Uwe: Do a better implementation of this.

        // @Uwe: Is this temporary? I.e. will this step be in the final version?
        // If so, might be better to move this code to EmailClassifier
        // Including a method to print confusion matrices
        try {
            ConfusionMatrix cm = new ConfusionMatrix();
            for(File trainingFile : cv.getTest()) {
                EmailClass actualClass = trainingFile.getName().contains("ham")? EmailClass.Ham : EmailClass.Spam;
                EmailClass emailClass = emailClassifier.classify(trainingFile);

                cm.add(actualClass, emailClass);
            }

            cm.print();

            // Print confusion matrix
        } catch(IOException e) {
            System.err.println("An error occurred while calculating the confusion matrix");
            e.printStackTrace();
        }
    }
}
