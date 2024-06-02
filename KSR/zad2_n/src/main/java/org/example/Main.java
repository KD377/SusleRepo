package org.example;

import org.example.data.CsvReader;
import org.example.data.PlayerStats;
import org.example.generator.LinguisticSummary;
import org.example.generator.SummaryGenerator;


import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");


            //System.out.println(SummaryGenerator.generateSummary(playerStats, List.of("młody wiek"), "około 5000"));


            //System.out.println(SummaryGenerator.generateSummary(playerStats, List.of("młody wiek"), "połowa", "bardzo mało gier rozegranych"));


//            System.out.println(SummaryGenerator.generateMultiSubjectSummary(
//                    playerStats, List.of("młody wiek"), "większość", "PF", "SG"
//            ));
            LinguisticSummary summary1 = SummaryGenerator.generateMultiSubjectSummaryFirstForm(playerStats, List.of("dużo 3P"), "większość", "SG", "PF");
            System.out.println(summary1);

            LinguisticSummary summary2 = SummaryGenerator.generateMultiSubjectSummarySecondForm(playerStats, List.of("średni wiek"), "większość", "mało gier rozegranych", "PF", "SG");
            System.out.println(summary2);

            LinguisticSummary summaryThirdForm = SummaryGenerator.generateMultiSubjectSummaryThirdForm(playerStats, List.of("mało 3P"), "większość", "PF", "SG", "młody wiek");
            System.out.println(summaryThirdForm);

            // Przykładowe wywołanie dla czwartej formy
            LinguisticSummary summaryFourthForm = SummaryGenerator.generateMultiSubjectSummaryFourthForm(playerStats, List.of("średni wiek"), "PF", "SG");
            System.out.println(summaryFourthForm);


//            List<String> summarizersList = List.of(
//                    "młody wiek", "średni wiek", "stary wiek",
//                    "bardzo mało gier rozegranych", "mało gier rozegranych",
//                    "średnio gier rozegranych", "dużo gier rozegranych", "bardzo dużo gier rozgeranych",
//                    "bardzo mało 3P", "mało 3P", "średnio 3P", "dużo 3P", "bardzo dużo 3P",
//                    "bardzo mało 3PA", "mało 3PA", "średnio 3PA", "dużo 3PA", "bardzo dużo 3PA",
//                    "bardzo mało 2P", "mało 2P", "średnio 2P", "dużo 2P", "bardzo dużo 2P",
//                    "bardzo mało 2PA", "mało 2PA", "średnio 2PA", "dużo 2PA", "bardzo dużo 2PA",
//                    "bardzo mało AST", "mało AST", "średnio AST", "dużo AST", "bardzo dużo AST",
//                    "mało przechwytów", "średnio przechwytów", "dużo przechwytów",
//                    "mało bloków", "średnio bloków", "dużo bloków",
//                    "mało strat", "średnio strat", "dużo strat"
//            );
//
//            List<String> quantifiersList = List.of(
//                    "duża mniejszość", "mniejszość", "połowa", "większość", "duża większość",
//                    "poniżej 3000", "około 5000", "około 9000", "powyżej 11000"
//            );
//
//            List<String> qualifiersList = List.of(
//                    "młody wiek", "średni wiek", "stary wiek",
//                    "bardzo mało gier rozegranych", "mało gier rozegranych",
//                    "średnio gier rozegranych", "dużo gier rozegranych", "bardzo dużo gier rozgeranych",
//                    "bardzo mało 3P", "mało 3P", "średnio 3P", "dużo 3P", "bardzo dużo 3P",
//                    "bardzo mało 3PA", "mało 3PA", "średnio 3PA", "dużo 3PA", "bardzo dużo 3PA",
//                    "bardzo mało 2P", "mało 2P", "średnio 2P", "dużo 2P", "bardzo dużo 2P",
//                    "bardzo mało 2PA", "mało 2PA", "średnio 2PA", "dużo 2PA", "bardzo dużo 2PA",
//                    "bardzo mało AST", "mało AST", "średnio AST", "dużo AST", "bardzo dużo AST",
//                    "mało przechwytów", "średnio przechwytów", "dużo przechwytów",
//                    "mało bloków", "średnio bloków", "dużo bloków",
//                    "mało strat", "średnio strat", "dużo strat"
//            );
//
//            for (String summarizer : summarizersList) {
//                for (String quantifier : quantifiersList) {
//                    // Generate summaries without a qualifier (first type).
//                    System.out.println(SummaryGenerator.generateSummary(playerStats, List.of(summarizer), quantifier));
//
//
////                    for (String qualifier : qualifiersList) {
////                        // Generate summaries with a qualifier (second type).
////                        if (!summarizer.equals(qualifier)) { // Ensure the summarizer and qualifier are not the same.
////                            System.out.println(SummaryGenerator.generateSummary(playerStats, List.of(summarizer), quantifier, qualifier));
////                        }
////                    }
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}