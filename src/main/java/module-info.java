module org.example.eddday {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires com.google.gson;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens org.example.eddday to javafx.fxml, com.fasterxml.jackson.databind;
    exports org.example.eddday;
}
