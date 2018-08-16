package com.jr.structure.dao;

import com.jr.logic.CritHardcode;
import com.jr.structure.model.Crit;
import com.jr.util.FileOps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class CritRepositoryFile implements CritRepository {
    private static final String ID_NAME = "id";
    private static final String NAME_NAME = "name";
    private static final String MIN_NAME = "min";
    private static final String MAX_NAME = "max";
    private static final String WHITELIST_NAME = "whitelist";
    private static List<Crit> crits;

    static {
        List<Map<String, String>> allCritsMap = FileOps.getAll(FileOps.getCritsName());
        crits = new ArrayList<>();

        for (Map<String, String> critMap : allCritsMap) {
            long id = Long.parseLong(critMap.get(ID_NAME));
            int min = Integer.parseInt(critMap.get(MIN_NAME));
            int max = Integer.parseInt(critMap.get(MAX_NAME));
            boolean include_undefined = Boolean.parseBoolean(critMap.get(WHITELIST_NAME));

            crits.add(new Crit(id, critMap.get(NAME_NAME), min, max, include_undefined));
        }
    }

    @Override
    public List<Crit> findAll() {
        CritHardcode.saveStandardCrits();
        return crits;
    }

    @Override
    public synchronized List<Crit> save(Iterable<Crit> items) {
        for (Crit critToSave : items) {
            for (int i = crits.size(); i > 0; i--) {
                Crit crit = crits.get(i - 1);
                if (critToSave.getId() == crit.getId()
                        || critToSave.getName().equals(crit.getName())) {
                    critToSave.setId(crit.getId());
                    crits.remove(crit);
                }
            }
            crits.add(critToSave);
        }
        rewriteFile();
        return (List<Crit>) items;
    }

    @Override
    public synchronized void delete(Iterable<Crit> items) {
        for (Crit critToDelete : items) {
            crits.remove(critToDelete);
        }
        rewriteFile();
    }

    private synchronized void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (Crit crit : crits) {
            Map<String, String> mapOfCrit = new HashMap<>();

            mapOfCrit.put(ID_NAME, Long.toString(crit.getId()));
            mapOfCrit.put(NAME_NAME, crit.getName());
            mapOfCrit.put(MIN_NAME, Integer.toString(crit.getMin()));
            mapOfCrit.put(MAX_NAME, Integer.toString(crit.getMax()));
            mapOfCrit.put(WHITELIST_NAME, Boolean.toString(crit.isWhitelist()));

            listToSave.add(mapOfCrit);
        }

        FileOps.put(FileOps.getCritsName(), listToSave, false);
    }
}
