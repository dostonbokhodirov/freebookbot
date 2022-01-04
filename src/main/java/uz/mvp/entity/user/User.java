package uz.mvp.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Doston Bokhodirov, Sun 8:37 PM. 12/19/2021
 */
@Getter
@Setter
@Builder
public class User {
    private String id;
    private String fullName;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String language;
    private String role;
    private String userName;
    private String createdAt;
}
