package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileReader fileReader = new FileReader("data/reut2-000.sgm");
        List<String> rawArticles = fileReader.readFile();
        ArticleParser articleParser = new ArticleParser();
        List<Article> list = articleParser.parseArticles(rawArticles);


        if (!list.isEmpty()) {
            System.out.println(list.get(0).toString());
        } else {
            System.out.println("No articles parsed.");
        }
    }
}
