package org.example.eddday;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TabPane tabPane;

    private final ObjectMapper mapper = new ObjectMapper();

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

    public void initialize() {
        createFolderIfNotExists("tabs");
        loadTabsFromFile("tabs/data.json");
        addTodayTabIfNotExists();
    }

    private void addTodayTabIfNotExists() {
        String today = java.time.LocalDate.now().toString();
        if (findTabByName(today) == null) {
            tabPane.getTabs().add(createTab(today, ""));
            saveTabs();
        }
    }

    private void createFolderIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
    }

    private Tab findTabByName(String name) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(name)) return tab;
        }
        return null;
    }

    private Tab createTab(String name, String content) {
        TextArea ta = new TextArea(content);
        return new Tab(name, ta);
    }

    private void loadTabsFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            try {
                mapper.writeValue(file, new ArrayList<TabData>());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            List<TabData> data =
                    mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, TabData.class));
            tabPane.getTabs().clear();
            for (TabData tabData : data) {
                if (findTabByName(tabData.name) == null) {
                    tabPane.getTabs().add(createTab(tabData.name, tabData.content));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTabs() {
        List<TabData> allTabs = new ArrayList<>();
        for (Tab tab : tabPane.getTabs()) {
            TextArea ta = (TextArea) tab.getContent();
            allTabs.add(new TabData(tab.getText(), ta.getText()));
        }
        try {
            mapper.writeValue(new File("tabs/data.json"), allTabs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Conect(MouseEvent mouseEvent) {
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
    public void addTab(ActionEvent event) {
        String tabName = "Новая вкладка " + (tabPane.getTabs().size() + 1);
        tabPane.getTabs().add(createTab(tabName, ""));
        saveTabs();
    }

    public void deleteTab() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String name = selected.getText();
            deleteTabByName(name);
        } else {
            showAlert("Нет выбранной вкладки");
        }
    }

    public void deleteTabByName(String name) {
        Tab tabToRemove = findTabByName(name);
        if (tabToRemove != null) {
            tabPane.getTabs().remove(tabToRemove);
            saveTabs();
        } else {
            showAlert("Вкладка " + name + " не найдена");
        }
    }
    public void Save(ActionEvent event) {
        saveTabs();
    }
    public void showOP(ActionEvent event) {
        try {
            Parent scene = FXMLLoader.load(getClass().getResource("OPscene.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
        loadTabsFromFile("tabs/data.json");
    }
    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void showTabByDate(String date) {
        Tab tab = findTabByName(date);
        if (tab != null) {
            tabPane.getSelectionModel().select(tab);
        } else {
            showAlert("День " + date + " не найден");
        }
    }
    public void deleteCurrentTab() {
        Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String name = selected.getText();
            deleteTabByName(name);
        } else {
            showAlert("Нет выбранной вкладки");
        }
    }
}