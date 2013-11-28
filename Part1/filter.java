
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class filter {

    // Simple filename filter that only accepts Files that are either ham or spam
    private static class SpamHamFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.matches("(spam|ham)\\d+.txt");
        }
    }

    // Simple filename filter that only accepts Files that are spam
    private static class SpamFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.matches("spam\\d+.txt");
        }
    }

    // Simple filename filter that only accepts Files that are ham
    private static class HamFileFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return name.matches("ham\\d+.txt");
        }
    }

    private static Set<String> vocab = new HashSet<String>();

    private static Map<String, Integer> hamMails = new HashMap<String, Integer>();
    private static Map<String, Integer> spamMails = new HashMap<String, Integer>();

    public static void usage() {
        System.out.println("Usage:");
        System.out.println("\tjava filter <traindir> <testfile>");
        System.out.println();
    }

    /**
     * Loads all training data into memory and preprocess the mails for usage
     * in a multinomial model.
     *
     * Loading is done in a 2-pass approach. In the first round we generate the
     * vocabulary, in the second we store the emails so that they can be used
     * to train the classifier.
     */
    public static void loadTrainingData(String trainDirectory) throws FileNotFoundException, IOException {
        File directory = new File(trainDirectory);
        File[] files = directory.listFiles(new SpamHamFileFilter());

        // Pass 1: Read all mails to build up the vocabulary.
        for (int i = 0; i < files.length; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(files[i]));
            String line = null;
            while((line = reader.readLine()) != null) {
                vocab.addAll(Arrays.asList(line.split(" ")));
            }
            reader.close();
        }

        // Pass 2: Read file into memory.
        File[] spamFiles = directory.listFiles(new SpamFileFilter());
        readMailFiles(spamFiles, spamMails);
        File[] hamFiles = directory.listFiles(new HamFileFilter());
        readMailFiles(hamFiles, hamMails);
    }

    public static void readMailFiles(File[] files, Map<String, Integer> wordMap) throws FileNotFoundException, IOException {
        for (int i = 0; i < files.length; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(files[i]));
            String line = null;
            while((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                for (int j = 0; j < words.length; j++) {
                    if (wordMap.containsKey(words[j])) {
                        wordMap.put(words[j], wordMap.get(words[j]) + 1);
                    } else {
                        wordMap.put(words[j], 1);
                    }
                }
            }
            reader.close();
        }
    }

    // Probability distributions for the classification
    private static HashMap<String, Double> spamProb = new HashMap<String, Double>();
    private static HashMap<String, Double> hamProb = new HashMap<String, Double>();

    public static void trainClassifier() {
        // Foreach class:
        // Count overall number of words
        int spamWords = 0;
        int hamWords = 0;
        for(Map.Entry<String, Integer> entry : hamMails.entrySet()) {
            hamWords += entry.getValue();
        }
        for(Map.Entry<String, Integer> entry : spamMails.entrySet()) {
            spamWords += entry.getValue();
        }
        // Prob = count/#words
        for(Map.Entry<String, Integer> entry : hamMails.entrySet()) {
            hamProb.put(entry.getKey(), entry.getValue() / (double)hamWords);
        }
        for(Map.Entry<String, Integer> entry : spamMails.entrySet()) {
            spamProb.put(entry.getKey(), entry.getValue() / (double)spamWords);
        }
    }

    private static double fac(int i) { if (i == 0) return 1; else return fac(i-1)*i; }

    public static boolean classify(String filename) throws IOException {
        // Step 1: Read the file into memory
        HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = null;
        while((line = reader.readLine()) != null) {
            String[] words = line.split(" ");
            for (int j = 0; j < words.length; j++) {
                if (wordMap.containsKey(words[j])) {
                    wordMap.put(words[j], wordMap.get(words[j]) + 1);
                } else {
                    wordMap.put(words[j], 1);
                }
            }
        }
        reader.close();
        // Step 2: Caculate the probabilities
        double spam = 1.0;
        boolean spamTouched = false;
        double ham = 1.0;
        boolean hamTouched = false;
        for(Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            Double prob = spamProb.get(entry.getKey());
            if (prob != null) {
                spam = spam * Math.pow(spamProb.get(entry.getKey()), entry.getValue()) / fac(entry.getValue());
                spamTouched = true;
            } else {
                spam = spam / fac(entry.getValue());
            }
            prob = hamProb.get(entry.getKey());
            if (prob != null) {
                ham = ham * Math.pow(hamProb.get(entry.getKey()), entry.getValue()) / fac(entry.getValue());
                hamTouched = true;
            } else {
                ham = ham / fac(entry.getValue());
            }
        }
        if (!spamTouched) spam = 0;
        if (!hamTouched) ham = 0;
        System.out.println(spam);
        System.out.println(ham);
        return spam > ham;
    }

    // Yeah, this is dirty. But catching them and writing a nice message will not
    // improve our classifier.
    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 2) {
            usage();
        }

        // TODO: Add a priori

        loadTrainingData(args[0]);
        trainClassifier();
        if (classify(args[1])) {
            System.out.println("spam");
        } else {
            System.out.println("ham");
        }
        // ln(Likelihood(ham)) - ln(likelihood(spam))
    }
}
