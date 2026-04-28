package pages.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties props = new Properties();

    static {
        try {
            String path = System.getProperty("user.dir")
                    + "/src/test/resources/config.properties";
            FileInputStream fis = new FileInputStream(path);
            props.load(fis);
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found! " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}