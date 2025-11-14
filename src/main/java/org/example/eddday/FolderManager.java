package org.example.eddday;

import java.io.File;

public class FolderManager {
    public static final String TEXT = "texts";
    public static final String SAVE_FOTO = "saved_photos";

    public static void ensureFolderExists() {
        File textDir = new File(TEXT);
        if (!textDir.exists()) {
            if (!textDir.mkdirs()) {
                AlertUtils.showAlert("Не удалось создать папку " + TEXT);
            }
        }
        File photoDir = new File(SAVE_FOTO);
        if (!photoDir.exists()) {
            if (!photoDir.mkdirs()) {
                AlertUtils.showAlert("Не удалось создать папку " + SAVE_FOTO);
            }
        }
    }
    public static void startFolderMonitoring() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    File folder = new File(TEXT);
                    if (!folder.exists()) folder.mkdirs();
                }
            }
        });
    }
}