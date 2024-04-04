package org.example.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private final String directoryPath;

    public FileReader(String filePath) {
        this.directoryPath = filePath;
    }

    public List<String> readFile(int numberOfFiles) throws Exception {
        if (numberOfFiles < 0 || numberOfFiles > 22)
            throw new Exception("NUmber of files must be positive and less than 22");
        List<String> articlesRaw = new ArrayList<>();
        String path;
        for(int i=0; i < numberOfFiles; i++) {
            if (i < 10){
                path = directoryPath +"/reut2-00"+ i + ".sgm";
            }else {
                path = directoryPath +"/reut2-0"+ i + ".sgm";
            }

            try {
                BufferedReader reader = new BufferedReader(new java.io.FileReader(path));
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
        }
        return articlesRaw;
    }


}
