package org.example.linguistic.data;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class CsvReader {

    public List<PlayerStats> readCsv(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return new CsvToBeanBuilder<PlayerStats>(reader)
                    .withType(PlayerStats.class)
                    .build()
                    .parse();
        }
    }
}
