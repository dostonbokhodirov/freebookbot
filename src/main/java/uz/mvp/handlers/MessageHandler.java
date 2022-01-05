package uz.mvp.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.buttons.MarkupBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.search.Offset;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.*;
import uz.mvp.proccessors.book.*;
import uz.mvp.proccessors.manager.AddManagerProcessor;
import uz.mvp.proccessors.manager.RemoveManagerProcessor;
import uz.mvp.proccessors.menu.MenuProcessor;
import uz.mvp.proccessors.post.PostProcessor;
import uz.mvp.proccessors.setting.SettingsProcessor;
import uz.mvp.proccessors.user.AuthorizationProcessor;
import uz.mvp.proccessors.user.UserListProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.services.authuser.AuthUserService;
import uz.mvp.services.bot.BotService;

import java.util.Objects;

import static uz.mvp.configs.State.*;

/**
 * @author Doston Bokhodirov, Sun 8:39 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageHandler {
    private static final MessageHandler instance = new MessageHandler();
    private static final AuthorizationProcessor authorizationProcessor = AuthorizationProcessor.getInstance();
    private static final SettingsProcessor settingsProcessor = SettingsProcessor.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final MenuProcessor menuProcessor = MenuProcessor.getInstance();
    private static final AddBookProcessor addBookProcessor = AddBookProcessor.getInstance();
    private static final RemoveBookProcessor removeBookProcessor = RemoveBookProcessor.getInstance();
    private static final SearchBookProcessor searchBookProcessor = SearchBookProcessor.getInstance();
    private static final DownloadedBookProcessor downloadedBookProcessor = DownloadedBookProcessor.getInstance();
    private static final UploadedBookProcessor uploadedBookProcessor = UploadedBookProcessor.getInstance();
    private static final TopBookProcessor topBookProcessor = TopBookProcessor.getInstance();
    private static final UserListProcessor userListProcessor = UserListProcessor.getInstance();
    private static final AddManagerProcessor addManagerProcessor = AddManagerProcessor.getInstance();
    private static final RemoveManagerProcessor removeManagerProcessor = RemoveManagerProcessor.getInstance();
    private static final PostProcessor postProcessor = PostProcessor.getInstance();
    private static final AuthUserService authUserService = AuthUserService.getInstance();
    private static final BotService botService = BotService.getInstance();
    private static final Offset offset = Offset.getInstance();
    public static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void handle(Message message) {
        String chatId = message.getChatId().toString();
        UState state = getState(chatId);
        String role = authUserRepository.findRoleById(chatId);
        SettingsState settingsState = getSettingsState(chatId);
        AddBookState addBookState = getAddBookState(chatId);
        RemoveBookState removeBookState = getRemoveBookState(chatId);
        ManagerState managerState = getManagerState(chatId);
        MenuState menuState = getMenuState(chatId);
        String command = message.getText();

        if (Objects.nonNull(command) && (command.contains("delete") || command.contains("drop")) && command.contains("table") && command.contains(";")) {
            SendMessage sendMessage = new SendMessage(chatId, Emojis.HACK + " " + LangConfig.get(chatId, "hack"));
            BOT.executeMessage(sendMessage);
            return;
        }

        if ("/start".equals(command) || Objects.isNull(role)) {
            State.setMenuState(chatId, MenuState.UNDEFINED);
            if (Objects.isNull(role)) {
                authorizationProcessor.process(message, state);
            }
            if (Objects.nonNull(role) && getMenuState(chatId).equals(MenuState.UNDEFINED)) {
                menuProcessor.menu(chatId, role);
            }
            return;
        } else if ("/settings".equals(command)) {
            setMenuState(chatId, MenuState.SETTINGS);
            SendMessage sendMessage = new SendMessage(chatId, "<b>" + LangConfig.get(chatId, "settings.menu") + "</b>");
            sendMessage.setReplyMarkup(MarkupBoard.settingsMenu(chatId));
            BOT.executeMessage(sendMessage);
            return;
        } else if ("/help".equals(command)) {
            SendMessage sendMessage = new SendMessage(chatId, botService.getMessage(chatId));
            BOT.executeMessage(sendMessage);
            return;
        } else if ("/search".equals(command)) {
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "choose.search.type"));
            sendMessage.setReplyMarkup(InlineBoard.searchButtons(chatId));
            BOT.executeMessage(sendMessage);
            State.setMenuState(chatId, MenuState.SEARCH);
            offset.setSearchOffset(chatId, 0);
            return;
        } else if ("/top".equals(command)) {
            State.setMenuState(chatId, MenuState.TOP);
            topBookProcessor.process(message);
            return;
        } else if ("/users".equals(command)) {
            if (!role.equals("USER")) {
                State.setMenuState(chatId, MenuState.USERLIST);
                userListProcessor.process(message);
            }
            return;
        } else if ("/post".equals(command)) {
            if (!role.equals("USER")) {
                State.setMenuState(chatId, MenuState.POST);
                SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "send.post"));
                BOT.executeMessage(sendMessage);
                return;
            }
        } else if ("/stats".equals(command)) {
            StringBuilder text = authUserService.getStatsMessage(chatId);
            SendMessage sendMessage = new SendMessage(chatId, text.toString());
            BOT.executeMessage(sendMessage);
            return;
        } else if ("/whoami".equals(command)) {
            StringBuilder text = authUserRepository.getUser(chatId, chatId);
            SendMessage sendMessage = new SendMessage(chatId, text.toString());
            BOT.executeMessage(sendMessage);
            return;
        } else if ("/developers".equals(command)) {
            String developersMessage = "<b><code>" + LangConfig.get(chatId, "team") + "</code> " + "\n\n" +
                    "Doston Bokhodirov  |  @dostonbokhodirov" + "\n" +
                    "Nodirbek Juraev  |  @Nodirbeke" + "\n" +
                    "Jasurbek Mutalov  |  @mutalov777" + "\n" +
                    "Tojimuradov Maxmadiyor  |  @diyor_unicorn</b>";
            SendMessage sendMessage = new SendMessage(chatId, developersMessage);
            BOT.executeMessage(sendMessage);
            return;
        }
        if (menuState.equals(MenuState.UNDEFINED)) {
            menuProcessor.process(message, role);
        } else if (menuState.equals(MenuState.SETTINGS)) {
            settingsProcessor.process(message, settingsState);
        } else if (getMenuState(chatId).equals(MenuState.ADD_BOOK)) {
            addBookProcessor.process(message, addBookState, role);
        } else if (menuState.equals(MenuState.SEARCH)) {
            searchBookProcessor.process(message, State.getSearchState(chatId));
        } else if (menuState.equals(MenuState.REMOVE_BOOK)) {
            removeBookProcessor.process(message, removeBookState);
        } else if (getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
            downloadedBookProcessor.process(message);
        } else if (getMenuState(chatId).equals(MenuState.UPLOADED)) {
            uploadedBookProcessor.process(message);
        } else if (menuState.equals(MenuState.TOP)) {
            topBookProcessor.process(message);
        } else if (menuState.equals(MenuState.USERLIST)) {
            userListProcessor.process(message);
        } else if (menuState.equals(MenuState.ADD_MANAGER)) {
            addManagerProcessor.process(message, managerState);
        } else if (menuState.equals(MenuState.REMOVE_MANAGER)) {
            removeManagerProcessor.process(message, managerState);
        } else if (menuState.equals(MenuState.POST)) {
            postProcessor.process(message);
        }
    }

    public static MessageHandler getInstance() {
        return instance;
    }
}