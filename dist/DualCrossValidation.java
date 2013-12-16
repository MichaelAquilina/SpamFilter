package spamfilter;

import classification.ConfusionMatrix;
import classification.EmailClass;
import classification.EmailClassifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DualCrossValidation extends CrossValidation {
    private EmailClassifier classifier2;
    private ConfusionMatrix confusionMatrix2;
    private List<ConfusionMatrix> confusionMatrices2;

    public DualCrossValidation(String directory, EmailClassifier classifier, EmailClassifier classifier2) {
        super(directory, classifier);
        this.classifier2 = classifier2;
    }

    @Override
    public void fold(int folds, double lowerPercentile, double upperPercentile) throws IOException {
        // Shuffle the list, so that we get each time other results
        Collections.shuffle(files);

        // Split into folds
        List<List<File>> fileFolds = new ArrayList<List<File>>(folds);
        int foldLength = files.size() / folds;
        for (int i = 0; i < folds; i++) {
            int end = (i == (folds - 1)) ? files.size() : ((i+1) * foldLength);
            fileFolds.add(files.subList(i * foldLength, end));
        }

        // For each combination spamfilter and test
        confusionMatrix = new ConfusionMatrix();
        confusionMatrices = new ArrayList<ConfusionMatrix>();
        confusionMatrix2 = new ConfusionMatrix();
        confusionMatrices2 = new ArrayList<ConfusionMatrix>();
        for (int i = 0; i < folds; i++) {
            // Gather training data
            List<File> train = new ArrayList<File>();
            for (int j = 0; j < folds; j++) {
                if (j != i) {
                    train.addAll(fileFolds.get(j));
                }
            }

            classifier.train(train, lowerPercentile, upperPercentile);
            classifier2.train(train, lowerPercentile, upperPercentile);
            
            ConfusionMatrix cm = new ConfusionMatrix();
            ConfusionMatrix cm2 = new ConfusionMatrix();
            for(File trainingFile : fileFolds.get(i)) {
                EmailClass actualClass = trainingFile.getName().contains("ham")? EmailClass.Ham : EmailClass.Spam;
                EmailClass emailClass = classifier.classify(trainingFile);
                EmailClass emailClass2 = classifier2.classify(trainingFile);

                confusionMatrix.add(actualClass, emailClass);
                confusionMatrix2.add(actualClass, emailClass2);
                cm.add(actualClass, emailClass);
                cm2.add(actualClass, emailClass2);
            }
            System.out.println("Fold done:");
            System.out.format("(%f, %f, %f)\n", cm.getAccuracy(), cm.getPrecision(), cm.getRecall());
            System.out.format("(%f, %f, %f)\n", cm2.getAccuracy(), cm2.getPrecision(), cm2.getRecall());
            System.out.format("(%f, %f, %f)\n", cm.getAccuracy() - cm2.getAccuracy(), cm.getPrecision() - cm2.getPrecision(), cm.getRecall() - cm2.getRecall());
            confusionMatrices.add(cm);
            confusionMatrices2.add(cm2);
        }
    }
}
