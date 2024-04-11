package org.example;

import org.example.algorithm.Knn;
import org.example.algorithm.Metrics;
import org.example.reader.Article;
import org.example.reader.ArticleParser;
import org.example.reader.FeatureExtractor;
import org.example.reader.FileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        FileReader fileReader = new FileReader("./data");
        List<String> rawArticles;
        try {
            rawArticles = fileReader.readFile(22);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ArticleParser articleParser = new ArticleParser();
        List<Article> articles = articleParser.parseArticles(rawArticles);
        articles.removeIf(article -> article.getPlaces().isEmpty());

        HashMap<Object[], String> data = new HashMap<>();
        for (Article article : articles) {
            article.createFeatureVector();
            data.put(article.getFeatureVector(), article.getPlaces());
        }

        Knn algorithm = new Knn(data, 80, 12);
        algorithm.classifyTestingSet(Metrics::euclideanDistance);

        int correctPredictions = 0;
        HashMap<Object[], String> predictedLabels = algorithm.getTestingSet();
        HashMap<Object[], String> originalLabels = algorithm.getOriginalLabelsForTestingSet();

        for (Map.Entry<Object[], String> entry : predictedLabels.entrySet()) {
            Object[] testVector = entry.getKey();
            String predictedLabel = entry.getValue();
            String originalLabel = originalLabels.get(testVector);
            //System.out.println("Original: " + originalLabel+ " zaklasyfikowano: " + predictedLabel);

            if (predictedLabel != null && predictedLabel.equals(originalLabel)) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / predictedLabels.size();
        String[] labels ={"WEST_GERMANY", "USA", "FRANCE", "UK", "CANADA", "JAPAN"};

        for (String label : labels) {
            calculateMetrics(label, predictedLabels, originalLabels);
        }
        System.out.println("Dokładność klasyfikacji: " + accuracy);
    }
    private static void calculateMetrics(String label, HashMap<Object[], String> predictedLabels, HashMap<Object[], String> originalLabels) {
        int truePositives = 0;
        int falsePositives = 0;
        int falseNegatives = 0;

        for (Map.Entry<Object[], String> entry : predictedLabels.entrySet()) {
            Object[] testVector = entry.getKey();
            String predictedLabel = entry.getValue();
            String originalLabel = originalLabels.get(testVector);

            if (originalLabel != null && originalLabel.equals(label)) {
                if (predictedLabel != null && predictedLabel.equals(label)) {
                    truePositives++;
                } else {
                    falseNegatives++;
                }
            } else if (predictedLabel != null && predictedLabel.equals(label)) {
                falsePositives++;
            }
        }

        double precision = 0.0;
        double recall = 0.0;
        if (truePositives + falsePositives != 0) {
            precision = (double) truePositives / (truePositives + falsePositives);
        }
        if (truePositives + falseNegatives != 0) {
            recall = (double) truePositives / (truePositives + falseNegatives);
        }

        System.out.println(label + ":");
        System.out.println("Precision: " + precision);
        System.out.println("Recall: " + recall + "\n");
    }

}
