package org.example.eddday;

import java.io.File;

public class FolderManager {
    public static final String TEXT = "texts";

    public static void ensureFolderExists() {
        File dir = new File(TEXT);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                AlertUtils.showAlert("Не удалось создать папку " + TEXT);
            }
        }
    }

    public static void startFolderMonitoring() {
        Thread t = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                File folder = new File(TEXT);
                if (!folder.exists()) folder.mkdirs();
            }
        });
        t.setDaemon(true);
        t.start();
    }
}