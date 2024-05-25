package org.example;

import org.example.data.CsvReader;
import org.example.data.PlayerStats;
import org.example.fuzzy.FuzzySet;
import org.example.generator.LinguisticSummary;
import org.example.generator.LinguisticVariable;
import org.example.generator.SummaryGenerator;
import org.example.membership.TrapezoidalMembershipFunction;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");

            LinguisticVariable gamesPlayed = new LinguisticVariable("gamesPlayed");
            gamesPlayed.addTerm("mało", new FuzzySet(new TrapezoidalMembershipFunction(9,16,27,33)));

            LinguisticSummary summary = SummaryGenerator.generateSummary(playerStats, gamesPlayed, "many");
            System.out.println(summary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}