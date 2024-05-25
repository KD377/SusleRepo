package org.example;

import org.example.data.CsvReader;
import org.example.data.PlayerStats;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");

            System.out.println(playerStats.get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}