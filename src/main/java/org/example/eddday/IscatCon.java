package org.example.eddday;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;
import java.time.LocalDate;

public class IscatCon {

    @FXML
    private TextField DateFile;

    @FXML
    void Back(ActionEvent event) {
        try {
              Parent newView = FXMLLoader.load(getClass().getResource("OPscene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
              stage.setScene(new Scene(newView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Iscat(ActionEvent event) {
        String date = DateFile.getText().trim();
          if (!isDateValid(date)) {
            return;
        }
        try {
              FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
                     HelloController helloController = loader.getController();
            helloController.showTabByDate(date);
             Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
              stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDateValid(String dateStr) {
        try {
              LocalDate.parse(dateStr);
            return true;
        } catch (Exception e) {
            showAlert("Используйте ГГГГ-ММ-ДД.");
            return false;
        }
    }

    private void showAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
          alert.setHeaderText(null);
        alert.setContentText(message);
          alert.showAndWait();
    }

    @FXML
    void initialize() {
    }
}