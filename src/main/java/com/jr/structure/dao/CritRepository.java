package com.jr.structure.dao;

import com.jr.structure.model.Crit;

/**
 * @author Galatyuk Ilya
 */
public interface CritRepository extends CommonRepository<Crit, Long> {

    Crit getByName(String name);
}
