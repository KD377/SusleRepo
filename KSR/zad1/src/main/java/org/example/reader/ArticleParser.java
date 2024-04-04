package org.example.reader;

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
        String correctPlaces = this.extractAndProcessPlaces(places);

        return new Article(date,title,body,correctPlaces);
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
        Pattern bodyPattern = Pattern.compile("<BODY>(.*?)</BODY>", Pattern.DOTALL); // Add Pattern.DOTALL to match newline characters
        Matcher bodyMatcher = bodyPattern.matcher(articleContent);
        if (bodyMatcher.find()) {
            return bodyMatcher.group(1).trim(); // Trim to remove leading/trailing whitespace
        } else {
            return ""; // Return empty string if <BODY> tag not found
        }
    }


    private String extractPlaces(String articleContent) {
        Pattern placesPattern = Pattern.compile("<PLACES>(.*?)</PLACES>");
        Matcher placesMatcher = placesPattern.matcher(articleContent);
        return placesMatcher.find() ? placesMatcher.group(1) : "";
    }

    private String extractAndProcessPlaces(String places) {
        String[] validLabels = {"west-germany", "usa", "france", "uk", "canada", "japan"};
        String result = "";

        // Count the number of valid labels found in the places string
        int count = 0;
        for (String label : validLabels) {
            if (places.contains(label)) {
                count++;
                result = label.toUpperCase(); // Update result with the current label
            }
        }

        // If more than one valid label found or no valid label found, set result to empty string
        if (count != 1) {
            result = "";
        }

        return result;
    }
}
