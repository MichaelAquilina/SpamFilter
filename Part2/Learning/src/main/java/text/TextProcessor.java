package text;

import java.util.regex.Pattern;

// This class should extended with any text pre or post processing methods
// such as spell checking, stemming, lemmatisation etc...
public class TextProcessor {
    private static final Stemmer stemmer = new Stemmer();

    private static Pattern currencyPattern = Pattern.compile("\\$[0-9]+");
    private static Pattern numberPattern = Pattern.compile("^[0-9]+$");

    public static String rstrip(String word) {
        if(word.isEmpty())
            return word;

        String current = word;
        while(!current.isEmpty() && !Character.isLetterOrDigit(current.charAt(current.length() - 1)))
            current = current.substring(0, current.length() - 1);

        return current;
    }

    public static String lstrip(String word) {
        if(word.isEmpty())
            return word;

        String current = word;
        while(!current.isEmpty() && !Character.isLetterOrDigit(current.charAt(0)))
            current = current.substring(1, current.length());

        return current;
    }

    public static boolean isSymbol(String word) {

        for(int i=0; i<word.length(); ++i)
            if(Character.isLetterOrDigit(word.charAt(i)))
                return false;

        return true;
    }

    public static boolean isCurrency(String word) {
        return currencyPattern.matcher(word).find();
    }

    public static boolean isNumber(String word) {
        return numberPattern.matcher(word).find();
    }
    
    public static String lemmatise(String word) {
        // TODO: Implement if time permits
        throw new UnsupportedOperationException();
    }
    
    // Weak Stemming algorithm - prefer porterStem over this
    public static String weakStem(String word) {
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
    public static String porterStem(String word) {
        stemmer.add(word.toCharArray(), word.length());
        stemmer.stem();
        
        return stemmer.toString();
    }
}
