package invertedindex;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class HashedIndex extends InvertedIndex {
    
    private final HashMap<String, TermData> termMap;
    private final HashSet<String> documents;
    
    public HashedIndex() {
        termMap = new HashMap<>();
        documents = new HashSet<>();
    }
    
    @Override
    public void add(String term, String document) {
        if(!termMap.containsKey(term)) {
            termMap.put(term, new TermData());
        }
        
        TermData termData = termMap.get(term);
        termData.addTermOccurance(document);
        
        documents.add(document);
    }
    
    @Override
    public int getTermFrequency(String term) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTotalFrequency();
        }
        else return 0;
    }
    
    @Override
    public int getTermFrequency(String term, String document) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTermOcurrance(document);
        }
        else return 0;
    }
    
    @Override
    public Collection<String> getDocuments() {
        return documents;
    }
}
