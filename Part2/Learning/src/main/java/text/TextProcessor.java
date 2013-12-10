package text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// This class should extended with any text pre or post processing methods
// such as spell checking, stemming, lemmatisation etc...
public class TextProcessor {
    private static final Stemmer stemmer = new Stemmer();

    private static Pattern currencyPattern = Pattern.compile("\\$[0-9]+");

    private static Pattern urlPattern = Pattern.compile("^https?://([^\\.]*.)([^\\/]*).*");
    private static Pattern emailAddressPattern = Pattern.compile("^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]+)");

    private static Pattern numberPattern = Pattern.compile("^([0-9]+,?)*([0-9]+,?)*[0-9]+.?[0-9]+");

    public static String extractMailDomain(String emailAddress) {
        Matcher matcher = emailAddressPattern.matcher(emailAddress);
        if(matcher.find())
            return matcher.group(2) + "." + matcher.group(3);
        else
            return null;
    }

    public static String extractUrlDomain(String url) {
        Matcher matcher = urlPattern.matcher(url);
        if(matcher.find())
            return matcher.group(2);
        else
            return null;
    }

    public static boolean isEmailAddress(String text) {
        return emailAddressPattern.matcher(text).find();
    }

    public static boolean isUrl(String text) {
        return urlPattern.matcher(text).find();
    }

    public static String stripAttributes(String word) {
        final Pattern attributePattern = Pattern.compile("(src=|href=|mailto:|value=)\"?'?([^\"']*)");

        boolean repeat;
        do {
            repeat = false;

            Matcher matcher = attributePattern.matcher(word);
            if(matcher.find()) {
                word = matcher.group(2);
                repeat = true;
            }
        } while(repeat);

        return word;
    }

    // Helper method that calls lstrip followed by rstrip
    public static String strip(String word) {
        String result = lstrip(word);
        result = rstrip(result);

        return result;
    }

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
