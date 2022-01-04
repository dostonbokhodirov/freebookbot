package uz.mvp.entity.book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Doston Bokhodirov, Wed 10:53 PM. 12/22/2021
 */

@Getter
@Setter
@Builder
public class Book {
    private String id;
    private String name;
    private String size;
    private String ownerId;
    private String uploadedAt;
    private Integer downloadsCount;
}
