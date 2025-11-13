package org.example.eddday;

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

import java.io.IOException;

public class MainApp {
    @FXML
    private TextField DateFile;
    @FXML
    private TabPane tabPane;
    @FXML
    StackPane conect;
    private TabManager tabManager;

    @FXML
    Tab findTabByName(String name) {
        for (int i = 0; i < tabPane.getTabs().size(); i++) {
            Tab tab = tabPane.getTabs().get(i);
            if (tab.getText().equals(name)) {
                return tab;
            }
        }
        return null;
    }

    @FXML
    public void initialize() {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                FolderManager.ensureFolderExists();
                if (tabPane != null) {
                    FolderManager.startFolderMonitoring();
                    tabManager = new TabManager(tabPane);
                    tabManager.loadTabsFromJsonFiles();
                    tabManager.addTodayTabIfNotExists();
                }
            }
        });
    }

    public void deleteTabByName(String name) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                if (tabManager != null) {
                    tabManager.deleteTabByName(name);
                }
            }
        });
    }

    @FXML
    public void deleteTab() {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                Tab selected = tabPane.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    deleteTabByName(selected.getText());
                } else {
                    showAlert("Нет выбранной вкладки");
                }
            }
        });
    }

    @FXML
    public void Save(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                if (tabManager != null) {
                    for (int i = 0; i < tabPane.getTabs().size(); i++) {
                        Tab t = tabPane.getTabs().get(i);
                        tabManager.saveTabToJsonFile(t);
                    }
                }
            }
        });
    }

    @FXML
    public void showOP(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                SceneNavigator.switchScene(event, "OPscene.fxml");
            }
        });
    }

    @FXML
    public void EX(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                System.exit(0);
            }
        });
    }

    public void showTabByName(String name) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                Tab tab = findTabByName(name);
                if (tab != null) {
                    tabPane.getSelectionModel().select(tab);
                } else {
                    showAlert("Вкладка " + name + " не найдена");
                }
            }
        });
    }

    public void showAllTabs() {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                if (tabManager != null) {
                    tabManager.loadTabsFromJsonFiles();
                }
            }
        });
    }

    public void showTabByDate(String date) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                if (Validator.isDateValid(date)) {
                    showTabByName(date);
                }
            }
        });
    }

    @FXML
    public void Conect(MouseEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                String targetTabText = "Название вкладки";
                boolean found = false;
                for (int i = 0; i < tabPane.getTabs().size(); i++) {
                    Tab tab = tabPane.getTabs().get(i);
                    if (tab.getText().equals(targetTabText)) {
                        tabPane.getSelectionModel().select(tab);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    showAlert("Вкладка не найдена");
                }
            }
        });
    }

    @FXML
    public void deleteCurrentTab() {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                Tab selected = tabPane.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    deleteTabByName(selected.getText());
                } else {
                    showAlert("Нет выбранной вкладки ");
                }
            }
        });
    }

    @FXML
    public void AllRecords(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                SceneNavigator.switchScene(event, "hello-view.fxml");
            }
        });
    }

    @FXML
    public void showSearch(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                SceneNavigator.switchScene(event, "IscCon.fxml");
            }
        });
    }

    @FXML
    public void Back(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                SceneNavigator.switchScene(event, "OPscene.fxml");
            }
        });
    }

    private void showAlert(String message) {
        AlertUtils.showAlert(message);
    }

    @FXML
    void Iscat(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                String date = DateFile.getText().trim();
                if (!Validator.isDateValid(date)) {
                    return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                MainApp mainApp = loader.getController();
                mainApp.showTabByDate(date);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            }
        });
    }

    public void addTab(ActionEvent event) {
    }

    private void executeWithErrorHandling(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            showAlert("Произошла ошибка " + e.getMessage());
            e.printStackTrace();
        }
    }
}