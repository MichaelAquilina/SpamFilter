package classification;

import java.util.Map;
import java.util.HashMap;

public class ConfusionMatrix {
        Map<EmailClass, Map<EmailClass, Integer>> confusion = new HashMap<EmailClass, Map<EmailClass, Integer>>();

        public ConfusionMatrix() {
            confusion.put(EmailClass.Spam, new HashMap<EmailClass, Integer>());
            confusion.get(EmailClass.Spam).put(EmailClass.Spam, 0);
            confusion.get(EmailClass.Spam).put(EmailClass.Ham, 0);
            confusion.put(EmailClass.Ham, new HashMap<EmailClass, Integer>());
            confusion.get(EmailClass.Ham).put(EmailClass.Spam, 0);
            confusion.get(EmailClass.Ham).put(EmailClass.Ham, 0);
        }

        public void add(EmailClass actualClass, EmailClass emailClass) {
                confusion.get(actualClass).put(emailClass, confusion.get(actualClass).get(emailClass) + 1);
        }

        public int getTotal() {
            return getValue(EmailClass.Ham, EmailClass.Ham) + getValue(EmailClass.Spam, EmailClass.Spam) +
                   getValue(EmailClass.Spam, EmailClass.Ham) + getValue(EmailClass.Ham, EmailClass.Spam);
        }

        public int getValue(EmailClass actual, EmailClass predicted) {
            return confusion.get(actual).get(predicted);
        }

        public void print() {
            System.out.println("   |   Spam |    Ham |");
            System.out.println("---|--------|--------|");
            System.out.format( " S | % 6d | % 6d |\n", getValue(EmailClass.Spam, EmailClass.Spam), getValue(EmailClass.Spam, EmailClass.Ham));
            System.out.format( " H | % 6d | % 6d |\n", getValue(EmailClass.Ham, EmailClass.Spam), getValue(EmailClass.Ham, EmailClass.Ham));
            System.out.println();

            // Display Model Accuracy
            int correctlyPredicted = getValue(EmailClass.Spam, EmailClass.Spam) + getValue(EmailClass.Ham, EmailClass.Ham);
            double accuracy = ((double) correctlyPredicted) / (double) getTotal();

            System.out.format("Accuracy = %f\n", accuracy);

            // TODO: Print and calculate Precision and Recall
        }
}
