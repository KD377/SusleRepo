package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleParser {
    public List<Article> parseArticles(List<String> rawArticles) {
        List <Article> articles = new ArrayList<>();

        for (String rawArticle : rawArticles) {
            Article article = parseArticle(rawArticle);
            articles.add(article);
        }

        return articles;
    }

    private Article parseArticle(String rawArticle) {
        String date = this.extractDate(rawArticle);
        String title = this.extractTitle(rawArticle);
        String body = this.extractBody(rawArticle);
        String places = this.extractPlaces(rawArticle);

        return new Article(date,title,body,places);
    }

    private String extractDate(String articleContent) {
        Pattern datePattern = Pattern.compile("<DATE>(.*?)</DATE>");
        Matcher dateMatcher = datePattern.matcher(articleContent);
        return dateMatcher.find() ? dateMatcher.group(1) : "";
    }

    private String extractTitle(String articleContent) {
        Pattern titlePattern = Pattern.compile("<TITLE>(.*?)</TITLE>");
        Matcher titleMatcher = titlePattern.matcher(articleContent);
        return titleMatcher.find() ? titleMatcher.group(1) : "";
    }

    private String extractBody(String articleContent) {
        Pattern bodyPattern = Pattern.compile("<BODY>(.*?)</BODY>");
        Matcher bodyMatcher = bodyPattern.matcher(articleContent);
        return bodyMatcher.find() ? bodyMatcher.group(1) : "";
    }

    private String extractPlaces(String articleContent) {
        Pattern placesPattern = Pattern.compile("<PLACES>(.*?)</PLACES>");
        Matcher placesMatcher = placesPattern.matcher(articleContent);
        return placesMatcher.find() ? placesMatcher.group(1) : "";
    }
}
