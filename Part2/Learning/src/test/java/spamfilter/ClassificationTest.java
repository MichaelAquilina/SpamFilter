package spamfilter;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class ClassificationTest {

    @Test
    public void testFilter() throws IOException {

        String trainDataPath = "../../traindata";

        File trainDataDir = new File(trainDataPath);

        int failures = 0;
        int total = trainDataDir.listFiles().length;

        for(File trainDataFile : trainDataDir.listFiles()) {
            String result = filter.classify(trainDataFile);

            String expectedClass = trainDataFile.getName().contains("spam")? "Spam" : "Ham";
            PrintStream targetStream;

            if(!expectedClass.equals(result))
            {
                targetStream = System.err;
                failures ++;
            }
            else
                targetStream = System.out;

            targetStream.format("E= %s  R=%s\n", expectedClass, result);
        }

        System.out.format("Misclassifications: %d\n", failures);
        System.out.format("Accuracy: %f\n", ((double) total - failures)/total);
    }


}
