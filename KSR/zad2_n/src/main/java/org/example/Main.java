package org.example;

import org.example.data.CsvReader;
import org.example.data.PlayerStats;
import org.example.generator.SummaryGenerator;


import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");


            System.out.println(SummaryGenerator.generateSummary(playerStats, List.of("młody wiek"), "około 5000"));


            System.out.println(SummaryGenerator.generateSummary(playerStats, List.of("średni wiek"), "większość", "mało gier rozegranych"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}