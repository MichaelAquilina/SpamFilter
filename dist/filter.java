import classification.EmailClass;
import classification.EmailClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.reflect.Method;

import java.net.URL;
import java.net.URLClassLoader;

public class filter {

    private static final String DEFAULT_MODEL_PATH = "model.json";

    public static void usage() {
        System.out.println("filter <targetFile>");
        System.exit(1);
    }

    public static String classify(File targetFile) throws IOException {
        EmailClassifier emailClassifier = EmailClassifier.load(DEFAULT_MODEL_PATH);

        EmailClass emailClass = emailClassifier.classify(targetFile);
        return emailClass.toString();
    }

    public static void main(String args[]) throws Exception {
        if(args.length != 1)
            usage();

        // Lazy load all compiled Java classes and 3rdparty dependencies
        URL classUrl = new URL("file://" + System.getProperty("user.dir") + "/spamfilter.jar");
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{classUrl});

        File modelFile = new File(DEFAULT_MODEL_PATH);
        File targetFile = new File(args[0]);

        if(modelFile.exists()) {

            try {
                System.out.println(classify(targetFile));
            } catch(IOException e) {
                System.err.format("Unable to classify file \"%s\"\n", targetFile.getName());
                e.printStackTrace();
            }
        }
        else {
            System.err.format("No Default model (%s) found. Have you performed training yet?\n", DEFAULT_MODEL_PATH);
            System.exit(1);
        }
    }
}
