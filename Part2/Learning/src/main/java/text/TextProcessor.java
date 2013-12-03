package text;

// This class should extended with any text pre or post processing methods
// such as spell checking, stemming, lemmatisation etc...
public class TextProcessor {
    private final Stemmer stemmer;
    
    public TextProcessor() {
        stemmer = new Stemmer();
    }
    
    public String lemmatise(String word) {
        // TODO: Implement if time permits
        throw new UnsupportedOperationException();
    }
    
    // Weak Stemming algorithm - prefer porterStem over this
    public String weakStem(String word) {
        // TODO: Test performance with and without weak stemmer
        // TODO: Add more suffixes

        if (word.endsWith("s") && word.length() > 1)
            return word.substring(0, word.length() - 1);

        return word;
    }
    
    /***
     * Porter Stemmer algorithm designed and developed by M.F. Porter 
     * http://tartarus.org/martin/PorterStemmer/. Only works with English
     * words - will give inaccurate results with other languages.
     * @param word String word (in English) to stem.
     * @return String stem of the input word.
     */
    public String porterStem(String word) {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        
        return stemmer.toString();
    }
}
