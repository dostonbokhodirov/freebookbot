package uz.mvp.proccessors.user;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.State;
import uz.mvp.configs.search.Offset;
import uz.mvp.entity.user.User;
import uz.mvp.enums.state.MenuState;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.services.callbackHandler.CallBackHandlerService;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Sat 8:48 PM. 12/25/2021
 */
public class UserListProcessor {
    private static final UserListProcessor instance = new UserListProcessor();
    private static final CallBackHandlerService callBackHandlerService = CallBackHandlerService.getInstance();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final Offset offset = Offset.getInstance();

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        offset.setSearchOffset(chatId, 0);
        ArrayList<User> users = authUserRepository.getUsers(State.getLimitState(chatId), offset.getSearchOffset(chatId));
        SendMessage sendMessage;
        sendMessage = new SendMessage(chatId, callBackHandlerService.getUserMessage(users, chatId).toString());
        sendMessage.setReplyMarkup(InlineBoard.user(users, offset.getSearchOffset(chatId)));
        BOT.executeMessage(sendMessage);
        State.setMenuState(chatId, MenuState.UNDEFINED);
    }

    public static UserListProcessor getInstance() {
        return instance;
    }
}
