package org.example.eddday;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.example.eddday.AlertUtils;
import org.example.eddday.FolderManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoSaver {
    private static final String SAVEFOTO = FolderManager.SAVE_FOTO;

    public static boolean saveImage(Image image, String dayFolderName) {
        if (image == null) {
            AlertUtils.showAlert("Нет изображения для сохранения.");
            return false;
        }
        try {
            File saveDir = new File(SAVEFOTO, dayFolderName);
            if (!saveDir.exists()) {
                boolean created = saveDir.mkdirs();
                if (!created) {
                    AlertUtils.showError("Не удалось создать папку для сохранения: " + saveDir.getAbsolutePath());
                    return false;
                }
            }

            byte[] newImageBytes = imageToBytes(image);
            if (newImageBytes == null) {
                AlertUtils.showError("Не удалось получить байты изображения");
                return false;
            }

            File[] existingFiles = saveDir.listFiles();
            if (existingFiles != null) {
                for (File file : existingFiles) {
                    if (file.getName().toLowerCase().endsWith(".png") || file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".jpeg")) {
                        byte[] existingBytes = readFileToBytes(file);
                        if (existingBytes != null && compareByteArrays(existingBytes, newImageBytes)) {
                            AlertUtils.showAlert("Это изображение уже сохранено: " + file.getName());
                            return false;
                        }
                    }
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = "photo_" + timeStamp + ".png";
            File outputFile = new File(saveDir, fileName);
            java.awt.image.BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            boolean success = ImageIO.write(bImage, "png", outputFile);
            if (success) {
                AlertUtils.showAlert("Фото успешно сохранено в папку:\n" + dayFolderName);
                return true;
            } else {
                AlertUtils.showError("Не удалось сохранить изображение.");
                return false;
            }
        } catch (Throwable t) {
            AlertUtils.showError("Ошибка при сохранении файла: " + t.getMessage());
            t.printStackTrace();
            return false;
        }
    }

    public static boolean deleteImageFile(File fileToDelete) {
        if (fileToDelete == null) {
            AlertUtils.showAlert("Файл для удаления не существует.");
            return false;
        }
        try {
            if (fileToDelete.exists() && fileToDelete.isFile()) {
                if (fileToDelete.delete()) {
                    AlertUtils.showAlert("Фото успешно удалено.");
                    return true;
                } else {
                    AlertUtils.showError("Не удалось удалить файл: возможно, нет прав доступа.");
                    return false;
                }
            } else {
                AlertUtils.showAlert("Файл для удаления не найден на диске.");
                return false;
            }
        } catch (Throwable t) {
            AlertUtils.showError("Критическая ошибка при попытке удаления файла: " + t.getMessage());
            t.printStackTrace();
            return false;
        }
    }

    private static byte[] imageToBytes(Image image) {
        try {
            java.awt.image.BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(bImage, "png", baos);
            return baos.toByteArray();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private static byte[] readFileToBytes(File file) {
        try {
            return java.nio.file.Files.readAllBytes(file.toPath());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private static boolean compareByteArrays(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
}