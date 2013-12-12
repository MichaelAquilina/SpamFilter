package classification;

import invertedindex.TermData;

public class TfidfWeighting implements FeatureWeighting {

    private double tfidf(int termFrequency, int documentFrequency, int maxDocumentTermFrequency, int numberOfDocuments)
    {
        return ((double)termFrequency / (double) maxDocumentTermFrequency) * Math.log(((double)numberOfDocuments / documentFrequency));
    }

    @Override
    public double calculate_weight(TermData termData, String document, int maxDocumentTermFrequency, int documentCount) {
        return tfidf(termData.getTermFrequency(document), termData.getDocumentFrequency(), maxDocumentTermFrequency, documentCount);
    }
}
