package net.spotv.smartalarm.util;

public class LocalUtil {
    public static String getFileNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return fileName; // 확장자가 없는 경우
        }
        return fileName.substring(0, dotIndex);
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return ""; // 확장자가 없는 경우
        }
        return fileName.substring(dotIndex + 1);
    }

}
