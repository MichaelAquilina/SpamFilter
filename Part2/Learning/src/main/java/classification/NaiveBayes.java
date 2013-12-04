package classification;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NaiveBayes extends Classifier {

    private int d = 0;
    private double[] posProbs = null;
    private double[] negProbs = null;
    
    public void train(ArrayList<LabelledVector> examples) {
        // We assume that all vectors are of equal length 
        // and that there is at least one vector for training.
        assert(examples.size() > 0);
        
        // Initialise the data structures
        d = examples.get(0).getVector().length;
        posProbs = new double[d];
        negProbs = new double[d];

        double posCount = d;
        double negCount = d;
        // Pass 0: Add Laplace correction
        for (int i = 0; i < d; i++) {
            negProbs[i] = 1.0;
            posProbs[i] = 1.0;
        }

        // Pass 1: Count occurences
        for (LabelledVector example : examples) {
            for (int i = 0; i < d; i++) {
                if (example.getEmailClass() == EmailClass.Spam) {
                    posCount += example.getVector()[i];
                    posProbs[i] += example.getVector()[i];
                } else {
                    negCount += example.getVector()[i];
                    negProbs[i] += example.getVector()[i];
                }
            }
        }
        
        // Pass 2: Calculate correct log-probabilities
        for (int i = 0; i < d; i++) {
            posProbs[i] = Math.log(posProbs[i] / posCount);
            negProbs[i] = Math.log(negProbs[i] / negCount);
        }
    }

    private double fac(int n) {
        if (n <= 1) return 1; else return n*fac(n-1);
    }
    
    public EmailClass classify(int[] vector) {
        // We should get the same input vector as in the training set
        assert(vector.length == d);

        double negPos = 1.0;
        double posPos = 1.0;
        for (int i = 0; i < d; i++) {
            negPos += negProbs[i]*vector[i] - Math.log(fac(vector[i]));
            posPos += posProbs[i]*vector[i] - Math.log(fac(vector[i]));
        }

        System.out.format("%f vs %f\n", negPos, posPos);
        if (negPos > posPos) {
            return EmailClass.Ham;
        } else {
            return EmailClass.Spam;
        }
    }
}

