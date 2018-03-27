package com.jr.structure.dao;

import com.jr.structure.model.Crit;
import com.jr.util.FileOps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class CritRepositoryFile implements CritRepository {
    private static List<Crit> crits;
    private static final String ID_NAME = "id";
    private static final String NAME_NAME = "name";
    private static final String MIN_NAME = "min";
    private static final String MAX_NAME = "max";
    private static final String INCLUDE_UNDEFINED_NAME = "include_undefined";


    static {
        List<Map<String, String>> allCritsMap = FileOps.getAll(FileOps.CRITS);
        crits = new ArrayList<>();

        for (Map<String, String> critMap : allCritsMap) {
            long id = Long.parseLong(critMap.get(ID_NAME));
            int min = Integer.parseInt(critMap.get(MIN_NAME));
            int max = Integer.parseInt(critMap.get(MAX_NAME));
            boolean include_undefined = Boolean.parseBoolean(critMap.get(INCLUDE_UNDEFINED_NAME));

            crits.add(new Crit(id, critMap.get(NAME_NAME), min, max, include_undefined));
        }
    }

    @Override
    public List<Crit> findAll() {
        return null;//todo
    }

    private void rewrite(List<Crit> crits) {
        //todo
    }

    //если есть уже - модифицировать
    @Override
    public List<Crit> save(Iterable<Crit> items) {
        return null;//todo
    }

    @Override
    public void delete(Iterable<Crit> items) {
        //todo
    }

}
