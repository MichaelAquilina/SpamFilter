package classification;

import java.util.ArrayList;

public abstract class Classifier {

    public void train(ArrayList<LabelledVector> examples) {
        throw new UnsupportedOperationException();
    }
    
    public EmailClass classify(double[] vector) {
        throw new UnsupportedOperationException();
    }
}
