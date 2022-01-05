package uz.mvp.services.authuser;

import org.telegram.telegrambots.meta.api.objects.Message;
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

    public static AuthUserService getInstance() {
        return instance;
    }

    public StringBuilder getStatsMessage() {
        StringBuilder stats = new StringBuilder();
        stats.append("<code>Bot statistics:</code>").append("\n\n")
                .append(Emojis.USERS)
                .append(" Number of all users:   |   <b>").append(authUserRepository.getUsersId().size())
                .append("</b>\n")
                .append(Emojis.USER)
                .append(" Number of users with <code>USER</code> role   |   <b>")
                .append(authUserRepository.getUsersId("USER").size())
                .append("</b>\n")
                .append(Emojis.MANAGER)
                .append(" Number of users with <code>MANAGER</code> role   |   <b>")
                .append(authUserRepository.getUsersId("MANAGER").size())
                .append("</b>\n")
                .append(Emojis.BOOKS)
                .append(" Number of all books   |   <b>").append(bookRepository.getIdBooks().size())
                .append("</b>\n");
        return stats;
    }
}
