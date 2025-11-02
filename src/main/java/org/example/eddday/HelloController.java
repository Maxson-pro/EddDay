package org.example.eddday;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TabPane tabPane;
    boolean isSearching = false;

    String folder = "tabs/";
    private final String jsonFilePath = "tabs/data.json";
    private final ObjectMapper mapper = new ObjectMapper();

    public static class TabData {
        public String name;
         public String content;

        public TabData() {}

        public TabData(String name, String content) {
              this.name = name;
            this.content = content;
        }
    }

    public void initialize() {
        createFolder();
           loadTabs();
           createTodayTabIfNot();
    }

    public void createFolder() {
        File folderFile = new File(folder);
          if (!folderFile.exists()) {
            folderFile.mkdir();
        }
    }

    public Tab findTabByName(String name) {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
               Tab tab = tabPane.getTabs().get(i);
              if (tab.getText().equals(name)) {
                return tab;
            }
        }
        return null;
    }

    public Tab createTab(String name, String content) {
         TextArea textArea = new TextArea(content);
          Tab tab = new Tab();
        tab.setText(name);
           tab.setContent(textArea);
        return tab;
    }

    public void addTab() {
//        if (isSearching) {
//               showAlert("Нельзя добавлять вкладки во время поиска");
//            return;
//        }
//
//           String date = askUserForDate();
//        if (date == null || !checkDateFormat(date))
//            return;
//        if (findTabByName(date) != null) {
//            showAlert("Эта дата уже существует");
//            return;
//        }
//        createFolderAndJsonForDay(date);
//        Tab newTab = createTab(date, "");
//           tabPane.getTabs().add(newTab);
//        saveAllTabs();
   }
   public void createTodayTabIfNot() {
        String today = LocalDate.now().toString();
        if (findTabByName(today) == null) {
            Tab todayTab = createTab(today, "");
            tabPane.getTabs().add(todayTab);
        }
   }
    private void createFolderAndJsonForDay(String date) {
        String baseFolder = "tabs_days";
        File mainFolder = new File(baseFolder);
        if (!mainFolder.exists()) {
            mainFolder.mkdir();
        }

        String dayFolderPath = baseFolder + "/" + date;
        File dayFolder = new File(dayFolderPath);
        if (!dayFolder.exists()) {
            dayFolder.mkdir();
        }
        File jsonFile = new File(dayFolderPath + "/tabs.json");
        if (!jsonFile.exists()) {
            try {
                jsonFile.createNewFile();
                mapper.writeValue(jsonFile, new Object());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String askUserForDate() {
        TextInputDialog dialog = new TextInputDialog();
          dialog.setTitle("Введите дату");
        dialog.setHeaderText(null);
           dialog.setContentText("Введите дату (ГГГГ-ММ-ДД):");
        return dialog.showAndWait().orElse(null);
    }

    public boolean checkDateFormat(String date) {
        try {
            LocalDate.parse(date);
              return true;
        } catch (Exception e) {
              showAlert("Некорректный формат даты");
            return false;
        }
    }

      public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Предупреждение");
        alert.setHeaderText(null);
           alert.setContentText(message);
        alert.showAndWait();
    }

    public void saveAllTabs() {
        List<TabData> tabsData = new ArrayList<>();
        for (Tab tab : tabPane.getTabs()) {
            String name = tab.getText();
            TextArea ta = (TextArea) tab.getContent();
            String content = ta.getText();
            tabsData.add(new TabData(name, content));
        }
        try {
            mapper.writeValue(new File(jsonFilePath), tabsData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadTabs() {
        File jsonFile = new File(jsonFilePath);
        if (!jsonFile.exists()) {
            return;
        }
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            if (rootNode.isArray()) {
                tabPane.getTabs().clear();
                for (JsonNode node : rootNode) {
                    String name = node.get("name").asText();
                    String content = node.get("content").asText();
                    Tab tab = createTab(name, content);
                    tabPane.getTabs().add(tab);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteTab() {
           Tab selected = tabPane.getSelectionModel().getSelectedItem();
        if (selected != null) {
               deleteFile(selected.getText());
            tabPane.getTabs().remove(selected);
               saveAllTabs();
        }
    }

    public void deleteFile(String name) {
        String filename = folder + name + ".txt";
           File file = new File(filename);
          if (file.exists()) {
            file.delete();
        }
    }

    public void showOP(ActionEvent event) {
        try {
               Parent newScene = FXMLLoader.load(getClass().getResource("OPscene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(newScene));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EX(ActionEvent event) {
        System.exit(0);
    }

    public void Save(ActionEvent event) {
        saveAllTabs();
    }

    public void showTabByDate(String date) {
        Tab tab = findTabByName(date);
         if (tab != null) {
            tabPane.getTabs().clear();
             tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().select(tab);
        } else {
            showAlert("Вкладка с этой датой не найдена");
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

           public void startSearch() {
        isSearching = true;
    }

    public void endSearch() {
        isSearching = false;
    }
}