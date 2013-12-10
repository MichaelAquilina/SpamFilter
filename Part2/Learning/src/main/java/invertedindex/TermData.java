package invertedindex;

import java.util.HashMap;

public class TermData implements Comparable<TermData> {

    private String term;
    private final HashMap<String, Integer> termDocumentFrequency;
    private int totalTermFrequency = 0;
    
    public TermData(String term) {
        this.term = term;
        this.termDocumentFrequency = new HashMap<>();
    }

    public String getTerm() {
        return term;
    }
    
    public void addTermOccurrence(String document) {
        if(!termDocumentFrequency.containsKey(document)) {
            termDocumentFrequency.put(document, 0);
        }
        
        ++totalTermFrequency;
        termDocumentFrequency.put(document, termDocumentFrequency.get(document) + 1);
    }
    
    public int getTermFrequency(String document) {
        if(termDocumentFrequency.containsKey(document))
            return termDocumentFrequency.get(document);
        else
            return 0;
    }
    
    public int getTotalTermFrequency() {
        return totalTermFrequency;
    }
    
    public int getDocumentFrequency() {
        return termDocumentFrequency.size();
    }
    
    @Override
    public String toString() {
        return "TermData: <totalTermFrequency=" + getTotalTermFrequency() + " documentFrequency=" + getDocumentFrequency() + ">";
    }

    @Override
    public int compareTo(TermData termData) {
        return this.getDocumentFrequency() - termData.getDocumentFrequency();
    }
}
