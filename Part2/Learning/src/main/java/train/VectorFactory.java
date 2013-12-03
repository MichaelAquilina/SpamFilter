package train;

import classification.EmailClass;
import classification.LabelledVector;
import invertedindex.InvertedIndex;
import java.util.ArrayList;
import java.util.HashMap;

// Produces vector representations from given inverted indexes
public class VectorFactory {
    
    private final HashMap<String, Integer> termIndexMap;
    private final InvertedIndex invertedIndex;
    
    public VectorFactory(InvertedIndex invertedIndex) {
        this.invertedIndex = invertedIndex;
        
        termIndexMap = new HashMap<>();
        
        // Generate a term->index map for future vectors
        int index = 0;
        for(String term : this.invertedIndex.getTerms()) {
            termIndexMap.put(term, index++);
        }
    }
    
    //Currently this takes ages to finish due to the large dimensions
    public ArrayList<LabelledVector> getLabelledVectors() {       
        ArrayList<LabelledVector> result = new ArrayList<>();
        
        for(String document : invertedIndex.getDocuments()){
            
            EmailClass emailClass = (document.contains("ham"))? EmailClass.Ham : EmailClass.Spam;
            
            LabelledVector emailVector = new LabelledVector();
            emailVector.setEmailClass(emailClass);
            
            float[] vector = new float[invertedIndex.termCount()];
            
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
    
    public int getIndex(String term) {
        if(termIndexMap.containsKey(term)) 
            return termIndexMap.get(term);
        else
            return -1;
    }
}