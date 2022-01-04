package uz.mvp.configs;

import org.glassfish.grizzly.http.server.Session;
import uz.mvp.enums.Language;
import uz.mvp.repository.authuser.AuthUserRepository;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Doston Bokhodirov, Sat 10:28 AM. 12/25/2021
 */
public class LangConfig {
    public static Properties uz;
    public static Properties ru;
    public static Properties en;
    public static String pathPre = "src/main/resources/i18n/";

    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    static {load();}
    private static void load() {
        try (FileReader uzFileReader = new FileReader(pathPre + "uz.properties");
             FileReader ruFileReader = new FileReader(pathPre + "ru.properties");
             FileReader enFileReader = new FileReader(pathPre + "en.properties")) {
            uz = new Properties();
            ru = new Properties();
            en = new Properties();
            uz.load(uzFileReader);
            ru.load(ruFileReader);
            en.load(enFileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String chatId, String key) {
        String language = authUserRepository.getLanguage(chatId);
        if (language.equalsIgnoreCase("uz"))
            return uz.getProperty(key);
        if (language.equalsIgnoreCase("ru"))
            return ru.getProperty(key);
        return en.getProperty(key);
    }
}

