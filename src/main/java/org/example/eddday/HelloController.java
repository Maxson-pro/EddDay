package org.example.eddday;

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
import java.time.LocalDate;

public class HelloController {

    @FXML
    private TabPane tabPane;
    boolean isSearching = false;

    String folder = "tabs/";

    public void initialize() {
          createFolder();
        loadTabs();
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
        if (isSearching) {
            showAlert("Нельзя добавлять вкладки во время поиска");
            return;
        }

         String date = askUserForDate();
        if (date == null || !checkDateFormat(date))
            return;
          if (findTabByName(date) != null) {
            showAlert("Эта дата уже существует");
            return;
        }
        Tab newTab = createTab(date, "");
            tabPane.getTabs().add(newTab);
        saveAllTabs();
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
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            saveTab(i);
        }
    }

    public void saveTab(int index) {
        Tab tab = tabPane.getTabs().get(index);
          String filename = folder + tab.getText() + ".txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
               TextArea textArea = (TextArea) tab.getContent();
            writer.write(textArea.getText());
              writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTabs() {
        File folderFile = new File(folder);
           if (!folderFile.exists()) folderFile.mkdir();
        File[] files = folderFile.listFiles();
           if (files == null) return;
        for (File file : files) {
               if (file.getName().endsWith(".txt")) {
                String name = file.getName().replace(".txt", "");
                String content = readFile(file);
                   Tab tab = createTab(name, content);
                tabPane.getTabs().add(tab);
            }
        }
    }

    public String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
               String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                   sb.append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
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
