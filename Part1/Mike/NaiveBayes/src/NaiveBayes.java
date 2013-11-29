import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;

public class NaiveBayes {
    
    private final HashMap<String, Double> hamProb;
    private final HashMap<String, Double> spamProb;
    
    public NaiveBayes() {
        hamProb = new HashMap<>();
        spamProb = new HashMap<>();
    }
    
    // Really Naive factorial function
    private double factorial(double value) {
        if(value <= 1.0)
            return value;
        
        return value * factorial(value - 1);
    }
    
    // Natural Logarithm
    private double ln(double value) {
       return (-Math.log(1-value))/value;
    }
    
    public boolean train(ArrayList<SimpleEntry<String, EmailClass>> examples) {
        hamProb.clear();
        spamProb.clear();
        
        int hamWords = 0;
        int spamWords = 0;
        
        for(SimpleEntry<String, EmailClass> example : examples) {
            
            String[] tokens = example.getKey().split(" ");
            
            for(String token : tokens) {
                if(!hamProb.containsKey(token))
                {
                    // Start off with a Laplacian correction
                    hamProb.put(token, 1.0);
                    spamProb.put(token, 1.0);
                    ++hamWords;
                    ++spamWords;
                }
                
                if(example.getValue() == EmailClass.Ham)
                {
                    hamProb.put(token, hamProb.get(token) + 1.0);
                    ++hamWords;
                }
                else
                {
                    spamProb.put(token, spamProb.get(token) + 1.0);
                    ++spamWords;
                }
            }
        }
        
        // Calculate the probability as wordfreq/wordcount
        for(String token : hamProb.keySet()) {
            hamProb.put(token, hamProb.get(token) / (double) hamWords);
            spamProb.put(token, spamProb.get(token) / (double) spamWords);
        }
        
        return true;
    }
    
    public EmailClass classify(String data) {
        String[] tokens = data.split(" ");
        
        // Calculate the word frequency in the message
        int wordCount = 0;
        HashMap<String, Integer> wordFreq = new HashMap<>();
        for(String token : tokens) {
            if(hamProb.containsKey(token)) {
                if(!wordFreq.containsKey(token))
                    wordFreq.put(token, 0);
            
                wordFreq.put(token, wordFreq.get(token) + 1);
                ++wordCount;
            }
            // Else do not bother including it
        }
        
        double spamLikelihood = factorial(wordCount);
        double hamLikelihood = factorial(wordCount);
        for(String token : wordFreq.keySet()) {
            int freq = wordFreq.get(token);
            hamLikelihood *= Math.pow(hamProb.get(token), freq) / factorial(freq);
            spamLikelihood *= Math.pow(spamProb.get(token), freq) / factorial(freq);
        }
        
        // The values still are not exactly as what is specified in his .txt file... but its correctly classified and close!
        System.out.format("ln(hamLikelihood) - ln(spamLikelihood) = %f\n", ln(hamLikelihood) - ln(spamLikelihood));
        return (hamLikelihood > spamLikelihood)? EmailClass.Ham : EmailClass.Spam;
    }
}
