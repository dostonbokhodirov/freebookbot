package uz.mvp.proccessors.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.MarkupBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.ManagerState;
import uz.mvp.enums.state.MenuState;
import uz.mvp.repository.authuser.AuthUserRepository;

import java.util.Objects;

/**
 * @author Doston Bokhodirov, Sun 2:53 PM. 12/26/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RemoveManagerProcessor {
    private static final RemoveManagerProcessor instance = new RemoveManagerProcessor();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void process(Message message, ManagerState managerState) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        if (managerState.equals(ManagerState.UNDEFINED)) {
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "enter.user.id"));
            BOT.executeMessage(sendMessage);
            State.setManagerState(chatId, ManagerState.UNIQUE);
        } else if (managerState.equals(ManagerState.UNIQUE)) {
            if (!StringUtils.isNumeric(text)) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(Emojis.REMOVE + " " + LangConfig.get(chatId, "id.consists"));
                BOT.executeMessage(sendMessage);
                State.setManagerState(chatId, ManagerState.UNDEFINED);
                State.setMenuState(chatId, MenuState.UNDEFINED);
                return;
            }
            SendMessage sendMessage;
            String roleById = authUserRepository.findRoleById(text);
            String roleByNumber = authUserRepository.findRoleByNumber(text);
            if (Objects.nonNull(roleById) && (roleById.equals("USER")) ||
                    (Objects.nonNull(roleByNumber) && roleByNumber.equals("USER"))) {
                sendMessage = new SendMessage(chatId,
                        Emojis.REALLY +  " " + LangConfig.get(chatId, "already.user"));
            } else if (authUserRepository.save("role", "USER", text) != 1) {
                if (authUserRepository.update("role", "USER", text) != 1) {
                    sendMessage = new SendMessage(chatId, Emojis.REMOVE + " " + LangConfig.get(chatId, "user.not.found"));
                } else {
                    sendMessage = new SendMessage(chatId, Emojis.ADD + " " + LangConfig.get(chatId, "user.changed.user"));
                }
            } else {
                sendMessage = new SendMessage(chatId, Emojis.ADD + " " + LangConfig.get(chatId, "user.changed.user"));
            }
            sendMessage.setReplyMarkup(MarkupBoard.adminMenu(chatId));
            BOT.executeMessage(sendMessage);
            State.setManagerState(chatId, ManagerState.UNDEFINED);
            State.setMenuState(chatId, MenuState.UNDEFINED);
        }
    }

    public static RemoveManagerProcessor getInstance() {
        return instance;
    }
}
