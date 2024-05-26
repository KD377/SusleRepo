package org.example;

import org.example.data.CsvReader;
import org.example.data.PlayerStats;
import org.example.generator.Quantifiers;
import org.example.generator.SummaryGenerator;
import org.example.generator.Terms;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

//        CsvReader csvReader = new CsvReader();
//        try {
//            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");
//
//            LinguisticVariable gamesPlayed = new LinguisticVariable("gamesPlayed");
//            gamesPlayed.addTerm("mało rozegranych gier", new FuzzySet(new TrapezoidalMembershipFunction(9,16,27,33)));
//
//            LinguisticSummary summary = SummaryGenerator.generateSummary(playerStats, gamesPlayed, "mało rozegranych gier");
//            System.out.println(summary);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");

            /*System.out.println("Podsumowania lingwistyczne dla rozegranych gier:");
            SummaryGenerator.generateAllSummariesForGamesPlayed(playerStats);

             */

            System.out.println("Podsumowania lingwistyczne dla wieku:");
            System.out.println(SummaryGenerator.generateSummary(playerStats,"age", Terms.getAgeTerms().get(0),List.of("młody wiek","mało gier rozegranych"),"połowa", Quantifiers.getRelativeQuantifiers().get("połowa") ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}