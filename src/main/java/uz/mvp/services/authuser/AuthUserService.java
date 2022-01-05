package uz.mvp.services.authuser;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.configs.LangConfig;
import uz.mvp.emojis.Emojis;
import uz.mvp.repository.authuser.AuthUserRepository;
import uz.mvp.repository.book.BookRepository;
import uz.mvp.services.AbstractService;

/**
 * @author Doston Bokhodirov, Sun 9:05 PM. 12/19/2021
 */
public class AuthUserService extends AbstractService<AuthUserRepository> {
    private static final AuthUserService instance = new AuthUserService(AuthUserRepository.getInstance());
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();
    private static final BookRepository bookRepository = BookRepository.getInstance();

    public void save(Message message) {
        String chatId = message.getChatId().toString();
        String userName = message.getFrom().getUserName();
        authUserRepository.save(chatId);
        authUserRepository.save("user_name", userName, chatId);
    }

    private AuthUserService(AuthUserRepository repository) {
        super(repository);
    }

    public StringBuilder getStatsMessage(String chatId) {
        StringBuilder stats = new StringBuilder();
        stats.append("<code>").append(LangConfig.get(chatId, "bot.statistics")).append("</code>").append("\n\n")
                .append(Emojis.USERS).append(" ").append(LangConfig.get(chatId, "number.all.users"))
                .append(" <b>").append(authUserRepository.getUsersId().size()).append("</b>\n")
                .append(Emojis.USER).append(" ").append(LangConfig.get(chatId, "number.users.with.user"))
                .append(" <b>").append(authUserRepository.getUsersId("USER").size()).append("</b>\n")
                .append(Emojis.MANAGER).append(" ").append(LangConfig.get(chatId, "number.users.with.manager"))
                .append(" <b>").append(authUserRepository.getUsersId("MANAGER").size()).append("</b>\n")
                .append(Emojis.BOOKS).append(" ").append(LangConfig.get(chatId, "number.all.books"))
                .append(" <b>").append(bookRepository.getIdBooks().size()).append("</b>\n");
        return stats;
    }

    public static AuthUserService getInstance() {
        return instance;
    }
}
