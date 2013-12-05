package train;

import invertedindex.TermData;

public class TfidfWeighting implements FeatureWeighting {

    private double tfidf(int termFrequency, int documentFrequency, int normalizationValue, int numberOfDocuments)
    {
        return ((double)termFrequency / normalizationValue) * Math.log(((double)numberOfDocuments / documentFrequency));
    }

    @Override
    public double calculate_weight(TermData termData, String document, int maxTermFrequency, int documentCount) {
        return tfidf(termData.getTermFrequency(document), termData.getDocumentFrequency(), maxTermFrequency, documentCount);
    }
}
