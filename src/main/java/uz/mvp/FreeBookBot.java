package uz.mvp;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.mvp.configs.PConfig;
import uz.mvp.handlers.UpdateHandler;
import uz.mvp.services.log.LogService;
import uz.mvp.services.authuser.AuthUserService;

import java.util.Objects;

/**
 * @author Doston Bokhodirov, Sun 8:27 PM. 12/19/2021
 */
public class FreeBookBot extends TelegramLongPollingBot {
    private static final FreeBookBot instance = new FreeBookBot();
    private static final AuthUserService authUserService = AuthUserService.getInstance();
    private static final UpdateHandler updateHandler = UpdateHandler.getInstance();
    public static LogService logService = LogService.getInstance();


    @Override
    public void onUpdateReceived(Update update) {
        logService.save(update);
        if (update.hasMessage() && 
                Objects.nonNull(update.getMessage().getText()) && update.getMessage().getText().equals("/start")) {
            authUserService.save(update.getMessage());
        }
        updateHandler.handle(update);
    }

    @Override
    public String getBotToken() {
        return PConfig.get("bot.token");
    }

    @Override
    public String getBotUsername() {
        return PConfig.get("bot.username");
    }

    public void executeMessage(BotApiMethod<?> msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(SendMessage msg) {
        msg.setParseMode("HTML");
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(SendDocument msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(SendPhoto msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(SendVideo msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(SendAudio msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void executeMessage(EditMessageText msg) {
        msg.setParseMode("HTML");
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static FreeBookBot getInstance() {
        return instance;
    }
}
