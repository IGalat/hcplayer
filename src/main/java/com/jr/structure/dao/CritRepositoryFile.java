package com.jr.structure.dao;

import com.jr.structure.model.Crit;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritRepositoryFile implements CritRepository { //todo

    @Override
    public Crit getOne(Long aLong) {
        return null;
    }

    @Override
    public List<Crit> findAll() {
        return null;
    }

    @Override
    public List<Crit> findAll(Iterable<Long> id) {
        return null;
    }

    public Crit getByName(String name) {
        return null;
    }

    //если есть такой уже - модифицировать
    @Override
    public Crit save(Crit item) {
        return null;
    }

    @Override
    public List<Crit> save(Iterable<Crit> items) {
        return null;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(String name) {

    }

    @Override
    public void delete(Iterable<Crit> items) {

    }

    @Override
    public void delete(Crit item) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }
}
