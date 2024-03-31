package org.example;

public class Article {
    private String date;
    private String title;
    private String body;
    private String places;

    public Article(String date, String title, String body, String places) {
        this.date = date;
        this.title = title;
        this.body = body;
        this.places = places;
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
