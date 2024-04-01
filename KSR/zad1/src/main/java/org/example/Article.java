package org.example;

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

    public Object[] createFeatureVector() throws Exception {
        throw new Exception("Not implemented"); // USE FOR THIS FEATURE EXTRACTOR Utility class methods
        //Implement them first then use article body as argument
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
