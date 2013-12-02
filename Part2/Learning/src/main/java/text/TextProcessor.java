package text;

// This class should extended
public class TextProcessor {
    public TextProcessor() {
        
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
}
