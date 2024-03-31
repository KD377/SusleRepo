package org.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private String filePath;

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> readFile() {
        List<String> articlesRaw = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new java.io.FileReader(this.filePath));
            String line;
            StringBuilder articleBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {

                if (line.startsWith("<REUTERS")) {
                    articleBuilder.setLength(0);
                }
                articleBuilder.append(line).append("\n");

                if (line.startsWith("</REUTERS>")) {
                    articlesRaw.add(articleBuilder.toString());
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return articlesRaw;
    }


}
