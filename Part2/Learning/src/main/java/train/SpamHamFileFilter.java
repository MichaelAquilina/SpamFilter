package train;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Simple filename filter that only accepts Files that are either ham or spam
 */
public class SpamHamFileFilter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return name.matches("(spam|ham)\\d+.txt");
    }
}


