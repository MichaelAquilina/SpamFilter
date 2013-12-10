package invertedindex;

import java.util.ArrayList;
import java.util.Collection;

public abstract class InvertedIndex {
    
    public Collection<String> getDocuments() {
        throw new UnsupportedOperationException();
    }
    
    public void add(String term, String document) {
        throw new UnsupportedOperationException();
    }
    
    public int getTermFrequency(String term) {
        throw new UnsupportedOperationException();
    }
    
    public int getTermFrequency(String term, String document) {
        throw new UnsupportedOperationException();
    }
    
    public int getDocumentFrequency(String term) {
        throw new UnsupportedOperationException();
    }
    
    // Should return -1 if no terms available
    public int getMaxTermFrequency() {
        throw new UnsupportedOperationException();
    }
    
    // Should return -1 if no terms available
    public int getMinTermFrequency() {
        throw new UnsupportedOperationException();
    }

    public ArrayList<String> getOuterWords(int min, int max) {
        throw new UnsupportedOperationException();
    }
    
    public void trimIndex(int min, int max) {
        throw new UnsupportedOperationException();
    }
       
    public boolean containsTerm(String term) {
        return getTermFrequency(term) > 0;
    }
    
    public boolean remove(String term) {
        throw new UnsupportedOperationException();
    }
    
    public int getTermCount() {
        throw new UnsupportedOperationException();
    }
    
    public int getDocumentCount() {
        throw new UnsupportedOperationException();
    }
    
    public Collection<String> getTerms() {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public TermData getTermData(String term) {
        throw new UnsupportedOperationException();
    }
}
