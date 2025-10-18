package org.example.eddday;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

import java.io.*;

public class HelloController {

    @FXML private TabPane tabPane;
    private static final String tabsFol = "tabs/";

    @FXML
    void initialize() {
        new File(tabsFol).mkdir();
        loadTabs();
         if (tabPane.getScene() != null && tabPane.getScene().getWindow() != null) {
             tabPane.getScene().getWindow().setOnCloseRequest(event -> saveTabs());
         }
    }
     @FXML
    void addTab(ActionEvent event) {
         String today = java.time.LocalDate.now().toString();
        String lastDate = "";
         File dateFile = new File("date.txt");
        if (dateFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(dateFile))) {
                lastDate = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
         }
        if  (today.equals(lastDate)) {
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
              alert.setHeaderText(null);
            alert.setContentText("Вкладку можно создать только один раз в день");
             alert.showAndWait();
             return;
        }
        Tab newTab = createTab("Вкладка " + (tabPane.getTabs().size() + 1), "");
        tabPane.getTabs().add(newTab);
        tabPane.getSelectionModel().select(newTab);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("date.txt"))) {
            writer.write(today);
        } catch (IOException e) {
            e.printStackTrace();
        }
        saveTabs();
    }
    @FXML
    void deleteTab(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            deleteTabFile(selectedTab.getText());
            tabPane.getTabs().remove(selectedTab);
            saveTabs();
        }
    }
    @FXML
    void Save(ActionEvent event) {
         saveTabs();
    }
    @FXML
    void EX(ActionEvent event) {
        System.exit(0);
    }
    private void saveTabs() {
        for    (Tab tab : tabPane.getTabs()) {
            saveTabToFile(tab);
           }
    }
    private void saveTabToFile(Tab tab) {
           String filename = tabsFol + tab.getText() + ".txt";
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            TextArea ta = (TextArea) tab.getContent();
               String content = ta.getText().replace("\n", "\\n");
            writer.write(content);
        }catch (IOException e) {
             e.printStackTrace();
        }
    }
    private void loadTabs() {
        File folder = new File(tabsFol);
          File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) return;
        for (File file : files) {
                String filename = file.getName();
            String title = filename.substring(0, filename.length() - 4);
                 String content = "";

             try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                   StringBuilder sb = new StringBuilder();
                String line;
                    while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\\n");
                }
                content = sb.toString();
            }catch (IOException e) {
                  e.printStackTrace();
            }
            content = content.replace("\\n", "\n");
            Tab tab = createTab(title, content);
                 tabPane.getTabs().add(tab);
        }
    }
    private void deleteTabFile(String tabTitle) {
             String filename = tabsFol + tabTitle + ".txt";
        File file = new File(filename);
        if (file.exists()) {
              file.delete();
        }
    }
    private Tab createTab(String title, String content) {
            Tab tab = new Tab(title);
        TextArea ta = new TextArea(content);
            tab.setContent(ta);
        return tab;
    }
}