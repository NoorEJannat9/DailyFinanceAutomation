package pages.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class ConfigReader {

    private static final Properties fileProps = new Properties();

    static {
        try {
            String path = System.getProperty("user.dir")
                    + "/src/test/resources/config.properties";
            try (FileInputStream fis = new FileInputStream(path)) {
                fileProps.load(fis);
            }
        } catch (IOException e) {
            System.err.println("[ConfigReader] WARNING: config.properties not found – "
                    + "will rely entirely on -D system properties. (" + e.getMessage() + ")");
        }
    }

    public static String get(String key) {
        // System property (-D flag) takes priority → secure CLI injection
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }
        // Fall back to file
        String fileProp = fileProps.getProperty(key);
        return (fileProp != null) ? fileProp.trim() : null;
    }
}