package uz.mvp.configs;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Doston Bokhodirov, Sun 8:31 PM. 12/19/2021
 */
public class PConfig {
    private static Properties p;

    static {
        try (FileReader fileReader = new FileReader("src/main/resources/application.properties")) {
            p = new Properties();
            p.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return p.getProperty(key);
    }
}
