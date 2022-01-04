package uz.mvp.configs.search;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Doston Bokhodirov, Sat 9:57 AM. 12/25/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Offset {
    private static final Offset instance = new Offset();
    private static final Map<String, Integer> searchOffset = new HashMap<>();

    public Integer getSearchOffset(String chatId) {
        return searchOffset.get(chatId);
    }

    public void setSearchOffset(String chatId, int offset) {
        if (offset == 0) {
            searchOffset.put(chatId, offset);
        } else searchOffset.put(chatId, getSearchOffset(chatId) + offset);
    }

    public static Offset getInstance() {
        return instance;
    }
}
