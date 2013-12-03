package invertedindex;

import java.util.ArrayList;
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
    public int getDocumentFrequency(String term) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getDocumentFrequency();
        }
        else return 0;
    }
    
    @Override
    public Collection<String> getDocuments() {
        return documents;
    }
    
    @Override
    public void trimIndex(int min, int max) {
        ArrayList<String> trash = new ArrayList<>();
        for(String term : termMap.keySet()) {
            TermData termData = termMap.get(term);
            
            if(termData.getDocumentFrequency()< min || termData.getDocumentFrequency()> max)
                trash.add(term);
        }
        
        for(String term : trash)
            termMap.remove(term);
    }
    
    @Override
    public int getMaxTermFrequency() {
        if(termMap.isEmpty())
            return -1;
        
        int max = Integer.MIN_VALUE;
        for(String term : termMap.keySet()) {
            TermData termData = termMap.get(term);
            
            if(termData.getTotalFrequency() > max) {
                max = termData.getTotalFrequency();
            }
        }
        
        return max;
    }
    
    @Override
    public int getMinTermFrequency() {
        if(termMap.isEmpty())
            return -1;
        
        int min = Integer.MAX_VALUE;
        for(String term : termMap.keySet()) {
            TermData termData = termMap.get(term);
            
            if(termData.getTotalFrequency() < min) {
                min = termData.getTotalFrequency();
            }
        }
        
        return min;
    }
    
    @Override
    public boolean remove(String term) {
        return termMap.remove(term) != null;
    }
    
    @Override
    public int size() {
        return termMap.size();
    }
    
    @Override
    public Collection<String> getTerms() {
        return termMap.keySet();
    }
}
