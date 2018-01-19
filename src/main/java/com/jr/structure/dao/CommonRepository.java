package com.jr.structure.dao;

import java.io.Serializable;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface CommonRepository<T, ID extends Serializable> {

    T getOne(ID id);

    List<T> findAll();

    List<T> findAll(Iterable<ID> id);

    T save(T item);

    List<T> save(Iterable<T> items);

    void delete(ID id);

    void delete(String name);

    void delete(Iterable<T> items);

    void delete(T item);

    void deleteAll();

    boolean exists(ID id);

    long count();
}
