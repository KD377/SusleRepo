package org.example.reader;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.jshell.spi.ExecutionControl;


import java.util.HashMap;
import java.util.List;

public class FeatureExtractor {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));

    private static final SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
    private static final PorterStemmer stemmer = new PorterStemmer();

    public static int extractWordCount(String text) {
        String[] tokens = tokenizer.tokenize(text);
        return (int) Arrays.stream(tokens)
                .filter(token -> !STOP_WORDS.contains(token.toLowerCase()))
                .count();
    }

    public static int extractLetterCount(String text) {
        String[] tokens = tokenizer.tokenize(text);
        return Arrays.stream(tokens)
                .filter(token -> !STOP_WORDS.contains(token.toLowerCase()))
                .mapToInt(String::length)
                .sum();
    }

    public static String extractCurrency(String text) {
        // Dodano pełne nazwy walut do wzorca
        Pattern pattern = Pattern.compile("\\b(USD|EUR|JPY|GBP|dollar|euro|yen|pound sterling)\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String matched = matcher.group();
            // Mapowanie nazw słownych i symboli do standardowych kodów walut
            switch (matched.toLowerCase()) {
                case "dollar":
                    return "USD";
                case "euro":
                    return "EUR";
                case "yen":
                    return "JPY";
                case "pound sterling":
                    return "GBP";
                default:
                    return matched;
            }
        }
        return "None";
    }

    public static int extractNumWordsOverTen(String text) {
        String[] tokens = tokenizer.tokenize(text);
        return (int) Arrays.stream(tokens)
                .filter(token -> token.length() >= 10)
                .count();
    }

    public static double extractMeanWordLen(String text) {
        String[] tokens = tokenizer.tokenize(text);
        OptionalDouble average = Arrays.stream(tokens)
                .filter(token -> !STOP_WORDS.contains(token.toLowerCase()))
                .mapToInt(String::length)
                .average();
        return average.isPresent() ? average.getAsDouble() : 0.0;
    }

    public static String extractCity(String text) {
        Map<String, Integer> cityCounts = new HashMap<>();
        List<String> cities = Arrays.asList("Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart",
                "New York City", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia",
                "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes",
                "London", "Birmingham", "Manchester", "Glasgow", "Liverpool", "Newcastle",
                "Toronto", "Montreal", "Vancouver", "Calgary", "Edmonton", "Ottawa",
                "Tokyo", "Yokohama", "Osaka", "Nagoya", "Sapporo", "Fukuoka");
        String[] tokens = tokenizer.tokenize(text);
        for (String token : tokens) {
            if (cities.contains(token)) {
                cityCounts.put(token, cityCounts.getOrDefault(token, 0) + 1);
            }
        }
        return cityCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("None");
    }

    public static int extractNumberOfSentences(String text) {
        String[] sentences = text.split("[.!?]\\s+");
        return sentences.length;
    }

    public static int extractUniqueWordCount(String text) {
        Set<String> uniqueWords = new HashSet<>();
        String[] tokens = tokenizer.tokenize(text);
        for (String token : tokens) {
            if (!STOP_WORDS.contains(token.toLowerCase())) {
                uniqueWords.add(token.toLowerCase());
            }
        }
        return uniqueWords.size();
    }

    public static String extractCountryOrAdj(String text) {
        Map<String, Integer> countryCounts = new HashMap<>();
        Map<String, String> countryToAdjective = Map.ofEntries(
                Map.entry("United States of America", "American"),
                Map.entry("Japan", "Japanese"),
                Map.entry("West Germany", "German"),
                Map.entry("United Kingdom", "British"),
                Map.entry("Canada", "Canadian"),
                Map.entry("France", "French")
        );
        countryToAdjective.forEach((country, adj) -> {
            Pattern pattern = Pattern.compile("\\b" + country + "\\b|\\b" + adj + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String matched = matcher.group();
                countryCounts.put(matched, countryCounts.getOrDefault(matched, 0) + 1);
            }
        });
        return countryCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("None");
    }

    public static double extractStosunek(String text) {
        int originalLength = tokenizer.tokenize(text).length;
        int lengthAfterRemovingStopWords = extractWordCount(text);
        return originalLength > 0 ? (double) lengthAfterRemovingStopWords / originalLength : 0;
    }
}

