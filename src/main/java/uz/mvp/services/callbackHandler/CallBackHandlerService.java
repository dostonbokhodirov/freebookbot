package uz.mvp.services.callbackHandler;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.buttons.MarkupBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.add.BookId;
import uz.mvp.configs.search.MSG;
import uz.mvp.emojis.Emojis;
import uz.mvp.entity.book.Book;
import uz.mvp.entity.user.User;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.state.SearchState;
import uz.mvp.enums.state.SettingsState;
import uz.mvp.enums.state.UState;
import uz.mvp.proccessors.setting.SettingsProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.repository.book.BookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static uz.mvp.configs.State.setState;

/**
 * @author Doston Bokhodirov, Sat 11:10 AM. 12/25/2021
 */
public class CallBackHandlerService {
    private static final CallBackHandlerService instance = new CallBackHandlerService();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final MSG msg = MSG.getInstance();
    private static final BookId bookId = BookId.getInstance();
    private static final SettingsProcessor settingsProcessor = SettingsProcessor.getInstance();

    public void languageMessage(Message message, String data) {
        String chatId = message.getChatId().toString();
        String role = authUserRepository.findRoleById(chatId);
        if (Objects.isNull(role)) {
            authUserRepository.save("language", data, chatId);
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "enter.full.name"));
            sendMessage.setReplyMarkup(new ForceReplyKeyboard());
            BOT.executeMessage(sendMessage);
            setState(chatId, UState.FULL_NAME);
        } else {
            authUserRepository.save("language", data, message.getChatId().toString());
            SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                    LangConfig.get(chatId, "language.changed") + "\n" +
                    LangConfig.get(chatId, "current.language") + data);
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.UNDEFINED);
            settingsProcessor.menu(chatId, LangConfig.get(chatId, "settings.menu"));
        }
    }

    public void genderMessage(Message message, String data) {
        String chatId = message.getChatId().toString();
        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        BOT.executeMessage(deleteMessage);
        authUserRepository.save("gender", data, message.getChatId().toString());
        SendMessage sendMessage = new SendMessage(chatId, Emojis.PHONE + " " + LangConfig.get(chatId, "share.phone.number") + ":");
        sendMessage.setReplyMarkup(MarkupBoard.sharePhoneNumber(chatId));
        BOT.executeMessage(sendMessage);
        setState(chatId, UState.PHONE_NUMBER);
    }

    public void nextMessage(Message message, Integer offset) {
        String chatId = message.getChatId().toString();
        ArrayList<Book> books;
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(message.getMessageId());
        if (State.getMenuState(chatId).equals(MenuState.SEARCH)) {
            if (State.getSearchState(chatId).equals(SearchState.NAME)) {
                books = bookRepository.getBooksByName(msg.getSearchMessage(chatId), State.getLimitState(chatId), offset);
            } else {
                books = bookRepository.getBooksByGenre(msg.getSearchGenre(chatId), State.getLimitState(chatId), offset);
            }
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.UPLOADED)) {
            books = bookRepository.getUploadedBooks(chatId, State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.TOP)) {
            books = bookRepository.getTopBooks(State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
            books = bookRepository.getDownloadedBooks(chatId, State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.USERLIST)) {
            ArrayList<User> users = authUserRepository.getUsers(State.getLimitState(chatId), offset);
            editMessageText.setText(getUserMessage(users, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.user(users, offset));
            BOT.executeMessage(editMessageText);
        }
    }

    public void prevMessage(Message message, Integer offset) {
        String chatId = message.getChatId().toString();
        ArrayList<Book> books;
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(message.getMessageId());
        if (State.getMenuState(chatId).equals(MenuState.SEARCH)) {
            if (State.getSearchState(chatId).equals(SearchState.NAME)) {
                books = bookRepository.getBooksByName(msg.getSearchMessage(chatId), State.getLimitState(chatId), offset);
            } else {
                books = bookRepository.getBooksByGenre(msg.getSearchGenre(chatId), State.getLimitState(chatId), offset);
            }
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.TOP)) {
            books = bookRepository.getTopBooks(State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.UPLOADED)) {
            books = bookRepository.getUploadedBooks(chatId, State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
            books = bookRepository.getDownloadedBooks(chatId, State.getLimitState(chatId), offset);
            editMessageText.setText(getBookMessage(books, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.book(books, State.getLimitState(chatId), offset));
            BOT.executeMessage(editMessageText);
        } else if (State.getMenuState(chatId).equals(MenuState.USERLIST)) {
            ArrayList<User> users = authUserRepository.getUsers(State.getLimitState(chatId), offset);
            editMessageText.setText(getUserMessage(users, chatId).toString());
            editMessageText.setReplyMarkup(InlineBoard.user(users, offset));
            BOT.executeMessage(editMessageText);
        }
    }

    public void getDocument(String data, String chatId) {
        String id = bookRepository.getBookId(data);
        bookId.setBookId(chatId, bookRepository.getBookId(data));
        if (Objects.isNull(id)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            StringBuilder text = authUserRepository.getUser(chatId, data);
            sendMessage.setText(text.toString());
            BOT.executeMessage(sendMessage);
        } else {
            if (!State.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
                Integer count = bookRepository.getDownloadsCount(id);
                bookRepository.save("downloads_count", count + 1, id);
            }
            InputFile inputFile = new InputFile(id);
            SendDocument sendDocument = new SendDocument(chatId, inputFile);
            sendDocument.setReplyMarkup(InlineBoard.documentButtons(chatId, State.getMenuState(chatId)));
            BOT.executeMessage(sendDocument);
        }
    }

    public StringBuilder getBookMessage(ArrayList<Book> books, String chatId) {
        StringBuilder messageText = new StringBuilder();
        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
        int i = 0;
        if (books.size() == 0) {
            messageText.append(LangConfig.get(chatId, "no.book"));
        } else {
            for (Book book : books) {
                double size = Math.round(Double.parseDouble(book.getSize()) / 1048576 * 100.0) / 100.0;
                messageText.append("<code>")
                        .append(numbers.get(i++)).append("</code> ")
                        .append(LangConfig.get(chatId, "book.name"))
                        .append(" <b>").append(book.getName()).append("</b>\n")
                        .append("      ").append(LangConfig.get(chatId, "book.size")).append(" <i>")
                        .append(size).append(" ").append(LangConfig.get(chatId, "mb")).append("</i>\n")
                        .append("      ").append(LangConfig.get(chatId, "uploaded.at")).append(" <code>")
                        .append(book.getUploadedAt()).append("</code>\n")
                        .append("      ").append(LangConfig.get(chatId, "number.downloads")).append(" <b>")
                        .append(book.getDownloadsCount()).append("</b>\n");
            }
        }
        return messageText;
    }

    public StringBuilder getUserMessage(ArrayList<User> users, String chatId) {
        StringBuilder messageText = new StringBuilder();
        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
        int i = 0;
        if (users.size() == 0) {
            messageText.append(LangConfig.get(chatId, "no.user"));
        } else {
            for (User user : users) {
                messageText.append("<code>")
                        .append(numbers.get(i++)).append("</code> ")
                        .append(LangConfig.get(chatId, "user.full.name")).append(" <b>")
                        .append(user.getFullName()).append("</b>\n")
                        .append("     ").append(LangConfig.get(chatId, "user.age")).append(" <b>")
                        .append(user.getAge()).append("</b>\n")
                        .append("     ").append(LangConfig.get(chatId, "user.name")).append(" @")
                        .append(user.getUserName()).append("\n")
                        .append("     ").append(LangConfig.get(chatId, "user.role")).append(" <code>")
                        .append(user.getRole()).append("</code>\n");
            }
        }
        return messageText;
    }

    public static CallBackHandlerService getInstance() {
        return instance;
    }

}
