package uz.mvp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Doston Bokhodirov, Sat 10:29 AM. 12/25/2021
 */
@Getter
@AllArgsConstructor
public enum Language {

    UZ("uz", "Uzbek"),
    RU("ru", "Russian"),
    EN("en", "English");

    private final String code;
    private final String name;

    public static Language getByCode(String lang) {
        for (Language language : values()) {
            if (language.getCode().equalsIgnoreCase(lang)) return language;
        }
        return null;
    }
}
