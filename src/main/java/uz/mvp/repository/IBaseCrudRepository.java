package uz.mvp.repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Doston Bokhodirov, Sun 9:00 PM. 12/19/2021
 */
public interface IBaseCrudRepository<E, ID> {
    default void save(E e) {
    }

    default Optional<E> get(ID id) {
        return Optional.empty();
    }
    default Optional<List<E>> getAll() {
        return Optional.empty();
    }

    default void update(E e) {

    }

    default void delete(ID id) {
    }
}
