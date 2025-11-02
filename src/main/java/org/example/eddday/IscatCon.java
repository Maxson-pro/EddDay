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
        if (dateStr.length() != 10 || dateStr.charAt(4) != '-' || dateStr.charAt(7) != '-') {
            showAlert("Используете формат ГГГГ-MM-ДД c тире и цифрами!!!!");
            return false;
        }
            String[] par = dateStr.split("-");
        int year, month, day;
        try {
            year = Integer.parseInt(par[0]);
            month = Integer.parseInt(par[1]);
            day = Integer.parseInt(par[2]);
        }catch (NumberFormatException e) {
            showAlert("вводите только цифрами");
            return false;
        }
        if (month <1 || month >12) {
            showAlert("Месяцы от 1 до 12");
            return false;
        }
        int maxDay = getMAXIM_Day(year, month);
        if (day < 1 || day > maxDay) {
            showAlert("Некорректное число дней. В этом месяце " + maxDay + " дней.");
            return false;
        }

        return true;
    }
        private int getMAXIM_Day(int year, int month) {
            switch (month) {
                case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                    return 31;
                case 4: case 6: case 9: case 11:
                    return 30;
                case 2:
                    return (isLeapYear(year)) ? 29 : 28;
                default:
                    return 0;
            }
        }

        private boolean isLeapYear(int year) {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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