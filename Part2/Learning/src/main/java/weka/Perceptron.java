package weka;

import weka.classifiers.functions.MultilayerPerceptron;

public class Perceptron extends WekaClassifier {
    public Perceptron() {
        wekaClassifier = new MultilayerPerceptron();
    }
}
