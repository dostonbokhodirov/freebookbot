package uz.mvp.services.log;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.mvp.repository.log.LogRepository;
import uz.mvp.services.AbstractService;
import uz.mvp.utils.Util;

/**
 * @author Doston Bokhodirov, Sun 9:04 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogService extends AbstractService<LogRepository> {
        private static final LogService instance = new LogService(LogRepository.getInstance());

        private LogService(LogRepository repository) {
            super(repository);
        }

        public void save(Update update) {
            if (update.hasMessage()) {
                repository.save(Util.asString(update), update.getMessage().getText(), update.getMessage().getChatId().toString());
            }
            else if (update.hasCallbackQuery()){
                repository.save(Util.asString(update), update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId().toString());
            }
        }

        public static LogService getInstance() {
            return instance;
        }
}