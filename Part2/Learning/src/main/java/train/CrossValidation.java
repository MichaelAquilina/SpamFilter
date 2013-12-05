package train;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class CrossValidation {
    private List<File> trainingFiles;
    private List<File> testFiles;

    public CrossValidation(String directory) {
        File trainingDirectory = new File(directory);
        ArrayList<File> files = new ArrayList<File>(Arrays.asList(trainingDirectory.listFiles(new SpamHamFileFilter())));
        Collections.shuffle(files);
        trainingFiles = files.subList(0, (int)Math.round(files.size() * 0.9));
        testFiles = files.subList((int)Math.round(files.size() * 0.9), files.size());
    }

    public List<File> getTraining() {
        return trainingFiles;
    }

    public List<File> getTest() {
        return testFiles;
    }
}
