package uz.mvp.proccessors.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.emojis.Emojis;
import uz.mvp.entity.book.Book;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.state.RemoveBookState;
import uz.mvp.enums.state.SearchState;
import uz.mvp.repository.book.BookRepository;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Sat 9:28 AM. 12/25/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemoveBookProcessor {
    private static final RemoveBookProcessor instance = new RemoveBookProcessor();
    public static final BookRepository bookRepository = BookRepository.getInstance();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void process(Message message, RemoveBookState removeBookState) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        if (removeBookState.equals(RemoveBookState.UNDEFINED)) {
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "enter.remove.book"));
            BOT.executeMessage(sendMessage);
            State.setRemoveBookState(chatId, RemoveBookState.NAME);
        }
        else if (removeBookState.equals(RemoveBookState.NAME)) {
            ArrayList<Book> books = bookRepository.getBooksByName(text, State.getLimitState(chatId), 0);
            if (books.size() == 0) {
                SendMessage sendMessage = new SendMessage(chatId,  LangConfig.get(chatId, "no.book") + " " + Emojis.CONFUSE);
                State.setSearchState(chatId, SearchState.UNDEFINED);
                BOT.executeMessage(sendMessage);
            } else {
                bookRepository.delete(text);
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.ADD + " " + LangConfig.get(chatId, "book.deleted"));
                BOT.executeMessage(sendMessage);
            }
            State.setRemoveBookState(chatId, RemoveBookState.UNDEFINED);
            State.setMenuState(chatId, MenuState.UNDEFINED);
        }
    }

    public static RemoveBookProcessor getInstance() {
        return instance;
    }
}
