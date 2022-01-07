package uz.mvp.configs.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Doston Bokhodirov, Fri 2:31 PM. 12/31/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MSG {
    private static final MSG instance = new MSG();
    private static final Map<String, String> searchMessage = new HashMap<>();
    private static final Map<String, String> searchGenre = new HashMap<>();

    public String getSearchMessage(String chatId) {
        return searchMessage.get(chatId);
    }

    public String getSearchGenre(String chatId) {
        return searchGenre.get(chatId);
    }

    public void setSearchMessage(String chatId, String message) {
        searchMessage.put(chatId, message);
    }

    public void setSearchGenre(String chatId, String genre) {
        searchGenre.put(chatId, genre);
    }

    public static MSG getInstance() {
        return instance;
    }
}
