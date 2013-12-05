package train;

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
        Map<EmailClass, Map<EmailClass, Integer>> confusion = new HashMap<EmailClass, Map<EmailClass, Integer>>();
        confusion.put(EmailClass.Spam, new HashMap<EmailClass, Integer>());
        confusion.get(EmailClass.Spam).put(EmailClass.Spam, 0);
        confusion.get(EmailClass.Spam).put(EmailClass.Ham, 0);
        confusion.put(EmailClass.Ham, new HashMap<EmailClass, Integer>());
        confusion.get(EmailClass.Ham).put(EmailClass.Spam, 0);
        confusion.get(EmailClass.Ham).put(EmailClass.Ham, 0);

        // @Uwe: Is this temporary? I.e. will this step be in the final version?
        // If so, might be better to move this code to EmailClassifier
        // Including a method to print confusion matrices
        try {
            for(File trainingFile : cv.getTest()) {
                EmailClass actualClass = trainingFile.getName().contains("ham")? EmailClass.Ham : EmailClass.Spam;
                EmailClass emailClass = emailClassifier.classify(trainingFile);

                confusion.get(actualClass).put(emailClass, confusion.get(actualClass).get(emailClass) + 1);
            }

            // Print confusion matrix
            System.out.println("   |   Spam |    Ham |");
            System.out.println("---|--------|--------|");
            System.out.format( " S | % 6d | % 6d |\n", confusion.get(EmailClass.Spam).get(EmailClass.Spam), confusion.get(EmailClass.Spam).get(EmailClass.Ham));
            System.out.format( " H | % 6d | % 6d |\n", confusion.get(EmailClass.Ham).get(EmailClass.Spam), confusion.get(EmailClass.Ham).get(EmailClass.Ham));
        } catch(IOException e) {
            System.err.println("An error occurred while calculating the confusion matrix");
            e.printStackTrace();
        }
    }
}
