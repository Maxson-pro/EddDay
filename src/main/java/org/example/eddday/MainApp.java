package org.example.eddday;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.time.LocalDate;

import static org.example.eddday.Validator.isDateValid;

public class MainApp {
    @FXML
    private TextField DateFile;

    @FXML
    private TabPane tabPane;

    @FXML
    private StackPane conect;

    private TabManager tabManager;
    public Tab findTabByName(String name) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(name)) {
                return tab;
            }
        }
        return null;
    }

    @FXML
    public void initialize() {
        FolderManager.ensureFolderExists();
        if (tabPane != null) {
            FolderManager.startFolderMonitoring();
            tabManager = new TabManager(tabPane);
            tabManager.loadTabsFromJsonFiles();
            tabManager.addTodayTabIfNotExists();
        } else {
            System.out.println("tabPane не инициализировано. Проверьте ваш FXML и привязку fx:id.");
        }
    }

    public void deleteTabByName(String name) {
        if (tabManager != null) {
            tabManager.deleteTabByName(name);
        }
    }

    @FXML
    public void deleteTab() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected != null) deleteTabByName(selected.getText());
        else showAlert("Нет выбранной вкладки");
    }

    @FXML
    public void Save(ActionEvent event) {
        if (tabManager != null) {
            for (Tab t : tabPane.getTabs()) {
                tabManager.saveTabToJsonFile(t);
            }
        }
    }

    @FXML
    public void showOP(ActionEvent event) {
        SceneNavigator.switchScene(event, "OPscene.fxml");
    }

    @FXML
    public void EX(ActionEvent event) {
        System.exit(0);
    }

    public void showTabByName(String name) {
        Tab tab = findTabByName(name);
        if (tab != null) {
            tabPane.getSelectionModel().select(tab);
        } else {
            showAlert("Вкладка " + name + " не найдена");
        }
    }

    public void showAllTabs() {
        if (tabManager != null) {
            tabManager.loadTabsFromJsonFiles();
        }
    }

    public void showTabByDate(String date) {
        if (isDateValid(date)) {
            showTabByName(date);
        }
    }

    @FXML
    public void Conect(MouseEvent event) {
        String targetTabText = "Название вкладки";
        boolean found = false;
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(targetTabText)) {
                tabPane.getSelectionModel().select(tab);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Вкладка не найдена");
        }
    }

    @FXML
    public void deleteCurrentTab() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected != null) deleteTabByName(selected.getText());
        else showAlert("Нет выбранной вкладки");
    }

    @FXML
    public void AllRecords(ActionEvent event) {
        SceneNavigator.switchScene(event, "hello-view.fxml");
    }

    @FXML
    public void showSearch(ActionEvent event) {
        SceneNavigator.switchScene(event, "IscCon.fxml");
    }

    @FXML
    public void Back(ActionEvent event) {
        SceneNavigator.switchScene(event, "OPscene.fxml");
    }

    private void showAlert(String message) {
        AlertUtils.showAlert(message);
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
            MainApp mainApp = loader.getController();
            mainApp.showTabByDate(date);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void addTab(ActionEvent event) {
    }

    public static class TabData {
        @JsonProperty
        public String name;
        @JsonProperty
        public String content;

        @JsonCreator
        public TabData(@JsonProperty("name") String name, @JsonProperty("content") String content) {
            this.name = name;
            this.content = content;
        }
    }
}