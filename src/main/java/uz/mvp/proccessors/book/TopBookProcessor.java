package uz.mvp.proccessors.book;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.search.Offset;
import uz.mvp.entity.book.Book;
import uz.mvp.enums.state.MenuState;
import uz.mvp.repository.book.BookRepository;
import uz.mvp.services.callbackHandler.CallBackHandlerService;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Sat 4:32 PM. 12/25/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TopBookProcessor {
    private static final TopBookProcessor instance = new TopBookProcessor();
    private static final CallBackHandlerService callBackHandlerService = CallBackHandlerService.getInstance();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final Offset offset = Offset.getInstance();

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        offset.setSearchOffset(chatId, 0);
        ArrayList<Book> books = bookRepository.getTopBooks(State.getLimitState(chatId), offset.getSearchOffset(chatId));
        SendMessage sendMessage;
        if (books.size() == 0) {
            sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "no.book"));
        } else {
            sendMessage = new SendMessage(chatId, callBackHandlerService.getBookMessage(books, chatId).toString());
            sendMessage.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset.getSearchOffset(chatId)));
        }
//        State.setMenuState(chatId, MenuState.UNDEFINED);
        BOT.executeMessage(sendMessage);
    }

    public static TopBookProcessor getInstance() {
        return instance;
    }
}
