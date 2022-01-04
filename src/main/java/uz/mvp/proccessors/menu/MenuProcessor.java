package uz.mvp.proccessors.menu;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.MarkupBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.enums.Role;

/**
 * @author Doston Bokhodirov, Mon 5:01 PM. 12/20/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuProcessor {
    private static final MenuProcessor instance = new MenuProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final UserMenuProcessor userMenuProcessor = UserMenuProcessor.getInstance();
    private static final AdminMenuProcessor adminMenuProcessor = AdminMenuProcessor.getInstance();
    private static final ManagerMenuProcessor managerMenuProcessor = ManagerMenuProcessor.getInstance();

    public void process(Message message, String role) {
        if (Role.USER.toString().equals(role)) {
            userMenuProcessor.process(message);
        } else if (Role.MANAGER.toString().equals(role)) {
            managerMenuProcessor.process(message);
        } else if (Role.ADMIN.toString().equals(role)) {
            adminMenuProcessor.process(message);
        }
    }

    public void menu(String chatId, String role) {
        SendMessage sendMessage = new SendMessage(chatId, "<b>" + LangConfig.get(chatId, "choose.menu") + "</b>");
        if (role.equals("USER")) {
            sendMessage.setReplyMarkup(MarkupBoard.userMenu(chatId));
        } else if (role.equals("MANAGER")) {
            sendMessage.setReplyMarkup(MarkupBoard.managerMenu(chatId));
        } else {
            sendMessage.setReplyMarkup(MarkupBoard.adminMenu(chatId));
        }
        BOT.executeMessage(sendMessage);
    }

    public void menu(String chatId, String role, String text) {
        SendMessage sendMessage = new SendMessage(chatId, text);
        if (role.equals("USER")) {
            sendMessage.setReplyMarkup(MarkupBoard.userMenu(chatId));
        } else if (role.equals("MANAGER")) {
            sendMessage.setReplyMarkup(MarkupBoard.managerMenu(chatId));
        } else {
            sendMessage.setReplyMarkup(MarkupBoard.adminMenu(chatId));
        }
        BOT.executeMessage(sendMessage);
    }

    public static MenuProcessor getInstance() {
        return instance;
    }
}
