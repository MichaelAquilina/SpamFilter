package train;

import classification.Classifier;
import classification.Email;
import invertedindex.HashedIndex;
import invertedindex.InvertedIndex;
import text.Parser;
import text.TextProcessor;

import java.io.File;
import java.io.IOException;

public class EmailClassifier {

    private Classifier classifier;
    private InvertedIndex invertedIndex;

    public EmailClassifier(Classifier classifier, InvertedIndex invertedIndex) {
        this.classifier = classifier;
        this.invertedIndex = invertedIndex;
    }

    public void train(String trainingPath) throws IOException {

        float upperPercentile = 0.9f;
        float lowerPercentile = 0.07f;

        // Part 1: Parsing and pre-processing of text
        Parser parser = new Parser();
        InvertedIndex stopwordIndex = new HashedIndex();

        File trainingDirectory = new File(trainingPath);
        File[] trainingFiles = trainingDirectory.listFiles(new SpamHamFileFilter());

        for(File trainingFile : trainingFiles) {

            Email email = parser.parseFile(trainingFile);
            for(String term : email.getWords()) {

                if(!stopwordIndex.containsTerm(term)) {
                    if(!TextProcessor.isNumber(term)) {
                        invertedIndex.add("9999", trainingFile.getName());
                    }
                    else
                    {
                        String stemmedTerm = TextProcessor.porterStem(term);
                        invertedIndex.add(stemmedTerm, trainingFile.getName());
                    }
                }
            }
        }

        // Part 2: Feature Selection
        int minDocFrequency = (int) (lowerPercentile * invertedIndex.documentCount());
        int maxDocFrequency = (int) (upperPercentile * invertedIndex.documentCount());
        invertedIndex.trimIndex(minDocFrequency, maxDocFrequency);

        // Part 3: Feature Weighting
        
    }
}
