package train;

import classification.Email;
import classification.EmailClass;
import classification.LabelledVector;
import invertedindex.InvertedIndex;
import text.TextProcessor;
import java.util.ArrayList;
import java.util.HashMap;

// Produces vector representations from given inverted indexes
// TODO: This isn't really a factory class anymore.... refactor accordingly
// TODO: Transform term frequency weighted vectors into TFIDF
public class VectorFactory {
    
    private final HashMap<String, Integer> termIndexMap;
    private final InvertedIndex invertedIndex;

    // Temporary method for transforming words before being added to an inverted index
    public static String transform(String word) {
        String term = TextProcessor.rstrip(word.toLowerCase());

        //Leave out stop words
        if(!TextProcessor.isSymbol(term))
        {
            if(TextProcessor.isNumber(term))
                return "9999";
            else
                return TextProcessor.porterStem(term);
        }
        else
            return null;
    }
    
    public VectorFactory(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
        
        termIndexMap = new HashMap<>();
        
        // Generate a term->index map for future vectors
        int index = 0;
        for(String term : this.invertedIndex.getTerms()) {
            termIndexMap.put(term, index++);
        }
    }

    // calculate tfidf value with given input parameters
    // Using augmented frequency as found on wikipedia http://en.wikipedia.org/wiki/Tf%E2%80%93idf
    public double tfidf(String term, String document)
    {
        double tf = 0.5 + 0.5 * ((double) invertedIndex.getTermFrequency(term, document) / (double) invertedIndex.getMaxTermFrequency());
        double idf = Math.log((double) invertedIndex.documentCount() / (double) (1 + invertedIndex.getDocumentFrequency(term)));

        return tf * idf;
    }

    public ArrayList<LabelledVector> getLabelledVectors() {       
        ArrayList<LabelledVector> result = new ArrayList<>();
        
        for(String document : invertedIndex.getDocuments()){
            
            EmailClass emailClass = (document.contains("ham"))? EmailClass.Ham : EmailClass.Spam;
            
            LabelledVector emailVector = new LabelledVector();
            emailVector.setEmailClass(emailClass);

            double[] vector = new double[invertedIndex.termCount()];
            
            for(String term : invertedIndex.getTerms()) {
                
                int index = termIndexMap.get(term);
                vector[index] = invertedIndex.getTermFrequency(term, document);
                invertedIndex.getTermFrequency(term, document);
            }
            
            emailVector.setVector(vector);
            
            result.add(emailVector);
        }
        
        return result;
    }
    
    // Converts a new Email to a vector based on the inverted index passed
    public float[] getVector(Email email) {
        float[] vector = new float[invertedIndex.termCount()];
        
        for(String term : email.getWords()) {
            
            int index = getIndex(transform(term));
            if(index != -1) {
                vector[index]++;
            }
        }
        
        return vector;
    }
    
    public int getIndex(String term) {
        if(termIndexMap.containsKey(term)) 
            return termIndexMap.get(term);
        else
            return -1;
    }
}
