package invertedindex;

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
}
