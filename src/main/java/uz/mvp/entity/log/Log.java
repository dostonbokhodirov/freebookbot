package uz.mvp.entity.log;

import uz.mvp.entity.Auditable;

/**
 * @author Doston Bokhodirov, Sun 8:37 PM. 12/19/2021
 */
public class Log extends Auditable {
    private String data;
    private Long chatId;
    private String createdAt;
}
