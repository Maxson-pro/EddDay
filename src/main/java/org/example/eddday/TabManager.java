package org.example.eddday;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TabManager {
    private TabPane tabPane;
    private static final String JsonFolderPath = "texts";

    public TabManager(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void loadTabsFromJsonFiles() {
        try {
            File folder = new File(JsonFolderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File[] files = folder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    if (file.getName().endsWith(".json")) {
                        loadTabFromJsonFile(file);
                    }
                }
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при загрузке вкладок: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadTabFromJsonFile(File file) {
        if (file.length() == 0) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            TabData tabData = mapper.readValue(file, TabData.class);
            createTabFromData(tabData);
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при чтении файла " + file.getName() + ": " + e.getMessage());
        }
    }

    public void saveTabToJsonFile(Tab tab) {
        try {
            String tabName = tab.getText();
            String filename = JsonFolderPath + File.separator + tabName + ".json";
            ObjectMapper mapper = new ObjectMapper();
            String content = "";
            if (tab.getContent() instanceof TextArea) {
                content = ((TextArea) tab.getContent()).getText();
            }
            TabData data = new TabData(tabName, content);
            mapper.writeValue(new File(filename), data);
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при сохранении вкладки " + tab.getText() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteTabByName(String name) {
        try {
            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                Tab tab = tabPane.getTabs().get(i);
                if (tab.getText().equals(name)) {
                    tabPane.getTabs().remove(i);
                    deleteJsonFile(name);
                    break;
                }
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при удалении вкладки " + name + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addTodayTabIfNotExists() {
        try {
            String today = java.time.LocalDate.now().toString();
            boolean exists = false;
            for (int i = 0; i < tabPane.getTabs().size(); i++) {
                if (tabPane.getTabs().get(i).getText().equals(today)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                Tab newTab = new Tab();
                newTab.setText(today);
                TextArea contentArea = new TextArea();
                newTab.setContent(contentArea);
                tabPane.getTabs().add(newTab);
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при добавлении вкладки за сегодня: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteJsonFile(String tabName) {
        try {
            String filename = JsonFolderPath + File.separator + tabName + ".json";
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при удалении файла вкладки " + tabName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void createTabFromData(TabData data) {
        try {
            Tab newTab = new Tab();
            newTab.setText(data.name);
            TextArea contentArea = new TextArea(data.content);
            newTab.setContent(contentArea);
            tabPane.getTabs().add(newTab);
        } catch (Exception e) {
            AlertUtils.showAlert("Ошибка при создании вкладки из данных: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static class TabData {
        public String name;
        public String content;

        public TabData() {
        }

        public TabData(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }
}