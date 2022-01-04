package uz.mvp.proccessors.menu;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.AddBookState;
import uz.mvp.enums.state.MenuState;
import uz.mvp.proccessors.book.AddBookProcessor;
import uz.mvp.proccessors.book.DownloadedBookProcessor;
import uz.mvp.proccessors.book.RemoveBookProcessor;
import uz.mvp.proccessors.book.UploadedBookProcessor;
import uz.mvp.proccessors.manager.AddManagerProcessor;
import uz.mvp.proccessors.manager.RemoveManagerProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;

import static uz.mvp.configs.State.getAddBookState;

/**
 * @author Doston Bokhodirov, Tue 12:27 PM. 12/21/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminMenuProcessor {
    private static final AdminMenuProcessor instance = new AdminMenuProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final AddBookProcessor addBookProcessor = AddBookProcessor.getInstance();
    private static final RemoveBookProcessor removeBookProcessor = RemoveBookProcessor.getInstance();
    private static final UploadedBookProcessor uploadedBookProcessor = UploadedBookProcessor.getInstance();
    private static final DownloadedBookProcessor downloadedBookProcessor = DownloadedBookProcessor.getInstance();
    private static final RemoveManagerProcessor removeManagerProcessor = RemoveManagerProcessor.getInstance();
    private static final AddManagerProcessor addManagerProcessor = AddManagerProcessor.getInstance();

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String role = authUserRepository.findRoleById(chatId);
        MenuState menuState = State.getMenuState(chatId);
        if (MenuState.UNDEFINED.equals(menuState)) {
            if ((Emojis.ADD + " " + LangConfig.get(chatId, "add.manager")).equals(text)) {
                State.setMenuState(chatId, MenuState.ADD_MANAGER);
                addManagerProcessor.process(message, State.getManagerState(chatId));
            } else if ((Emojis.REMOVE + " " + LangConfig.get(chatId, "remove.manager")).equals(text)) {
                State.setMenuState(chatId, MenuState.REMOVE_MANAGER);
                removeManagerProcessor.process(message, State.getManagerState(chatId));
            } else if ((Emojis.ADD_BOOK + " " + LangConfig.get(chatId, "add.book")).equals(text)) {
                AddBookState addBookState = getAddBookState(chatId);
                State.setMenuState(chatId, MenuState.ADD_BOOK);
                addBookProcessor.process(message, addBookState, role);
            } else if ((Emojis.REMOVE_BOOK + " " + LangConfig.get(chatId, "remove.book")).equals(text)) {
                State.setMenuState(chatId, MenuState.REMOVE_BOOK);
                removeBookProcessor.process(message, State.getRemoveBookState(chatId));
            } else if ((Emojis.DOWNLOAD + " " + LangConfig.get(chatId, "downloaded.books")).equals(text)) {
                State.setMenuState(chatId, MenuState.DOWNLOADED);
                downloadedBookProcessor.process(message);
            } else if ((Emojis.UPLOAD + " " + LangConfig.get(chatId, "uploaded.books")).equals(text)) {
                State.setMenuState(chatId, MenuState.UPLOADED);
                uploadedBookProcessor.process(message);
            } else {
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.REMOVE + " " + LangConfig.get(chatId, "wrong.button"));
                BOT.executeMessage(sendMessage);
            }
        }
    }

    public static AdminMenuProcessor getInstance() {
        return instance;
    }
}
