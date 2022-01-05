package uz.mvp.proccessors.post;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.mvp.FreeBookBot;
import uz.mvp.configs.State;
import uz.mvp.enums.state.MenuState;
import uz.mvp.repository.authuser.AuthUserRepository;

import java.util.ArrayList;

/**
 * @author Doston Bokhodirov, Thu 12:32 AM. 1/6/2022
 */
public class PostProcessor {
    private static final PostProcessor instance = new PostProcessor();
    private static final FreeBookBot BOT = FreeBookBot.getInstance();
    private static final AuthUserRepository authUserRepository = AuthUserRepository.getInstance();

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        ArrayList<Integer> usersId = authUserRepository.getUsersId("USER");
        if (message.hasPhoto()) {
            for (Integer id : usersId) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(id.toString());
                InputFile photo = new InputFile(message.getPhoto().get(0).getFileId());
                sendPhoto.setPhoto(photo);
                sendPhoto.setCaption(message.getCaption());
                BOT.executeMessage(sendPhoto);
            }
        } else if (message.hasVideo()) {
            for (Integer id : usersId) {
                SendVideo sendVideo = new SendVideo();
                sendVideo.setChatId(id.toString());
                InputFile video = new InputFile(message.getVideo().getFileId());
                sendVideo.setVideo(video);
                sendVideo.setCaption(message.getCaption());
                BOT.executeMessage(sendVideo);
            }
        } else if (message.hasAudio()) {
            for (Integer id : usersId) {
                SendAudio sendAudio = new SendAudio();
                sendAudio.setChatId(id.toString());
                InputFile audio = new InputFile(message.getAudio().getFileId());
                sendAudio.setAudio(audio);
                sendAudio.setCaption(message.getCaption());
                BOT.executeMessage(sendAudio);
            }
        } else if (message.hasDocument()) {
            for (Integer id : usersId) {
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(id.toString());
                InputFile document = new InputFile(message.getDocument().getFileId());
                sendDocument.setDocument(document);
                sendDocument.setCaption(message.getCaption());
                BOT.executeMessage(sendDocument);
            }
        }
        State.setMenuState(chatId, MenuState.UNDEFINED);
    }

    public static PostProcessor getInstance() {
        return instance;
    }
}
