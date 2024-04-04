package org.example.reader;

import jdk.jshell.spi.ExecutionControl;

import java.util.HashMap;
import java.util.List;

public class FeatureExtractor {

    public static HashMap<Object[], String> getData(List<Article> dataSet){
        HashMap<Object[],String> data = new HashMap<>();
        for (Article article : dataSet) {
            article.createFeatureVector();
            data.put(article.getFeatureVector(),article.getPlaces());
        }
        return data;
    }
    public static int extractWordCount(String articleBody) {
        return 1;
    }

    public static int extractLetterCount(String articleBody) {
        return 1;
    }

    public static String extractCurrency(String articleBody) {
        return "USA";
    }

    public static int extractNumWordsOverTen(String articleBody) {
        return 1;
    }

    public static String extractCity(String articleBody) {
        return "USA";
    }

    public static double extractMeanWordLen(String articleBody) {
        return 1.0;
    }

    public static int extractNumberOfSentences(String articleBody) {
        return 1;
    }

    public static int extractUniqueWordCount(String articleBody) {
        return 1;
    }

    public static String extractCountryOrAdj(String articleBody) {
        return "USA";
    }

    public static double extractStosunek(String articleBody) {
        return 1.0;
    }
}
