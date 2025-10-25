package org.example.eddday;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloOP {
    @FXML
    private StackPane conect;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void AllRecords(ActionEvent event) {
        try {
                 Parent newView = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(newView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void EX(ActionEvent event) {
        System.exit(0);

    }

    @FXML
    void Conect(MouseEvent event) {

    }

    @FXML
    void showSearch(ActionEvent event) {
        try {
                Parent newView = FXMLLoader.load(getClass().getResource("IscCon.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(newView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void initialize() {

    }
    public void Del() {
         conect.getChildren().clear();
    }

}
