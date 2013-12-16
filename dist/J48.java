package weka;

public class J48 extends WekaClassifier {
    public J48(int minInstances) {
        wekaClassifier = new weka.classifiers.trees.J48();
        try {
            wekaClassifier.setOptions(new String[]{"-D", Integer.toString(minInstances)});
        } catch (Exception e) {
            System.err.println("Could not setup J48 parameters");
            e.printStackTrace();
        }
    }
}
