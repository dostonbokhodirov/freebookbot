package uz.mvp.proccessors.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.Role;
import uz.mvp.enums.state.UState;
import uz.mvp.proccessors.menu.MenuProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.services.bot.BotService;

import java.util.Objects;

import static uz.mvp.configs.State.setMenuState;
import static uz.mvp.configs.State.setState;

/**
 * @author Doston Bokhodirov, Sun 8:57 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorizationProcessor {
    private static final AuthorizationProcessor instance = new AuthorizationProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    public static final MenuProcessor menuProcessor = MenuProcessor.getInstance();
    private static final BotService botService = BotService.getInstance();

    public void process(Message message, UState state) {
        String chatId = message.getChatId().toString();
        if (UState.LANG.equals(state) || Objects.isNull(state)) {
            String text = """
                    Tilni tanlang:
                    Выберите язык:
                    Choose your language:""";
            SendMessage sendMessage = new SendMessage(chatId, text);
            sendMessage.setReplyMarkup(InlineBoard.languageButtons());
            BOT.executeMessage(sendMessage);
            setState(chatId, UState.DELETE_ALL);
        } else if (UState.FULL_NAME.equals(state)) {
            String text = message.getText();
            if (StringUtils.isNumeric(text) || !text.equals(StringUtils.capitalize(text))) {
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.LOOK + " " + LangConfig.get(chatId, "full.name.correctly") + "\n"
                                + LangConfig.get(chatId, "without.numbers"));
                sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                BOT.executeMessage(sendMessage);
            } else {
                authUserRepository.save("full_name", text, message.getChatId().toString());
                SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "enter.age"));
//                sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                BOT.executeMessage(sendMessage);
                setState(chatId, UState.AGE);
            }
        } else if (UState.AGE.equals(state)) {
            String text = message.getText();
            if (StringUtils.isNumeric(text)) {
                if (Integer.parseInt(text) <= 100) {
                    authUserRepository.save("age", text, chatId);
                    SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "enter.gender"));
                    sendMessage.setReplyMarkup(InlineBoard.gender(chatId));
                    BOT.executeMessage(sendMessage);
                    setState(chatId, UState.DELETE_ALL);
                } else {
                    SendMessage sendMessage = new SendMessage(chatId,
                            LangConfig.get(chatId, "sure.age") + " " + Emojis.REALLY + "\n" +
                                    LangConfig.get(chatId, "enter.age.again"));
                    BOT.executeMessage(sendMessage);
                }
            } else {
                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "age.correctly") + " " + Emojis.SAD);
                BOT.executeMessage(sendMessage);
            }
        } else if (UState.PHONE_NUMBER.equals(state)) {
            if (message.hasContact()) {
                String phoneNumber = message.getContact().getPhoneNumber();
                if (!phoneNumber.contains("+")) {
                    phoneNumber = "+" + phoneNumber;
                }
                authUserRepository.save("phone_number", phoneNumber, message.getChatId().toString());
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.ADD + " <b>" + LangConfig.get(chatId, "congratulations") + " " + Emojis.GREAT + "\n" +
                                LangConfig.get(chatId, "welcome") + "</b>");
                BOT.executeMessage(sendMessage);
                setState(chatId, UState.AUTHORIZED);
                setMenuState(chatId, MenuState.UNDEFINED);
                authUserRepository.save("role", Role.USER.toString(), chatId);
                menuProcessor.menu(chatId, "USER", botService.getMessage(chatId));
            } else {
                SendMessage sendMessage = new SendMessage(chatId,
                        Emojis.REMOVE + " " + LangConfig.get(chatId, "invalid.number") + "\n" +
                                LangConfig.get(chatId, "correct.number"));
                BOT.executeMessage(sendMessage);
            }
        }

        if (UState.DELETE_ALL.equals(state)) {
            DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
            BOT.executeMessage(deleteMessage);
        }
    }

    public static AuthorizationProcessor getInstance() {
        return instance;
    }
}
