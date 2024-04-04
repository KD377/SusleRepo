package org.example.reader;

import java.util.HashMap;

public class Article {
    private String date;
    private String title;
    private String body;
    private String places;

    private Object[] featureVector;

    public Article(String date, String title, String body, String places) {
        this.date = date;
        this.title = title;
        this.body = body;
        this.places = places;

        this.featureVector = new Object[10];
    }

    public void createFeatureVector() {
        int wordCount =FeatureExtractor.extractWordCount(this.body);
        int letterCount=FeatureExtractor.extractLetterCount(this.body);
        String currency = FeatureExtractor.extractCurrency(this.body);
        int wordsOverTen = FeatureExtractor.extractNumWordsOverTen(this.body);
        String city = FeatureExtractor.extractCity(this.body);
        double meanWordLen = FeatureExtractor.extractMeanWordLen(this.body);
        int sentences = FeatureExtractor.extractNumberOfSentences(this.body);
        int unique = FeatureExtractor.extractUniqueWordCount(this.body);
        String country = FeatureExtractor.extractCountryOrAdj(this.body);
        double stosunek = FeatureExtractor.extractStosunek(this.body);

        featureVector[0] = wordCount;
        featureVector[1] = letterCount;
        featureVector[2] = currency;
        featureVector[3] = wordsOverTen;
        featureVector[4] = city;
        featureVector[5] = meanWordLen;
        featureVector[6] = sentences;
        featureVector[7] = unique;
        featureVector[8] = country;
        featureVector[9] = stosunek;
    }

    public String getPlaces() {
        return places;
    }

    public Object[] getFeatureVector() {
        return featureVector;
    }

    @Override
    public String toString() {
        return "Article{" +
                "date='" + date + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", places='" + places + '\'' +
                '}';
    }
}
