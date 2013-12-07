package classification;

import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;
import text.Parser;
import text.TextProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EmailClassifier {

    private static final String NUMBER_REP = "9999";

    private Classifier classifier;
    private Parser parser;
    private boolean useTextPreProcessing;
    private boolean useFeatureSelection;
    private FeatureWeighting weightingMethod;

    private boolean trained = false;
    private int maxTermFrequency = -1;
    private int documentCount = -1;

    // Map from term -> index in vector
    private HashMap<String, Integer> termIndexMap;

    public EmailClassifier(Classifier classifier, FeatureWeighting weightingMethod, boolean useTextPreProcessing, boolean useFeatureSelection) {
        this.classifier = classifier;
        this.parser = new Parser();
        this.termIndexMap = new HashMap<>();
        this.useTextPreProcessing = useTextPreProcessing;
        this.useFeatureSelection = useFeatureSelection;
        this.weightingMethod = weightingMethod;
    }

    public Parser getParser() {
        return parser;
    }

    public boolean isTrained() {
        return trained;
    }

    public int getTermCount() {
        return termIndexMap.size();
    }

    public int getMaxTermFrequency() {
        return maxTermFrequency;
    }

    public int getDocumentCount() {
        return documentCount;
    }

    public void train(List<File> trainingFiles) throws IOException {
        train(trainingFiles, 0.07f, 0.95f);     // Default values
    }

    public void train(List<File> trainingFiles, float lowerPercentile, float upperPercentile) throws IOException {
        InvertedIndex invertedIndex = new HashedIndex();

        trained = false;
        termIndexMap.clear();

        if(upperPercentile < 0 || upperPercentile > 1)
            throw new IllegalArgumentException("Upper Percentile must be a value between 0 and 1");
        if(lowerPercentile < 0 || lowerPercentile > 1)
            throw new IllegalArgumentException("Lower percentile must be a value between 0 and 1");

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

        // Store the values before destroying the inverted index
        maxTermFrequency = invertedIndex.getMaxTermFrequency();
        documentCount = invertedIndex.getDocumentCount();

        // Part 4: Feature Weighting
        ArrayList<LabelledVector> labelledVectors = new ArrayList<>();

        for(String emailDocument : invertedIndex.getDocuments()) {
            LabelledVector emailVector = new LabelledVector();
            emailVector.setEmailClass(emailDocument.contains("ham")? EmailClass.Ham : EmailClass.Spam);
            emailVector.setVector(generateVector(invertedIndex, emailDocument));
            labelledVectors.add(emailVector);
        }

        // Part 5: Classifier Training
        classifier.train(labelledVectors);
        trained = true;
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
            if(termIndexMap.containsKey(alteredTerm))
                localIndex.add(alteredTerm, document);
        }

        // Calculate the actual vector values using the local index
        double[] vector = generateVector(localIndex, emailFile.getName());

        return classifier.classify(vector);
    }

    // Generates a vector with the information from the given inverted index
    private double[] generateVector(InvertedIndex invertedIndex, String document) {
        double[] vectorData = new double[termIndexMap.size()];
        for(String term : invertedIndex.getTerms()) {
            int index = termIndexMap.get(term);
            vectorData[index] = weightingMethod.calculate_weight(invertedIndex.getTermData(term), document, maxTermFrequency, documentCount);
        }

        return vectorData;
    }

    private String performTextPreProcessing(String term) {
        String result = term.toLowerCase();
        result = TextProcessor.strip(result);

        // Completely remove symbolic terms
        if(TextProcessor.isSymbol(result))
            return null;
        else
            return TextProcessor.isNumber(result)? NUMBER_REP :  TextProcessor.porterStem(result);
    }
}
