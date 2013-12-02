package train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import classification.Email;
import text.Parser;
import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;

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

       Parser parser = new Parser();
       InvertedIndex invertedIndex = new HashedIndex();   // We can swap in the future if needs be
       
       String trainDataPath = args[0];
       String outFilePath = args[1];

       File directory = new File(trainDataPath);
       File[] examples = directory.listFiles(new SpamHamFileFilter());
       
       for(File example : examples) {
           try {
               Email email = parser.parseFile(example);
               
               for(String term : email.getWords()) {
                   invertedIndex.add(term, example.getName());
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
    }
}
