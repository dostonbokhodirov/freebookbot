package uz.mvp.services.bot;

import uz.mvp.configs.LangConfig;
import uz.mvp.emojis.Emojis;

/**
 * @author Doston Bokhodirov, Sun 10:22 PM. 12/26/2021
 */
public class BotService {
    private static final BotService instance = new BotService();

    public String getMessage(String chatId) {
        return "Free Book Bot: @freebookmvpbot" + "\n\n" +
                Emojis.MAGIC + " <b>" + LangConfig.get(chatId, "magic.commands") + "</b> " + "\n\n" +
                "/start -- " + LangConfig.get(chatId, "start") + "\n" +
                "/search -- " + LangConfig.get(chatId, "search") + "\n" +
                "<i>" + LangConfig.get(chatId, "search.definition") + "</i>" + "\n" +
                "/top -- " + LangConfig.get(chatId, "top") + "\n" +
                "/stats -- " + LangConfig.get(chatId, "stats") + "\n" +
                "/help -- " + LangConfig.get(chatId, "help") + "\n" +
                "/developers -- " + LangConfig.get(chatId, "developers") + "\n" +
                "/settings -- " + LangConfig.get(chatId, "settings");
    }

    public static BotService getInstance() {
        return instance;
    }
}
