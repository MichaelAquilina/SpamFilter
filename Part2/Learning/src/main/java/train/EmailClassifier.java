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

    private static final String NUMBER_REP = "9999";

    private Classifier classifier;
    private InvertedIndex invertedIndex;
    private Parser parser;
    private InvertedIndex stopWordIndex;
    private boolean useTextPreProcessing;
    private boolean useFeatureSelection;
    private FeatureWeighting weightingMethod;

    // Map from term -> index in vector
    private HashMap<String, Integer> termIndexMap;

    public EmailClassifier(Classifier classifier, FeatureWeighting weightingMethod, boolean useTextPreProcessing, boolean useFeatureSelection) {
        this.classifier = classifier;
        this.invertedIndex = new HashedIndex();
        this.parser = new Parser();
        this.stopWordIndex = loadStopWordsIndex();
        this.termIndexMap = new HashMap<>();
        this.useTextPreProcessing = useTextPreProcessing;
        this.useFeatureSelection = useFeatureSelection;
        this.weightingMethod = weightingMethod;
    }

    public Parser getParser() {
        return parser;
    }

    public void train(List<File> trainingFiles) throws IOException {
        invertedIndex.clear();
        termIndexMap.clear();

        // TODO: Make these values alterable
        final float upperPercentile = 0.9f;
        final float lowerPercentile = 0.07f;

        // Part 1: Parsing and pre-processing of text
        for(File trainingFile : trainingFiles) {

            Email email = parser.parseFile(trainingFile);
            for(String term : email.getWords()) {
                String alteredTerm = useTextPreProcessing? performTextPreProcessing(term) : term;

                if(alteredTerm != null)
                    invertedIndex.add(alteredTerm, trainingFile.getName());
            }
        }

        // Part 2: Feature Selection
        if (useFeatureSelection) {
            int minDocFrequency = (int) (lowerPercentile * invertedIndex.getDocumentCount());
            int maxDocFrequency = (int) (upperPercentile * invertedIndex.getDocumentCount());
            invertedIndex.trimIndex(minDocFrequency, maxDocFrequency);
        }

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
        String document = emailDocument.getEmailFile().getName();

        // Build an inverted index just for this email to calculate values
        // We need a separate one so that does not contaminate the values obtained from training
        InvertedIndex localIndex = new HashedIndex();
        for(String term : emailDocument.getWords()) {
            String alteredTerm = useTextPreProcessing? performTextPreProcessing(term) : term;

            // Only allow terms in the build index to be added
            if(invertedIndex.containsTerm(alteredTerm))
                localIndex.add(alteredTerm, document);
        }

        int maxTermFrequency = invertedIndex.getMaxTermFrequency();
        int documentCount = invertedIndex.getDocumentCount();

        // Calculate the actual vector values using the local index
        double[] vector = new double[invertedIndex.getTermCount()];
        for(String term : localIndex.getTerms()) {
            int index = termIndexMap.get(term);

            vector[index] = weightingMethod.calculate_weight(localIndex.getTermData(term), document, maxTermFrequency, documentCount);
        }

        return classifier.classify(vector);
    }

    public int getTermCount() {
        return invertedIndex.getTermCount();
    }

    public int getDocumentCount() {
        return invertedIndex.getDocumentCount();
    }

    private String performTextPreProcessing(String term) {
        String result = term.toLowerCase();
        result = TextProcessor.strip(result);

        // Completely remove symbolic terms and stop words
        if(stopWordIndex.containsTerm(result) || TextProcessor.isSymbol(result))
            return null;
        else
            return TextProcessor.isNumber(result)? NUMBER_REP :  TextProcessor.porterStem(result);
    }

    // Extracts labelled vectors from the current inverted index
    private ArrayList<LabelledVector> extractVectors() {
        ArrayList<LabelledVector> vectors = new ArrayList<>();

        int maxTermFrequency = invertedIndex.getMaxTermFrequency();
        int documentCount = invertedIndex.getDocumentCount();

        for(String emailDocument : invertedIndex.getDocuments()) {
            LabelledVector emailVector = new LabelledVector();
            emailVector.setEmailClass(emailDocument.contains("ham")? EmailClass.Ham : EmailClass.Spam);

            double[] vectorData = new double[invertedIndex.getTermCount()];
            for(String term : invertedIndex.getTerms()) {
                int index = termIndexMap.get(term);
                vectorData[index] = weightingMethod.calculate_weight(invertedIndex.getTermData(term), emailDocument, maxTermFrequency, documentCount);
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
