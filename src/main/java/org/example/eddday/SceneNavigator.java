package org.example.eddday;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class SceneNavigator {
    public static void switchScene(ActionEvent event, String fxmlFile) {
        try {
            Parent newView = FXMLLoader.load(SceneNavigator.class.getResource(fxmlFile));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(newView));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showAlert("Не удалось загрузить сцену: " + fxmlFile);
            e.printStackTrace();
        }
    }
}