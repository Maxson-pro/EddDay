package org.example.eddday;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.File;
import java.io.IOException;

public class MainApp {
    private ImageCollection imageCollection = new ImageCollection();
    private int currentIndex = -1;

    @FXML
    private ImageView Open;
    @FXML
    public TextField DateFile;
    @FXML
    private TabPane tabPane;
    @FXML
    StackPane conect;

    private TabManager tabManager;

    public MainApp() {
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

    private String getCurrentDayFolderName() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            return selectedTab.getText();
        }
        return java.time.LocalDate.now().toString();
    }

    private void loadSavedPhoto() {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                imageCollection.clear();
                currentIndex = -1;
                String dayFolderName = getCurrentDayFolderName();
                File saveDir = new File(FolderManager.SAVE_FOTO, dayFolderName);
                if (!saveDir.exists() || !saveDir.isDirectory()) {
                    Open.setImage(null);
                    return;
                }

                File[] files = saveDir.listFiles();
                if (files == null || files.length == 0) {
                    Open.setImage(null);
                    return;
                }

                for (File file : files) {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg")) {
                        Image image = new Image(file.toURI().toString());
                        if (!image.isError()) {
                            imageCollection.add(image, file);
                        }
                    }
                }

                if (imageCollection.getSize() > 0) {
                    currentIndex = imageCollection.getLastIndex();
                    Open.setImage(imageCollection.get(currentIndex));
                } else {
                    Open.setImage(null);
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
                    for (Tab t : tabPane.getTabs()) {
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
    public Tab findTabByName(String name) {
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getText().equals(name)) {
                return tab;
            }
        }
        return null;
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
                for (Tab tab : tabPane.getTabs()) {
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
                    tabManager.deleteTabByName(selected.getText());
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
        } catch (Throwable t) {
            showAlert("Произошла ошибка: " + t.getMessage());
            t.printStackTrace();
        }
    }

    public void photoDay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Photo.fxml"));
            Parent root = loader.load();
            MainApp photoController = loader.getController();
            photoController.setTabPane(this.tabPane);
            photoController.loadSavedPhoto();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            executeWithErrorHandling(new Runnable() {
                public void run() {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void setTabPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void addPH(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите фото");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageCollection.add(image, null);
                currentIndex = imageCollection.getLastIndex();
                Open.setImage(image);
            } catch (Exception e) {
                AlertUtils.showError("Не удалось загрузить изображение: " + e.getMessage());
            }
        }
    }

    public void loadImagesFromFiles(File[] files) {
        imageCollection.clear();
        for (File file : files) {
            Image img = new Image(file.toURI().toString());
            imageCollection.add(img, file);
        }
        if (imageCollection.getSize() > 0) {
            currentIndex = 0;
            Open.setImage(imageCollection.get(currentIndex));
        }
    }

    public void ForwardPhoto(ActionEvent event) {
        if (imageCollection.getSize() == 0) return;
        currentIndex++;
        if (currentIndex >= imageCollection.getSize()) {
            currentIndex = 0;
        }
        Open.setImage(imageCollection.get(currentIndex));
    }

    public void BackPhoto(ActionEvent event) {
        if (imageCollection.getSize() == 0) return;
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = imageCollection.getSize() - 1;
        }
        Open.setImage(imageCollection.get(currentIndex));
    }

    public void PhotoOpen(MouseEvent event) {
    }

    public void ClosePhoto(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                SceneNavigator.switchScene(event, "hello-view.fxml");
            }
        });
    }

    @FXML
    public void SavePhoto(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                Image imageToSave = Open.getImage();
                String dayFolderName = getCurrentDayFolderName();
                if (PhotoSaver.saveImage(imageToSave, dayFolderName)) {
                    loadSavedPhoto();
                }
            }
        });
    }

    public void DeleteFoto(ActionEvent event) {
        executeWithErrorHandling(new Runnable() {
            public void run() {
                if (imageCollection.getSize() == 0 || currentIndex == -1) {
                    return;
                }

                ImageEntry entryToDelete = imageCollection.getEntry(currentIndex);

                if (entryToDelete != null && PhotoSaver.deleteImageFile(entryToDelete.file)) {

                    int indexToRemove = currentIndex;

                    if (indexToRemove >= 0 && indexToRemove < imageCollection.size) {

                        for (int i = indexToRemove; i < imageCollection.size - 1; i++) {
                            imageCollection.entries[i] = imageCollection.entries[i + 1];
                        }

                        imageCollection.size--;
                        imageCollection.entries[imageCollection.size] = null;

                        if (imageCollection.getSize() == 0) {
                            currentIndex = -1;
                            Open.setImage(null);
                        } else {
                            if (currentIndex >= imageCollection.getSize()) {
                                currentIndex = imageCollection.getSize() - 1;
                            }
                            Open.setImage(imageCollection.get(currentIndex));
                        }
                    }
                }
            }
        });
    }

    public class ImageEntry {
        public Image image;
        public File file;

        public ImageEntry(Image image, File file) {
            this.image = image;
            this.file = file;
        }
    }

    public class ImageCollection {
        private ImageEntry[] entries;
        private int size;
        private int capacity;

        public ImageCollection() {
            this.capacity = 10;
            this.entries = new ImageEntry[this.capacity];
            this.size = 0;
        }

        public void add(Image image, File file) {
            if (size == capacity) {
                capacity *= 2;
                ImageEntry[] newEntries = new ImageEntry[capacity];
                System.arraycopy(entries, 0, newEntries, 0, size);
                entries = newEntries;
            }
            entries[size] = new ImageEntry(image, file);
            size++;
        }

        public void clear() {
            entries = new ImageEntry[capacity];
            size = 0;
        }

        public Image get(int index) {
            if (index >= 0 && index < size) {
                return entries[index].image;
            }
            return null;
        }

        public ImageEntry getEntry(int index) {
            if (index >= 0 && index < size) {
                return entries[index];
            }
            return null;
        }

        public int getSize() {
            return size;
        }

        public int getLastIndex() {
            return size - 1;
        }
    }
}