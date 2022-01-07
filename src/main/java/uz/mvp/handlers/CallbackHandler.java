package uz.mvp.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.add.BookId;
import uz.mvp.configs.search.MSG;
import uz.mvp.configs.search.Offset;
import uz.mvp.emojis.Emojis;
import uz.mvp.entity.book.Book;
import uz.mvp.enums.state.AddBookState;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.state.SearchState;
import uz.mvp.proccessors.book.SearchBookProcessor;
import uz.mvp.repository.AbstractRepository;
import uz.mvp.repository.book.BookRepository;
import uz.mvp.services.callbackHandler.CallBackHandlerService;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Sun 8:39 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CallbackHandler extends AbstractRepository {
    private static final CallbackHandler instance = new CallbackHandler();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final CallBackHandlerService callBackHandlerService = CallBackHandlerService.getInstance();
    private static final SearchBookProcessor searchBookProcessor = SearchBookProcessor.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final BookId bookId = BookId.getInstance();
    private static final Offset offset = Offset.getInstance();
    private static final MSG msg = MSG.getInstance();

    public void handle(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();
        String chatId = message.getChatId().toString();
        String fileId = bookId.getBookId(chatId);
        if ("uz".equals(data) || "ru".equals(data) || "en".equals(data)) {
            deleteMessage(message, chatId);
            callBackHandlerService.languageMessage(message, data);
        } else if ("male".equals(data) || "female".equals(data)) {
            callBackHandlerService.genderMessage(message, data);
        } else if (data.equals("prev")) {
            offset.setSearchOffset(chatId, -1);
            callBackHandlerService.prevMessage(message, offset.getSearchOffset(chatId));
        } else if (data.equals("next")) {
            offset.setSearchOffset(chatId, 1);
            callBackHandlerService.nextMessage(message, offset.getSearchOffset(chatId));
        } else if (data.equals("cancel")) {
            offset.setSearchOffset(chatId, 0);
            deleteMessage(message, chatId);
            State.setMenuState(chatId, MenuState.UNDEFINED);
            State.setSearchState(chatId, SearchState.UNDEFINED);
        } else if (data.equals("add")) {
            SendMessage sendMessage;
            if (!bookRepository.isHaveBookWithUser(chatId, fileId)) {
                bookRepository.addDownloads(chatId, fileId);
                sendMessage = new SendMessage(chatId, Emojis.ADD + LangConfig.get(chatId, "book.added"));
            } else sendMessage = new SendMessage(chatId, Emojis.LOOK + LangConfig.get(chatId, "already.add"));
            BOT.executeMessage(sendMessage);
        } else if (data.equals("remove")) {
            bookRepository.removeDownloads(chatId, fileId);
            SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + LangConfig.get(chatId, "book.removed"));
            BOT.executeMessage(sendMessage);
        } else if (data.equals("cancelDocument")) {
            deleteMessage(message, chatId);
        } else if (data.equals("genre")) {
            State.setSearchState(chatId, SearchState.GENRE);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setText(Emojis.GENRE + " " + LangConfig.get(chatId, "choose.genre"));
            editMessageText.setReplyMarkup(InlineBoard.genreButtons(chatId));
            BOT.executeMessage(editMessageText);
            State.setMenuState(chatId, MenuState.SEARCH);
        } else if (data.equals("name")) {
            deleteMessage(message, chatId);
            State.setSearchState(chatId, SearchState.NAME);
            SendMessage sendMessage = new SendMessage(chatId, Emojis.SEARCH + " " + LangConfig.get(chatId, "enter.book"));
            BOT.executeMessage(sendMessage);
            State.setMenuState(chatId, MenuState.SEARCH);
            offset.setSearchOffset(chatId, 0);
        } else if (data.equals("adventure") || data.equals("classic")
                || data.equals("comic") || data.equals("fiction")
                || data.equals("horror") || data.equals("scientific") || data.equals("other")) {
            if (State.getAddBookState(chatId).equals(AddBookState.GENRE)) {
                deleteMessage(message, chatId);
                bookRepository.save("genre", data, fileId);
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.ADD + " " + LangConfig.get(chatId, "file.uploaded"));
                BOT.executeMessage(sendMessage);
                State.setAddBookState(chatId, AddBookState.UNDEFINED);
                State.setMenuState(chatId, MenuState.UNDEFINED);
            } else if (State.getSearchState(chatId).equals(SearchState.GENRE)) {
                msg.setSearchGenre(chatId, data);
                ArrayList<Book> books = bookRepository.getBooksByGenre(data, State.getLimitState(chatId), offset.getSearchOffset(chatId));
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setMessageId(message.getMessageId());
                editMessageText.setChatId(chatId);
                if (books.size() == 0) {
                    editMessageText.setText(LangConfig.get(chatId, "no.book.genre"));
                    State.setMenuState(chatId, MenuState.UNDEFINED);
                    State.setSearchState(chatId, SearchState.UNDEFINED);
                } else {
                    offset.setSearchOffset(chatId, 0);
                    editMessageText.setText(callBackHandlerService.getBookMessage(books, chatId).toString());
                    editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset.getSearchOffset(chatId)));
                }
                BOT.executeMessage(editMessageText);
            }
        } else if (data.equals("five")) {
            State.setLimitState(chatId, 5);
            deleteMessage(message, chatId);
            sendMessage(chatId);
        } else if (data.equals("eight")) {
            State.setLimitState(chatId, 8);
            deleteMessage(message, chatId);
            sendMessage(chatId);
        } else if (data.equals("ten")) {
            State.setLimitState(chatId, 10);
            deleteMessage(message, chatId);
            sendMessage(chatId);
        } else {
            callBackHandlerService.getDocument(data, chatId);
        }
    }

    private void sendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(Emojis.ADD + " " + LangConfig.get(chatId, "limit.changed") + "\n" +
                LangConfig.get(chatId, "current.limit") + State.getLimitState(chatId));
        BOT.executeMessage(sendMessage);
    }

    private void deleteMessage(Message message, String chatId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        BOT.executeMessage(deleteMessage);
    }

    public static CallbackHandler getInstance() {
        return instance;
    }
}
