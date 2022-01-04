package uz.mvp.proccessors.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.enums.state.RemoveBookState;

/**
 * @author Juraev Nodirbek, ср 16:39. 22.12.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyBooksProcessor {
    private static final MyBooksProcessor instance = new MyBooksProcessor();

    public void process(Message message, RemoveBookState state) {
        message.getDocument().getFileId();
        message.getDocument().getFileUniqueId();
    }

    public static MyBooksProcessor getInstance() {
        return instance;
    }
}
