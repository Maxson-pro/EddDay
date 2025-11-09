package org.example.eddday;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class TabManager {
    private final ObjectMapper mapper = new ObjectMapper();
    private final javafx.scene.control.TabPane tabPane;

    public TabManager(javafx.scene.control.TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void loadTabsFromJsonFiles() {
        File folder = new File(FolderManager.TEXT);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;
        for (File f : files) {
            try {
                TabData data = mapper.readValue(f, TabData.class);
                if (findTabByName(data.name) == null) {
                    tabPane.getTabs().add(createTab(data.name, data.content));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTodayTabIfNotExists() {
        String today = LocalDate.now().toString();
        if (findTabByName(today) == null) {
            tabPane.getTabs().add(createTab(today, ""));
        }
    }

    public Tab createTab(String name, String content) {
        TextArea ta = new TextArea(content);
        ta.textProperty().addListener((obs, old, neu) -> {
            Tab t = findTabByName(name);
            if (t != null) saveTabToJsonFile(t);
        });
        return new Tab(name, ta);
    }

    public Tab findTabByName(String name) {
        for (Tab t : tabPane.getTabs()) {
            if (t.getText().equals(name)) return t;
        }
        return null;
    }

    public void saveTabToJsonFile(Tab tab) {
        String filename = FolderManager.TEXT + "/" + tab.getText() + ".json";
        TextArea ta = (TextArea) tab.getContent();
        TabData data = new TabData(tab.getText(), ta.getText());
        try {
            mapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTabByName(String name) {
        Tab tab = findTabByName(name);
        if (tab != null) {
            tabPane.getTabs().remove(tab);
            File file = new File(FolderManager.TEXT + "/" + name + ".json");
            if (file.exists()) file.delete();
        }
    }

    public static class TabData {
        public String name;
        public String content;

        public TabData() {}

        public TabData(String name, String content) {
            this.name = name;
            this.content = content;
        }
    }
}