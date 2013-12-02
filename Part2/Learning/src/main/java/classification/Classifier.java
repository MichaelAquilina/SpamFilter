package classification;

import java.util.ArrayList;

public abstract class Classifier {
    
    void train(ArrayList<LabelledVector> examples) {
        throw new UnsupportedOperationException();
    }
    
    EmailClass classify(int[] vector) {
        throw new UnsupportedOperationException();
    }
}
