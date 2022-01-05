package uz.mvp.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.mvp.configs.LangConfig;
import uz.mvp.emojis.Emojis;
import uz.mvp.entity.book.Book;
import uz.mvp.entity.user.User;
import uz.mvp.repository.book.BookRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Doston Bokhodirov, Sun 8:30 PM. 12/19/2021
 */
public class InlineBoard {
    public static final BookRepository bookRepository = BookRepository.getInstance();
    private static final InlineKeyboardMarkup board = new InlineKeyboardMarkup();

    public static InlineKeyboardMarkup searchButtons(String chatId) {
        InlineKeyboardButton byGenre = new InlineKeyboardButton(Emojis.GENRE + " " + LangConfig.get(chatId, "search.by.genre"));
        byGenre.setCallbackData("genre");

        InlineKeyboardButton byName = new InlineKeyboardButton(Emojis.NAME + " " + LangConfig.get(chatId, "search.by.name"));
        byName.setCallbackData("name");
        board.setKeyboard(Arrays.asList(getRow(byGenre), getRow(byName)));
        return board;
    }

    public static InlineKeyboardMarkup languageButtons() {
        InlineKeyboardButton uz = new InlineKeyboardButton(Emojis.UZ + " O'zbek");
        uz.setCallbackData("uz");

        InlineKeyboardButton ru = new InlineKeyboardButton(Emojis.RU + " Русский");
        ru.setCallbackData("ru");

        InlineKeyboardButton en = new InlineKeyboardButton(Emojis.EN + " English");
        en.setCallbackData("en");
        board.setKeyboard(Arrays.asList(getRow(uz), getRow(ru), getRow(en)));
        return board;
    }

    public static ReplyKeyboard gender(String chatId) {
        InlineKeyboardButton male = new InlineKeyboardButton(Emojis.MALE + " " + LangConfig.get(chatId, "male"));
        male.setCallbackData("male");

        InlineKeyboardButton female = new InlineKeyboardButton(Emojis.FEMALE + " " + LangConfig.get(chatId, "female"));
        female.setCallbackData("female");

        board.setKeyboard(Arrays.asList(getRow(male), getRow(female)));
        return board;
    }

