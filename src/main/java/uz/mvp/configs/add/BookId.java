package uz.mvp.configs.add;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Doston Bokhodirov, Sat 11:45 AM. 1/1/2022
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookId {
    private static final BookId instance = new BookId();
    private static final Map<String, String> bookId = new HashMap<>();

    public String getBookId(String chatId) {
         return bookId.get(chatId);
    }

    public void setBookId(String chatId, String fileId) {
        bookId.put(chatId, fileId);
    }

    public static BookId getInstance() {
        return instance;
    }
}
