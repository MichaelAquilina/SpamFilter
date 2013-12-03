package invertedindex;

import java.util.HashMap;

public class TermData {
    private final HashMap<String, Integer> termDocumentFrequency;
    private int totalFrequency = 0;
    
    public TermData() {
        termDocumentFrequency = new HashMap<>();
    }
    
    public void addTermOccurance(String document) {
        if(!termDocumentFrequency.containsKey(document)) {
            termDocumentFrequency.put(document, 0);
        }
        
        ++totalFrequency;
        termDocumentFrequency.put(document, termDocumentFrequency.get(document) + 1);
    }
    
    public int getTermOcurrance(String document) {
        if(termDocumentFrequency.containsKey(document))
            return termDocumentFrequency.get(document);
        else
            return 0;
    }
    
    public int getTotalFrequency() {
        return totalFrequency;
    }
    
    public int getDocumentFrequency() {
        return termDocumentFrequency.size();
    }
    
    @Override
    public String toString() {
        return "TermData: <total=" + totalFrequency+" documents=" + termDocumentFrequency.size() + ">";
    }
}
