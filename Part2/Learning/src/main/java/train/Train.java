package train;

import classification.EmailClass;
import classification.NaiveBayes;
import text.Parser;

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

        String trainingPath = args[0];
        String stateFilePath = args[1];
       
        EmailClassifier emailClassifier = new EmailClassifier(new NaiveBayes());

        try {
            emailClassifier.train(trainingPath);

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

        // @Uwe: Is this temporary?
        Parser parser = new Parser();

        File trainingDirectory = new File(trainingPath);
        File[] trainingFiles = trainingDirectory.listFiles(new SpamHamFileFilter());

        for(File trainingFile : trainingFiles) {
            EmailClass actualClass = trainingFile.getName().contains("ham")? EmailClass.Ham : EmailClass.Spam;
            EmailClass emailClass = emailClassifier.classify(trainingFile.getAbsolutePath());

            confusion.get(actualClass).put(emailClass, confusion.get(actualClass).get(emailClass) + 1);
        }

        // Print confusion matrix
        System.out.println("   |   Spam |    Ham |");
        System.out.println("---|--------|--------|");
        System.out.format( " S | % 6d | % 6d |\n", confusion.get(EmailClass.Spam).get(EmailClass.Spam), confusion.get(EmailClass.Spam).get(EmailClass.Ham));
        System.out.format( " H | % 6d | % 6d |\n", confusion.get(EmailClass.Ham).get(EmailClass.Spam), confusion.get(EmailClass.Ham).get(EmailClass.Ham));
    }
}
