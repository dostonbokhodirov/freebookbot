package uz.mvp.proccessors.contact;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.configs.PConfig;
import uz.mvp.configs.State;
import uz.mvp.enums.state.ContactState;
import uz.mvp.enums.state.MenuState;

/**
 * @author Doston Bokhodirov, Wed 3:02 PM. 12/22/2021
 */
public class ContactProcessor {
    public static final ContactProcessor instance = new ContactProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();

    public void process(Message message, ContactState contactState) {
        String chatId = message.getChatId().toString();
        if (contactState.equals(ContactState.UNDEFINED)) {
            SendMessage sendMessage = new SendMessage(chatId, "Enter your message:");
            BOT.executeMessage(sendMessage);
            State.setContactState(chatId, ContactState.SENT);
        }
        else {
            String text = message.getText() + "\n\n" +
                    "From: @" + message.getFrom().getUserName();
            String adminChatId = PConfig.get("admin.chatId");
            SendMessage sendMessage = new SendMessage(adminChatId, text);
            SendMessage sendMessage1 = new SendMessage(chatId, "Message is successfully sent");
            BOT.executeMessage(sendMessage);
            BOT.executeMessage(sendMessage1);
            State.setContactState(chatId, ContactState.UNDEFINED);
            State.setMenuState(chatId, MenuState.UNDEFINED);
        }
    }

    public static ContactProcessor getInstance() {
        return instance;
    }
}
