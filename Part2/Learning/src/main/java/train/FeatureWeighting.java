package train;

import invertedindex.TermData;

public interface FeatureWeighting {
    double calculate_weight(TermData termData, String document);
}
