package classification;

import invertedindex.TermData;

public interface FeatureWeighting {
    double calculate_weight(TermData termData, String document, int maxTermFrequency, int documentCount);
}
