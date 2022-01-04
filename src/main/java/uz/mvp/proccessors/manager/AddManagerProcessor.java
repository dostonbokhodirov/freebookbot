package uz.mvp.proccessors.manager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.ManagerState;
import uz.mvp.enums.state.MenuState;
import uz.mvp.repository.authuser.AuthUserRepository;

import java.util.Objects;

/**
 * @author Doston Bokhodirov, Sun 3:06 PM. 12/26/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddManagerProcessor {
    private static final AddManagerProcessor instance = new AddManagerProcessor();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void process(Message message, ManagerState managerState) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        if (managerState.equals(ManagerState.UNDEFINED)) {
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "enter.user.id"));
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
            if (Objects.nonNull(roleById) && (roleById.equals("MANAGER")) ||
                    (Objects.nonNull(roleByNumber) && roleByNumber.equals("MANAGER"))) {
                sendMessage = new SendMessage(chatId, Emojis.REALLY + LangConfig.get(chatId, "already.manager"));
            } else if (authUserRepository.save("role", "MANAGER", text) != 1) {
                if (authUserRepository.update("role", "MANAGER", text) != 1) {
                    sendMessage = new SendMessage(chatId, Emojis.REMOVE + " " + LangConfig.get(chatId, "user.not.found"));
                } else {
                    sendMessage = new SendMessage(chatId,
                            Emojis.ADD + " " + LangConfig.get(chatId, "user.changed.manager"));
                }
            } else {
                sendMessage = new SendMessage(chatId,
                        Emojis.ADD + " " + LangConfig.get(chatId, "user.changed.manager"));
                BOT.executeMessage(sendMessage);
            }
            BOT.executeMessage(sendMessage);
            State.setManagerState(chatId, ManagerState.UNDEFINED);
            State.setMenuState(chatId, MenuState.UNDEFINED);
        }
    }

    public static AddManagerProcessor getInstance() {
        return instance;
    }
}
