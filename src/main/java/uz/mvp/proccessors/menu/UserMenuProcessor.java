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
import uz.mvp.enums.state.SettingsState;
import uz.mvp.proccessors.book.AddBookProcessor;
import uz.mvp.proccessors.book.DownloadedBookProcessor;
import uz.mvp.proccessors.setting.SettingsProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;

import static uz.mvp.configs.State.*;

/**
 * @author Doston Bokhodirov, Tue 12:15 AM. 12/21/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMenuProcessor {
    private static final UserMenuProcessor instance = new UserMenuProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final SettingsProcessor settingsProcessor = SettingsProcessor.getInstance();
    private static final AddBookProcessor addBookProcessor = AddBookProcessor.getInstance();
    private static final DownloadedBookProcessor downloadedBookProcessor = DownloadedBookProcessor.getInstance();

    public static UserMenuProcessor getInstance() {
        return instance;
    }

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String role = authUserRepository.findRoleById(chatId);
        MenuState menuState = getMenuState(chatId);
        if (menuState.equals(MenuState.UNDEFINED)) {
            if ((Emojis.ADD_BOOK + " " + LangConfig.get(chatId, "add.book")).equals(text)) {
                AddBookState addBookState = getAddBookState(chatId);
                setMenuState(chatId, MenuState.ADD_BOOK);
                addBookProcessor.process(message, addBookState, role);
            } else if ((Emojis.DOWNLOAD + " " + LangConfig.get(chatId, "downloaded.books")).equals(text)) {
                setMenuState(chatId, MenuState.DOWNLOADED);
                downloadedBookProcessor.process(message);
            } else {
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.REMOVE + " " + LangConfig.get(chatId, "wrong.button"));
                BOT.executeMessage(sendMessage);
            }
        } else if (menuState.equals(MenuState.SETTINGS)) {
            SettingsState settingsState = State.getSettingsState(chatId);
            settingsProcessor.process(message, settingsState);
        }
    }
}
