package text;

// This class should extended
public class TextProcessor {
    private final Stemmer stemmer;
    
    public TextProcessor() {
        stemmer = new Stemmer();
    }
    
    public String lemmatise(String word) {
        // TODO: Implement if time permits
        throw new UnsupportedOperationException();
    }
    
    public String weakStem(String word) {
        // TODO: Test performance with and without weak stemmer
        // TODO: Add more suffixes

        if (word.endsWith("s") && word.length() > 1)
            return word.substring(0, word.length() - 1);

        return word;
    }
    
    public String porterStem(String word) {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        
        return stemmer.toString();
    }
}
