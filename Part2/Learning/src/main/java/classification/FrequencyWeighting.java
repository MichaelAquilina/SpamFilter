package classification;

import classification.FeatureWeighting;
import invertedindex.TermData;

public class FrequencyWeighting implements FeatureWeighting {
    @Override
    public double calculate_weight(TermData termData, String document, int maxTermFrequency, int documentCount) {
        return termData.getTermFrequency(document);
    }
}