    public static InlineKeyboardMarkup book(ArrayList<Book> books, Integer limit, Integer offset) {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> numberButtons = new ArrayList<>();
        List<InlineKeyboardButton> numberButtons1 = new ArrayList<>();
        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
        int i = 1;
        if (books.size() <= limit) {
            for (Book book : books) {
                InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                String id = bookRepository.getId(book.getId());
                button.setCallbackData(id);
                numberButtons.add(button);
            }
            buttons.add(numberButtons);
        } else {
            for (int j = 0; j < books.size(); j++) {
                if (j >= books.size() / 2) {
                    InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                    String id = bookRepository.getId(books.get(j).getId());
                    button.setCallbackData(id);
                    numberButtons1.add(button);
                } else {
                    InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                    String id = bookRepository.getId(books.get(j).getId());
                    button.setCallbackData(id);
                    numberButtons.add(button);
                }
            }
            buttons.add(numberButtons);
            buttons.add(numberButtons1);
        }

        List<InlineKeyboardButton> extraButtons = new ArrayList<>();
        if (offset > 0) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton(Emojis.PREVIOUS);
            prevButton.setCallbackData("prev");
            extraButtons.add(prevButton);
        }
        InlineKeyboardButton cancelButton = new InlineKeyboardButton(Emojis.REMOVE);
        cancelButton.setCallbackData("cancel");
        extraButtons.add(cancelButton);
        if (books.size() == limit) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton(Emojis.NEXT);
            nextButton.setCallbackData("next");
            extraButtons.add(nextButton);
        }
        buttons.add(extraButtons);
        board.setKeyboard(buttons);
        return board;
    }

    public static InlineKeyboardMarkup user(ArrayList<User> users, Integer offset) {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> numberButtons = new ArrayList<>();
        List<InlineKeyboardButton> numberButtons1 = new ArrayList<>();
        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
        int i = 1;
        if (users.size() <= 5) {
            for (User user : users) {
                InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                button.setCallbackData(user.getId());
                numberButtons.add(button);
            }
            buttons.add(numberButtons);
        } else {
            for (int j = 0; j < users.size(); j++) {
                if (j >= users.size() / 2) {
                    InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                    button.setCallbackData(users.get(j).getId());
                    numberButtons1.add(button);
                } else {
                    InlineKeyboardButton button = new InlineKeyboardButton(numbers.get(i++ - 1));
                    button.setCallbackData(users.get(j).getId());
                    numberButtons.add(button);
                }
            }
            buttons.add(numberButtons);
            buttons.add(numberButtons1);
        }

        List<InlineKeyboardButton> extraButtons = new ArrayList<>();
        if (offset > 0) {
            InlineKeyboardButton prevButton = new InlineKeyboardButton(Emojis.PREVIOUS);
            prevButton.setCallbackData("prev");
            extraButtons.add(prevButton);
        }
        InlineKeyboardButton cancelButton = new InlineKeyboardButton(Emojis.REMOVE);
        cancelButton.setCallbackData("cancel");
        extraButtons.add(cancelButton);
        if (users.size() == 5) {
            InlineKeyboardButton nextButton = new InlineKeyboardButton(Emojis.NEXT);
            nextButton.setCallbackData("next");
            extraButtons.add(nextButton);
        }
        buttons.add(extraButtons);
        board.setKeyboard(buttons);
        return board;
    }

    public static InlineKeyboardMarkup genreButtons(String chatId) {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton adventure = new InlineKeyboardButton(Emojis.ADVENTURE + " " +
                LangConfig.get(chatId, "genre.adventure"));
        adventure.setCallbackData("adventure");
        row1.add(adventure);
        buttons.add(row1);

        InlineKeyboardButton classic = new InlineKeyboardButton(Emojis.CLASSIC + " " +
                LangConfig.get(chatId, "genre.classic"));
        classic.setCallbackData("classic");
        row1.add(classic);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton comic = new InlineKeyboardButton(Emojis.COMIC + " " +
                LangConfig.get(chatId, "genre.comic"));
        comic.setCallbackData("comic");
        row2.add(comic);

        InlineKeyboardButton fiction = new InlineKeyboardButton(Emojis.FICTION + " " +
                LangConfig.get(chatId, "genre.fiction"));
        fiction.setCallbackData("fiction");
        row2.add(fiction);
        buttons.add(row2);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton horror = new InlineKeyboardButton(Emojis.HORROR + " " +
                LangConfig.get(chatId, "genre.horror"));
        horror.setCallbackData("horror");
        row3.add(horror);

        InlineKeyboardButton scientific = new InlineKeyboardButton(Emojis.SCIENCE + " " +
                LangConfig.get(chatId, "genre.scientific"));
        scientific.setCallbackData("scientific");
        row3.add(scientific);
        buttons.add(row3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton other = new InlineKeyboardButton(Emojis.OTHER + " " +
                LangConfig.get(chatId, "genre.other"));
        other.setCallbackData("other");
        row4.add(other);
        buttons.add(row4);

        board.setKeyboard(buttons);
        return board;
    }

    public static InlineKeyboardMarkup limitButtons() {
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton five = new InlineKeyboardButton("5️⃣");
        five.setCallbackData("five");
        row1.add(five);

        InlineKeyboardButton eight = new InlineKeyboardButton("8️⃣");
        eight.setCallbackData("eight");
        row1.add(eight);

        InlineKeyboardButton ten = new InlineKeyboardButton("\uD83D\uDD1F");
        ten.setCallbackData("ten");
        row1.add(ten);
        buttons.add(row1);

        board.setKeyboard(buttons);
        return board;
    }

    private static List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Arrays.stream(buttons).toList();
    }
}
