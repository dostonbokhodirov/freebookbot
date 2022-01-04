package uz.mvp.repository.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.mvp.repository.AbstractRepository;

/**
 * @author Doston Bokhodirov, Sun 9:01 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogRepository extends AbstractRepository {
    private static final LogRepository instance = new LogRepository();

    public void save(String data, String message, String chatId) {
        query.append("insert into log (data, message, chatId) values (?, ?, ?);");
        getPreparedStatement(data, message, chatId);
        executeWithout();
        close();
    }

    public static LogRepository getInstance() {
        return instance;
    }
}