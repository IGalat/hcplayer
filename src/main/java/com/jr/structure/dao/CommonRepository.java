package com.jr.structure.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface CommonRepository<T, ID extends Serializable> {

    T getOne(ID id);

    List<T> findAll();

    List<T> findAll(Iterable<ID> ids);

    default T save(T item) {
        ArrayList<T> items = new ArrayList<>();
        items.add(item);
        save(items);
        return item;
    }

    List<T> save(Iterable<T> items);

    void delete(ID id);

    void delete(String name);

    void delete(Iterable<T> items);

    default void delete(T item) {
        ArrayList<T> items = new ArrayList<>();
        items.add(item);
        delete(items);
    }

    void deleteAll();

    boolean exists(ID id);

    default long count() {
        return findAll().size();
    }
}
