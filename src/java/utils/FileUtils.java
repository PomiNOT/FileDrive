package utils;

import jakarta.servlet.ServletContext;
import java.io.File;

public class FileUtils {
    public static String getFilePath(ServletContext ctx, String uuid) {
        String defaultPath = ctx.getInitParameter("storagePath");
        String fileLocation = defaultPath + File.separator + uuid;

        return fileLocation;
    }
}
