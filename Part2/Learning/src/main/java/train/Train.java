package train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import classification.Email;
import classification.Parser;

public class Train {
    public static void usage() {
        System.out.println("Usage: ");
        System.out.println("\tjava -jar spamfilter-learning.jar <testdata> <outfile>");
    }

    public static void main(String[] args) {
       if (args.length != 2) {
           usage();
           return;
       }

       Parser parser = new Parser();
       // TODO@Mike: Add your tree initialisation here.

       File directory = new File(args[0]);
       File[] files = directory.listFiles(new SpamHamFileFilter());
       for (int i = 0; i < files.length; i++) {
           try {
               Email email = parser.parseFile(files[i]);
               // TODO@Mike: Add your code here!
           } catch (FileNotFoundException e) {
               System.err.println(String.format("Could not load \"%s\"", files[i].getName()));
               e.printStackTrace();
           } catch (IOException e) {
               System.err.println(String.format("IO Error during loading \"%s\"", files[i].getName()));
               e.printStackTrace();
           }
       }
    }
}
