package uz.mvp.proccessors.book;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.mvp.FreeBookBot;
import uz.mvp.buttons.InlineBoard;
import uz.mvp.configs.LangConfig;
import uz.mvp.configs.State;
import uz.mvp.configs.add.BookId;
import uz.mvp.emojis.Emojis;
import uz.mvp.enums.state.AddBookState;
import uz.mvp.enums.state.MenuState;
import uz.mvp.proccessors.menu.MenuProcessor;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.repository.book.BookRepository;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Wed 4:39 PM. 12/22/2021
 */
public class AddBookProcessor {
    private static final AddBookProcessor instance = new AddBookProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final BookId bookId = BookId.getInstance();
    private static final MenuProcessor menuProcessor = MenuProcessor.getInstance();


    public void process(Message message, AddBookState addBookState, String role) {
        String chatId = message.getChatId().toString();
        if (role.equals("USER")) {
            if (addBookState.equals(AddBookState.UNDEFINED)) {
                SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "upload.file"));
                BOT.executeMessage(sendMessage);
                State.setAddBookState(chatId, AddBookState.FILE);
            } else if (addBookState.equals(AddBookState.FILE)) {
                if (message.hasDocument()) {
                    SendMessage sendMessage = new SendMessage(chatId,
                            Emojis.ADD + " " + LangConfig.get(chatId, "file.sent"));
                    BOT.executeMessage(sendMessage);
                    menuProcessor.menu(chatId, role);
                    State.setAddBookState(chatId, AddBookState.UNDEFINED);
                    State.setMenuState(chatId, MenuState.UNDEFINED);
                    ArrayList<Integer> list = authUserRepository.getUsersId("MANAGER");
                    int i = (int) (Math.random() * (list.size() + 1));
                    InputFile inputFile = new InputFile(message.getDocument().getFileId());
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(list.get(i).toString());
                    sendDocument.setDocument(inputFile);
                    BOT.executeMessage(sendDocument);
                } else {
                    SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "upload.file.again"));
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    BOT.executeMessage(sendMessage);
                }
            }
        } else {
            if (addBookState.equals(AddBookState.UNDEFINED)) {
                SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "upload.file"));
                BOT.executeMessage(sendMessage);
                State.setAddBookState(chatId, AddBookState.FILE);
            } else if (addBookState.equals(AddBookState.FILE)) {
                if (message.hasDocument()) {
                    String fileId = message.getDocument().getFileId();
                    bookId.setBookId(chatId, fileId);
                    bookRepository.save(fileId);
                    String fileSize = message.getDocument().getFileSize().toString();
                    String fileName = message.getDocument().getFileName();
                    bookRepository.save("size", fileSize, fileId);
                    bookRepository.save("owner_id", chatId, fileId);
                    bookRepository.save("name", fileName, fileId);
                    bookRepository.save("downloads_count", 0, fileId);
                    SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "choose.genre"));
                    sendMessage.setReplyMarkup(InlineBoard.genreButtons(chatId));
                    BOT.executeMessage(sendMessage);
                    State.setAddBookState(chatId, AddBookState.GENRE);
                } else {
                    SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "upload.file.again"));
                    sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                    BOT.executeMessage(sendMessage);
                }
            }
        }
    }

    public static AddBookProcessor getInstance() {
        return instance;
    }
}
