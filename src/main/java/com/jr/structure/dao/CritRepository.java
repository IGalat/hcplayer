package com.jr.structure.dao;

import com.jr.structure.model.Crit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface CritRepository extends CommonRepository<Crit, Long> {

    @Override
    default Crit getOne(Long id) {
        ArrayList<Long> oneId = new ArrayList<>();
        oneId.add(id);
        List<Crit> crit = findAll(oneId);
        if (crit.size() < 1) {
            return null;
        }
        return crit.get(0);
    }

    @Override
    default List<Crit> findAll(Iterable<Long> ids) {
        ArrayList<Crit> crits = new ArrayList<>();
        List<Crit> allCrits = findAll();
        for (Crit crit : allCrits) {
            for (Long id : ids) {
                if (id.equals(crit.getId())) {
                    crits.add(crit);
                    break;
                }
            }
        }
        return crits;
    }

    default Crit getByName(String name) {
        List<Crit> crits = findAll();
        for (Crit crit : crits) {
            if (crit.getName().equals(name)) {
                return crit;
            }
        }
        return null;
    }

    @Override
    default void delete(Long id) {
        Crit crit = getOne(id);
        if (crit == null)
            return;

        List<Crit> oneCrit = new ArrayList<>();
        oneCrit.add(crit);
        delete(oneCrit);
    }

    @Override
    default void delete(String name) {
        Crit crit = getByName(name);
        if (crit == null)
            return;

        List<Crit> oneCrit = new ArrayList<>();
        oneCrit.add(crit);
        delete(oneCrit);
    }


    @Override
    default void deleteAll() {
        delete(findAll());
    }

    @Override
    default boolean exists(Long id) {
        return getOne(id) != null;
    }
}
