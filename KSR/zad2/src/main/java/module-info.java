module org.example.zad2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires static lombok;
    requires com.opencsv;

    opens org.example.zad2 to javafx.fxml;
    exports org.example.zad2;
}