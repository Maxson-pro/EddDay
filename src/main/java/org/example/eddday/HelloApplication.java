package org.example.eddday;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("OPscene.fxml"));
        Parent root = loader.load();
        stage.initStyle(StageStyle.UNDECORATED);
        Scene scene = new Scene(root, 619, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}