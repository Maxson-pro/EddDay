module org.example.eddday {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens org.example.eddday to javafx.fxml;
    exports org.example.eddday;
}