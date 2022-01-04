package uz.mvp.proccessors.book;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.search.MSG;
import uz.mvp.configs.search.Offset;
import uz.mvp.entity.book.Book;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.state.SearchState;
import uz.mvp.repository.book.BookRepository;
import uz.mvp.services.callbackHandler.CallBackHandlerService;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Thu 11:42 PM. 12/23/2021
 */
public class SearchBookProcessor {
    public static final SearchBookProcessor instance = new SearchBookProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final CallBackHandlerService callBackHandlerService = CallBackHandlerService.getInstance();
    private static final Offset offset = Offset.getInstance();
    private static final MSG msg = MSG.getInstance();

    public void process(Message message, SearchState state) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        msg.setSearchMessage(chatId, text);
        if (SearchState.NAME.equals(state)) {
            ArrayList<Book> books = bookRepository.getBooksByName(text, State.getLimitState(chatId), offset.getSearchOffset(chatId));
            SendMessage sendMessage;
            if (books.size() == 0) {
                sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "no.book.name"));
                State.setSearchState(chatId, SearchState.UNDEFINED);
                State.setMenuState(chatId, MenuState.UNDEFINED);
            } else {
                offset.setSearchOffset(chatId, 0);
                sendMessage = new SendMessage(chatId, callBackHandlerService.getBookMessage(books, chatId).toString());
                sendMessage.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset.getSearchOffset(chatId)));
            }
            BOT.executeMessage(sendMessage);
        }
    }

    public static SearchBookProcessor getInstance() {
        return instance;
    }
}
