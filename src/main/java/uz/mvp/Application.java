package uz.mvp;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author Doston Bokhodirov, Sun 8:26 PM. 12/19/2021
 */
public class Application {
    public static void main(String[] args) {
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(FreeBookBot.getInstance());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
