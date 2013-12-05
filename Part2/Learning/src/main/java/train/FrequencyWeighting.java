package train;

import invertedindex.TermData;

public class FrequencyWeighting implements FeatureWeighting {
    @Override
    public double calculate_weight(TermData termData, String document) {
        return termData.getTermFrequency(document);
    }
}
