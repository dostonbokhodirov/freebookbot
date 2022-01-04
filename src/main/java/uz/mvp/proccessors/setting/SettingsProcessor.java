package uz.mvp.proccessors.setting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.buttons.MarkupBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.MenuState;
import uz.mvp.enums.state.SettingsState;
import uz.mvp.repository.authuser.AuthUserRepository;

import java.util.Objects;

import static uz.mvp.proccessors.user.AuthorizationProcessor.menuProcessor;

/**
 * @author Juraev Nodirbek, пн 23:23. 20.12.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SettingsProcessor {
    private static final SettingsProcessor instance = new SettingsProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    public void process(Message message, SettingsState settingsState) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        if (Objects.nonNull(text) && text.equals(Emojis.NAME + " " + LangConfig.get(chatId, "change.name")) && settingsState.equals(SettingsState.UNDEFINED)) {
            String name = authUserRepository.findFieldByChatID(chatId, "full_name");
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "current.name") + " " + name + "\n" +
                            LangConfig.get(chatId, "new.name"));
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.CHANGE_NAME);
        } else if (settingsState.equals(SettingsState.CHANGE_NAME)) {
            if (StringUtils.isNumeric(text) || !text.equals(StringUtils.capitalize(text))) {
                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "full.name.correctly") + "\n" +
                                LangConfig.get(chatId, "without.numbers"));
                sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                BOT.executeMessage(sendMessage);
            } else {
                authUserRepository.save("full_name", text, chatId);
                SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                        LangConfig.get(chatId, "full.name.changed") + "\n" +
                        LangConfig.get(chatId, "current.name") + " " +
                        authUserRepository.findFieldByChatID(chatId, "full_name"));
                BOT.executeMessage(sendMessage);
                State.setSettingsState(chatId, SettingsState.UNDEFINED);
                menu(chatId, LangConfig.get(chatId, "settings.menu"));
            }
        } else if (Objects.nonNull(text) && text.equals(Emojis.AGE + " " + LangConfig.get(chatId, "change.birthdate")) &&
                settingsState.equals(SettingsState.UNDEFINED)) {
            String age = authUserRepository.findFieldByChatID(chatId, "age");
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "current.age") + " " + age + "\n" +
                    LangConfig.get(chatId, "new.age"));
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.CHANGE_AGE);
        } else if (settingsState.equals(SettingsState.CHANGE_AGE)) {
            if (StringUtils.isNumeric(text)) {
                if (Integer.parseInt(text) <= 100) {
                    authUserRepository.save("age", text, chatId);
                    SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                            LangConfig.get(chatId, "age.changed") + "\n" +
                            LangConfig.get(chatId, "current.age") + authUserRepository.findFieldByChatID(chatId, "age"));
                    BOT.executeMessage(sendMessage);
                    State.setSettingsState(chatId, SettingsState.UNDEFINED);
                    menu(chatId, LangConfig.get(chatId, "settings.menu"));
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
        } else if (Objects.nonNull(text) && text.equals(Emojis.PHONE + " " + LangConfig.get(chatId, "change.phone.number"))
                && settingsState.equals(SettingsState.UNDEFINED)) {
            String phoneNumber = authUserRepository.findFieldByChatID(chatId, "phone_number");
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "current.phone.number") + " " + phoneNumber + "\n" +
                            LangConfig.get(chatId, "new.phone.number"));
            sendMessage.setReplyMarkup(MarkupBoard.sharePhoneNumber(chatId));
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.CHANGE_NUMBER);
        } else if (Objects.nonNull(text) && text.equals(Emojis.LIMIT + " " + LangConfig.get(chatId, "change.limit"))
                && settingsState.equals(SettingsState.UNDEFINED)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(LangConfig.get(chatId, "choose.limit"));
            sendMessage.setReplyMarkup(InlineBoard.limitButtons());
            BOT.executeMessage(sendMessage);
        } else if (settingsState.equals(SettingsState.CHANGE_NUMBER)) {
            if (message.hasContact()) {
                authUserRepository.save("phone_number", text, chatId);
                SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                        LangConfig.get(chatId, "phone.number.changed") + "\n" +
                        LangConfig.get(chatId, "current.phone.number") + " " + message.getContact().getPhoneNumber());
                BOT.executeMessage(sendMessage);
                State.setSettingsState(chatId, SettingsState.UNDEFINED);
                menu(chatId, LangConfig.get(chatId, "settings.menu"));
            } else {
                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "invalid.number") + "\n" +
                                LangConfig.get(chatId, "correct.number"));
                BOT.executeMessage(sendMessage);
            }
        } else if (Objects.nonNull(text) && text.equals(Emojis.LANGUAGE + " " + LangConfig.get(chatId, "change.language"))
                && settingsState.equals(SettingsState.UNDEFINED)) {
            String language = authUserRepository.findFieldByChatID(chatId, "language");
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "current.language") + " " + language + "\n" +
                            LangConfig.get(chatId, "new.language"));
            sendMessage.setReplyMarkup(InlineBoard.languageButtons());
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.CHANGE_LANGUAGE);
        } else if (settingsState.equals(SettingsState.CHANGE_LANGUAGE)) {
            SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                    LangConfig.get(chatId, "language.changed") + "\n" +
                    LangConfig.get(chatId, "current.language") + " " +
                    authUserRepository.findFieldByChatID(chatId, "language"));
            BOT.executeMessage(sendMessage);
            State.setSettingsState(chatId, SettingsState.UNDEFINED);
            menu(chatId, LangConfig.get(chatId, "settings.menu"));
        } else if (Objects.nonNull(text) && text.equals(Emojis.BACK + " " + LangConfig.get(chatId, "back"))) {
            String role = authUserRepository.findRoleById(chatId);
            menuProcessor.menu(chatId, role, "<b>" + LangConfig.get(chatId, "back.menu") + "</b>");
            State.setMenuState(chatId, MenuState.UNDEFINED);
            State.setSettingsState(chatId, SettingsState.UNDEFINED);
        } else menu(chatId, LangConfig.get(chatId, "wrong.button"));
    }

    public void menu(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (text.equals(LangConfig.get(chatId, "wrong.button"))) {
            sendMessage.setText(Emojis.REMOVE + " " + text);
        } else {
            sendMessage.setText(text);
        }
        sendMessage.setReplyMarkup(MarkupBoard.settingsMenu(chatId));
        BOT.executeMessage(sendMessage);
    }

    public static SettingsProcessor getInstance() {
        return instance;
    }
}
