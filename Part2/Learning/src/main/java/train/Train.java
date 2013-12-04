package train;

import classification.Classifier;
import classification.Email;
import classification.EmailClass;
import classification.LabelledVector;
import classification.NaiveBayes;
import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;
import text.Parser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Train {
    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <traindata> <outfile>");
    }
    
    public static InvertedIndex loadStopWordsIndex() {
        InvertedIndex stopwordsIndex = new HashedIndex(); 
        
        InputStream stream = Train.class.getResourceAsStream("stopwords.txt");
        
        BufferedReader stopwords = new BufferedReader(new InputStreamReader(stream));
        
        try {
            String line;
            while((line = stopwords.readLine()) != null)
                stopwordsIndex.add(line, "stopwords.txt");
            
            stopwords.close();
        } catch(IOException e) {
            System.err.println("Error reading from stopwords index");
            e.printStackTrace();
            return null;
        }
        
        return stopwordsIndex;
    }

    public static void main(String[] args) {
       if (args.length != 2) {
           usage();
           return;
       }
       
       // Load Stopwords Index
       InvertedIndex stopwordsIndex = loadStopWordsIndex();

       Parser parser = new Parser();
       InvertedIndex invertedIndex = new HashedIndex();   // We can swap in the future if needs be
       
       String trainDataPath = args[0];
       String outFilePath = args[1];

       File directory = new File(trainDataPath);
       File[] examples = directory.listFiles(new SpamHamFileFilter());

       for(File example : examples) {
           try {
               Email email = parser.parseFile(example);
               
               for(String word : email.getWords()) {
                   if(!stopwordsIndex.containsTerm(word)) {
                       String term = VectorFactory.transform(word);

                       if(term != null)
                           invertedIndex.add(term, example.getName());
                   }
               }
               
               System.out.format("Successfully loaded %d terms for %s with class %s\n", email.getWords().size(), example.getName(), email.getEmailClass().toString());
               
           } catch (FileNotFoundException e) {
               System.err.println(String.format("Could not load \"%s\"", example.getName()));
               e.printStackTrace();
           } catch (IOException e) {
               System.err.println(String.format("IO Error during loading \"%s\"", example.getName()));
               e.printStackTrace();
           } 
       }
        
       System.out.format("Final InvertedIndex term size = %d\n", invertedIndex.termCount());
       
       System.out.println("Trimming Index...");
       
       int min = (int) (invertedIndex.documentCount() * 0.07);
       int max = (int) (invertedIndex.documentCount() * 0.9);
       
       invertedIndex.trimIndex(min, max);
       
       System.out.format("Trimmed Inverted Index term size = %d\n", invertedIndex.termCount());

       VectorFactory vectorise = new VectorFactory(invertedIndex);
       
       ArrayList<LabelledVector> labelledVectors = vectorise.getLabelledVectors();

       // IMPORTANT
       // Use the Classifier class as a common interface for performing training
       Classifier someClassifier = new NaiveBayes();
       someClassifier.train(labelledVectors);
       
       Map<EmailClass, Map<EmailClass, Integer>> confusion = new HashMap<EmailClass, Map<EmailClass, Integer>>();
       confusion.put(EmailClass.Spam, new HashMap<EmailClass, Integer>());
       confusion.get(EmailClass.Spam).put(EmailClass.Spam, 0);
       confusion.get(EmailClass.Spam).put(EmailClass.Ham, 0);
       confusion.put(EmailClass.Ham, new HashMap<EmailClass, Integer>());
       confusion.get(EmailClass.Ham).put(EmailClass.Spam, 0);
       confusion.get(EmailClass.Ham).put(EmailClass.Ham, 0);

       // Hack: Simply clasifiy an instance to see if the classifier works.
       int[] vector = new int[labelledVectors.get(0).getVector().length];
       for (LabelledVector vec : labelledVectors) {
           for (int i = 0; i < vector.length; i++) {
               vector[i] = (int)vec.getVector()[i];
           }
           EmailClass clClass = someClassifier.classify(vector);
           EmailClass actualClass = vec.getEmailClass();
           confusion.get(actualClass).put(clClass, confusion.get(actualClass).get(clClass) + 1);
       }

       // Print confusion matrix
       System.out.println("   |   Spam |    Ham |");
       System.out.println("---|--------|--------|");
       System.out.format( " S | % 6d | % 6d |\n", confusion.get(EmailClass.Spam).get(EmailClass.Spam), confusion.get(EmailClass.Spam).get(EmailClass.Ham));
       System.out.format( " H | % 6d | % 6d |\n", confusion.get(EmailClass.Ham).get(EmailClass.Spam), confusion.get(EmailClass.Ham).get(EmailClass.Ham));
    }
}
