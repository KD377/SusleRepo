module org.example.linguistic {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.opencsv;
    requires static lombok;
    requires commons.beanutils;
    requires java.sql;

    opens org.example.linguistic to javafx.fxml;
    exports org.example.linguistic;
    opens org.example.linguistic.data to com.opencsv;
    exports org.example.linguistic.data;
}