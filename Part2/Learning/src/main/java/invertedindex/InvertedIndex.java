package invertedindex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InvertedIndex {
    
    private final HashMap<String, TermData> termMap;
    private final HashSet<String> documents;
    
    public InvertedIndex() {
        termMap = new HashMap<>();
        documents = new HashSet<>();
    }
    
    public void add(String term, String document) {
        if(!termMap.containsKey(term)) {
            termMap.put(term, new TermData(term));
        }
        
        TermData termData = termMap.get(term);
        termData.addTermOccurrence(document);
        
        documents.add(document);
    }

    public int getTermFrequency(String term) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTotalTermFrequency();
        }
        else return 0;
    }

    public int getTermFrequency(String term, String document) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getTermFrequency(document);
        }
        else return 0;
    }

    public int getDocumentFrequency(String term) {
        if(termMap.containsKey(term)) {
            TermData termData = termMap.get(term);
            return termData.getDocumentFrequency();
        }
        else return 0;
    }

    public Collection<String> getDocuments() {
        return documents;
    }

    public ArrayList<TermData> getInnerWords(int min, int max) {
        ArrayList<TermData> result = new ArrayList<>();
        for(String term : termMap.keySet()) {
            TermData termData = termMap.get(term);

            if(termData.getDocumentFrequency()>= min && termData.getDocumentFrequency()<= max)
                result.add(termData);
        }

        return result;
    }

    public ArrayList<TermData> getOuterWords(int min, int max) {
        ArrayList<TermData> result = new ArrayList<>();
        for(String term : termMap.keySet()) {
            TermData termData = termMap.get(term);

            if(termData.getDocumentFrequency()< min || termData.getDocumentFrequency()> max)
                result.add(termData);
        }

        return result;
    }

    public void trimIndex(int min, int max) {
        ArrayList<TermData> trash = getOuterWords(min, max);
        
        for(TermData termData : trash)
            termMap.remove(termData.getTerm());
    }

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
    
    
    public boolean remove(String term) {
        return termMap.remove(term) != null;
    }
    
    
    public int getTermCount() {
        return termMap.size();
    }

    public boolean containsTerm(String term) {
        return getTermData(term) != null;
    }
    
    public int getDocumentCount() {
        return documents.size();
    }
    
    
    public Collection<String> getTerms() {
        return termMap.keySet();
    }

    
    public void clear() {
        termMap.clear();
        documents.clear();
    }

    
    public TermData getTermData(String term) {
        return termMap.get(term);
    }

    
    public void writeTermData(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));

        ArrayList<TermData> termDataList = new ArrayList<>(getInnerWords(6, getDocumentCount()));
        Collections.sort(termDataList);
        Collections.reverse(termDataList);

        for(int i=0; i<termDataList.size(); i+=5) {
            TermData termData = termDataList.get(i);
            String term = termData.getTerm();
            term = term.replace(',', ' ');

            writer.write(String.format("%s, %d\n", term, termData.getDocumentFrequency()));
        }

        writer.close();
    }
}
