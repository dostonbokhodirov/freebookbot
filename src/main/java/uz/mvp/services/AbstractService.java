package uz.mvp.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Doston Bokhodirov, Sun 9:03 PM. 12/19/2021
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractService<R> {
    protected R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }
}
