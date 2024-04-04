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
        FileReader fileReader = new FileReader("data");
        List<String> rawArticles = null;
        try {
            rawArticles = fileReader.readFile(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ArticleParser articleParser = new ArticleParser();
        List<Article> list = articleParser.parseArticles(rawArticles);
        for (int i = list.size() - 1; i >= 0; i--) {
            Article article = list.get(i);
            if (article.getPlaces().isEmpty()) {
                list.remove(i); // Remove the article if places is empty
            }
        }
        HashMap<Object[],String> data = FeatureExtractor.getData(list);

        Knn algorithm = new Knn(data,20,3);
        algorithm.classifyTestingSet(Metrics::euclideanDistance);

        for (Map.Entry<Object[], String> entry : algorithm.getTestingSet().entrySet()) {
            System.out.println("Value: " + entry.getValue());
        }
    }
}
