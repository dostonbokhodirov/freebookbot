package uz.mvp.handlers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Doston Bokhodirov, Sun 8:39 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateHandler {
    private static final UpdateHandler instance = new UpdateHandler();
    private final MessageHandler messageHandler = MessageHandler.getInstance();
    private final CallbackHandler callbackHandler = CallbackHandler.getInstance();
    private static final InlineHandler inlineHandler = InlineHandler.getInstance();

    public void handle(Update update) {
        if (update.hasMessage()) messageHandler.handle(update.getMessage());
        else if (update.hasCallbackQuery()) callbackHandler.handle(update.getCallbackQuery());
        else if (update.hasInlineQuery()) inlineHandler.handle(update.getInlineQuery());
    }

    public static UpdateHandler getInstance() {
        return instance;
    }
}
