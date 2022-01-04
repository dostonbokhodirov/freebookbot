package uz.mvp.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.mvp.configs.LangConfig;
import uz.mvp.emojis.Emojis;

import java.util.List;

/**
 * @author Doston Bokhodirov, Sun 8:31 PM. 12/19/2021
 */
public class MarkupBoard {
    private static final ReplyKeyboardMarkup board = new ReplyKeyboardMarkup();

    public static ReplyKeyboardMarkup sharePhoneNumber(String chatId) {
        KeyboardButton phoneContact = new KeyboardButton(Emojis.PHONE + " " + LangConfig.get(chatId, "share.phone.number"));
        phoneContact.setRequestContact(true);
        board.setResizeKeyboard(true);
        board.setSelective(true);
        KeyboardRow row = new KeyboardRow();
        row.add(phoneContact);
        board.setKeyboard(List.of(row));
        return board;
    }

    public static ReplyKeyboardMarkup settingsMenu(String chatId) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Emojis.NAME + " " + LangConfig.get(chatId, "change.name")));
        row1.add(new KeyboardButton(Emojis.AGE + " " + LangConfig.get(chatId, "change.birthdate")));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Emojis.PHONE + " " + LangConfig.get(chatId, "change.phone.number")));
        row2.add(new KeyboardButton(Emojis.LANGUAGE + " " + LangConfig.get(chatId, "change.language")));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Emojis.LIMIT + " " + LangConfig.get(chatId, "change.limit")));

        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton(Emojis.BACK + " " + LangConfig.get(chatId, "back")));
        board.setKeyboard(List.of(row1, row2, row3,row4));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public static ReplyKeyboardMarkup userMenu(String chatId) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Emojis.ADD_BOOK + " " + LangConfig.get(chatId, "add.book")));
        row1.add(new KeyboardButton(Emojis.DOWNLOAD + " " + LangConfig.get(chatId, "downloaded.books")));

        board.setKeyboard(List.of(row1));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public static ReplyKeyboardMarkup managerMenu(String chatId) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Emojis.ADD_BOOK + " " + LangConfig.get(chatId, "add.book")));
        row1.add(new KeyboardButton(Emojis.REMOVE_BOOK + " " + LangConfig.get(chatId, "remove.book")));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Emojis.DOWNLOAD + " " + LangConfig.get(chatId, "downloaded.books")));
        row2.add(new KeyboardButton(Emojis.UPLOAD + " " + LangConfig.get(chatId, "uploaded.books")));

        board.setKeyboard(List.of(row1, row2));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }

    public static ReplyKeyboardMarkup adminMenu(String chatId) {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(Emojis.ADD + " " + LangConfig.get(chatId, "add.manager")));
        row1.add(new KeyboardButton(Emojis.REMOVE + " " + LangConfig.get(chatId, "remove.manager")));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(Emojis.ADD_BOOK + " " + LangConfig.get(chatId, "add.book")));
        row2.add(new KeyboardButton(Emojis.REMOVE_BOOK + " " + LangConfig.get(chatId, "remove.book")));

        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(Emojis.DOWNLOAD + " " + LangConfig.get(chatId, "downloaded.books")));
        row3.add(new KeyboardButton(Emojis.UPLOAD + " " + LangConfig.get(chatId, "uploaded.books")));

        board.setKeyboard(List.of(row1, row2, row3));
        board.setResizeKeyboard(true);
        board.setSelective(true);
        return board;
    }
}
