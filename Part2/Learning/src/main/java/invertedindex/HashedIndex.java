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
        termData.addTermOccurrence(document);
        
        documents.add(document);
    }
    
    @Override
    public int getTermFrequency(String term) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTotalTermFrequency();
        }
        else return 0;
    }
    
    @Override
    public int getTermFrequency(String term, String document) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTermFrequency(document);
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
            
            if(termData.getTotalTermFrequency() > max) {
                max = termData.getTotalTermFrequency();
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
            
            if(termData.getTotalTermFrequency() < min) {
                min = termData.getTotalTermFrequency();
            }
        }
        
        return min;
    }
    
    @Override
    public boolean remove(String term) {
        return termMap.remove(term) != null;
    }
    
    @Override
    public int getTermCount() {
        return termMap.size();
    }
    
    @Override
    public int getDocumentCount() {
        return documents.size();
    }
    
    @Override
    public Collection<String> getTerms() {
        return termMap.keySet();
    }

    @Override
    public void clear() {
        termMap.clear();
        documents.clear();
    }

    @Override
    public TermData getTermData(String term) {
        return termMap.get(term);
    }
}
