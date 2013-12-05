package train;

import classification.Classifier;
import classification.Email;
import classification.EmailClass;
import classification.LabelledVector;
import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;
import text.Parser;
import text.TextProcessor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class EmailClassifier {

    private Classifier classifier;
    private InvertedIndex invertedIndex;
    private Parser parser;
    private InvertedIndex stopWordIndex;

    // Map from term -> index in vector
    private HashMap<String, Integer> termIndexMap;

    public EmailClassifier(Classifier classifier) {
        this.classifier = classifier;
        this.invertedIndex = new HashedIndex();
        this.parser = new Parser();
        this.stopWordIndex = loadStopWordsIndex();
        this.termIndexMap = new HashMap<>();
    }

    public void train(List<File> trainingFiles) throws IOException {
        invertedIndex.clear();
        termIndexMap.clear();

        // TODO: Make these values alterable
        float upperPercentile = 0.9f;
        float lowerPercentile = 0.07f;

        // Part 1: Parsing and pre-processing of text

        for(File trainingFile : trainingFiles) {

            Email email = parser.parseFile(trainingFile);
            for(String term : email.getWords()) {
                String alteredTerm = term.toLowerCase();
                alteredTerm = TextProcessor.rstrip(alteredTerm);

                if(!stopWordIndex.containsTerm(alteredTerm) && !TextProcessor.isSymbol(alteredTerm)) {
                    if(TextProcessor.isNumber(alteredTerm)) {
                        invertedIndex.add("9999", trainingFile.getName());
                    }
                    else
                    {
                        String stemmedTerm = TextProcessor.porterStem(alteredTerm);
                        invertedIndex.add(stemmedTerm, trainingFile.getName());
                    }
                }
            }
        }

        // Part 2: Feature Selection
        int minDocFrequency = (int) (lowerPercentile * invertedIndex.getDocumentCount());
        int maxDocFrequency = (int) (upperPercentile * invertedIndex.getDocumentCount());
        invertedIndex.trimIndex(minDocFrequency, maxDocFrequency);

        // Part 3: Generate term -> index Map
        int index = 0;
        for(String term : invertedIndex.getTerms()) {
            termIndexMap.put(term, index++);
        }

        // Part 4: Feature Weighting
        ArrayList<LabelledVector> vectors = extractVectors();

        // Part 5: Classifier Training
        classifier.train(vectors);
    }

    public EmailClass classify(File emailFile) throws IOException {
        Email emailDocument = parser.parseFile(emailFile);
        EmailClass emailClass = EmailClass.Unknown;
        double[] vector = new double[invertedIndex.getTermCount()];

        for(String term : emailDocument.getWords()) {
            String alteredTerm = term.toLowerCase();
            alteredTerm = TextProcessor.rstrip(alteredTerm);

            if(TextProcessor.isNumber(alteredTerm))
                alteredTerm = "9999";
            else
                alteredTerm = TextProcessor.porterStem(alteredTerm);

            if(termIndexMap.containsKey(alteredTerm)) {
                int index = termIndexMap.get(alteredTerm);

                vector[index]++;
            }
        }

        return classifier.classify(vector);
    }

    public int getTermCount() {
        return invertedIndex.getTermCount();
    }

    public int getDocumentCount() {
        return invertedIndex.getDocumentCount();
    }

    // calculate tfidf value with given input parameters
    // Using augmented frequency as found on wikipedia http://en.wikipedia.org/wiki/Tf%E2%80%93idf
    private double tfidf(String term, String document)
    {
        double tf = 0.5 + 0.5 * ((double) invertedIndex.getTermFrequency(term, document) / (double) invertedIndex.getMaxTermFrequency());
        double idf = Math.log((double) invertedIndex.getDocumentCount() / (double) (1 + invertedIndex.getDocumentFrequency(term)));

        return tf * idf;
    }

    // Extracts labelled vectors from the current inverted index
    private ArrayList<LabelledVector> extractVectors() {
        ArrayList<LabelledVector> vectors = new ArrayList<>();

        for(String emailDocument : invertedIndex.getDocuments()) {
            LabelledVector emailVector = new LabelledVector();
            emailVector.setEmailClass(emailDocument.contains("ham")? EmailClass.Ham : EmailClass.Spam);

            double[] vectorData = new double[invertedIndex.getTermCount()];
            for(String term : invertedIndex.getTerms()) {
                int index = termIndexMap.get(term);
                vectorData[index] = invertedIndex.getTermFrequency(term, emailDocument);
            }

            emailVector.setVector(vectorData);
            vectors.add(emailVector);
        }

        return vectors;
    }

    //TODO: Make this class less dependent on external files
    private InvertedIndex loadStopWordsIndex() {
        try {
            InvertedIndex stopwordsIndex = new HashedIndex();

            InputStream stream = Train.class.getResourceAsStream("stopwords.txt");

            BufferedReader stopwords = new BufferedReader(new InputStreamReader(stream));

            String line;
            while((line = stopwords.readLine()) != null)
                stopwordsIndex.add(line, "stopwords.txt");

            stopwords.close();

            return stopwordsIndex;
        } catch(IOException e) {
            System.err.println("There was an Error loading the stopwords Index");
            System.err.println("Expect feature selection to degrade");
            return new HashedIndex();
        }
    }
}
