package utils;

import jakarta.servlet.ServletContext;
import java.io.File;

public class FileUtils {
    public static String getFilePath(String basePath, String uuid) {
        String fileLocation = basePath + File.separator + uuid;
        return fileLocation;
    }
}
