package org.example.zad2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.zad2.data.CsvReader;
import org.example.zad2.data.PlayerStats;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        //launch();
        CsvReader csvReader = new CsvReader();
        try {
            List<PlayerStats> playerStats = csvReader.readCsv("src/main/resources/NBA_Player_Stats.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}