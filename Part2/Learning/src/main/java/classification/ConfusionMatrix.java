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

        public void print() {
            System.out.println("   |   Spam |    Ham |");
            System.out.println("---|--------|--------|");
            System.out.format( " S | % 6d | % 6d |\n", confusion.get(EmailClass.Spam).get(EmailClass.Spam), confusion.get(EmailClass.Spam).get(EmailClass.Ham));
            System.out.format( " H | % 6d | % 6d |\n", confusion.get(EmailClass.Ham).get(EmailClass.Spam), confusion.get(EmailClass.Ham).get(EmailClass.Ham));
        }
}
