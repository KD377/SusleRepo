package org.example.linguistic.data;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerStats {

    @CsvBindByName(column = "Rk")
    private int id;

    @CsvBindByName(column = "Player")
    private String name;

    @CsvBindByName(column = "Pos")
    private String position;

    @CsvBindByName(column = "Age")
    private int age;

    @CsvBindByName(column = "Tm")
    private String team;

    @CsvBindByName(column = "G")
    private int gamesPlayed;

    @CsvBindByName(column = "3P")
    private double threePointersMade;

    @CsvBindByName(column = "3PA")
    private double threePointerAttempts;

    @CsvBindByName(column = "2P")
    private double twoPointersMade;

    @CsvBindByName(column = "2PA")
    private double twoPointerAttempts;

    @CsvBindByName(column = "AST")
    private double assists;

    @CsvBindByName(column = "STL")
    private double steals;

    @CsvBindByName(column = "BLK")
    private double blocks;

    @CsvBindByName(column = "TOV")
    private double turnovers;


}
