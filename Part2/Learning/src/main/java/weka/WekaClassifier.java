package weka;

import classification.Classifier;
import classification.EmailClass;
import classification.LabelledVector;

import java.util.ArrayList;

public abstract class WekaClassifier extends Classifier {
    protected weka.classifiers.Classifier wekaClassifier;
    protected WekaInstances instances;

    public void train(ArrayList<LabelledVector> examples) {
        // Convert training set to WEKA data model
        instances = new WekaInstances(examples.get(0).getVector().length + 1);
        // The last column is the class
        instances.setClassIndex(instances.numAttributes() - 1);

        // Convert all instances to WekaInstances
        for (LabelledVector example : examples) {
            WekaInstance instance = instances.newInstance();
            for (int i = 0; i < example.getVector().length; i++) {
                instance.setValue(i, example.getVector()[i]);
            }
            instance.setClassValue(example.getEmailClass().toString());
        }

        try {
            wekaClassifier.buildClassifier(instances);
        } catch (Exception e) {
            // Oh, WEKA why do I have to do this?
            e.printStackTrace();
            System.err.println("Weka chrashed. Stopping here.");
            System.exit(0);
        }
    }

    public EmailClass classify(double[] vector) {
        // Convert data to WEKA
        // We need to fork from the original dataset so that tree classifiers work.
        WekaInstance instance = instances.newInstance();
        for (int i = 0; i < vector.length; i++) {
            instance.setValue(i, vector[i]);
        }

        // Classify
        double label = 0;
        try {
            label = wekaClassifier.classifyInstance(instance);
        } catch (Exception e) {
            // Oh, WEKA why do I have to do this?
            e.printStackTrace();
            System.err.println("Weka chrashed. Stopping here.");
            System.exit(0);
        }

        return EmailClass.valueOf(instances.classAttribute().value((int) label));
    }
}
