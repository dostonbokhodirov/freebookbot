package uz.mvp.entity;

import lombok.*;

import java.util.Date;

/**
 * @author Doston Bokhodirov, Sun 8:37 PM. 12/19/2021
 */
@Getter
@Setter
public class Auditable implements BaseEntity {
    protected Long id;
}
