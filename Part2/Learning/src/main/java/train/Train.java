package train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import classification.Email;
import text.Parser;
import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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
       
       int stopwordsCount = 0;
       for(File example : examples) {
           try {
               Email email = parser.parseFile(example);
               
               for(String term : email.getWords()) {
                   if(!stopwordsIndex.containsTerm(term))
                       invertedIndex.add(term, example.getName());
                   else
                       ++stopwordsCount;
               }
               
               System.out.format("Successfully loaded %d terms for %s\n", email.getWords().size(), example.getName());
               
           } catch (FileNotFoundException e) {
               System.err.println(String.format("Could not load \"%s\"", example.getName()));
               e.printStackTrace();
           } catch (IOException e) {
               System.err.println(String.format("IO Error during loading \"%s\"", example.getName()));
               e.printStackTrace();
           } 
       }
        
       System.out.format("Final InvertedIndex term size = %d\n", invertedIndex.size());
       System.out.format("Prevented %d stopword entries from being added\n", stopwordsCount);
    }
}
