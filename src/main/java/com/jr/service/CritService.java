package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.structure.dao.CritRepository;
import com.jr.structure.dao.CritRepositoryFile;
import com.jr.structure.model.Crit;
import com.jr.util.Settings;

import java.util.InputMismatchException;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritService {
    private static final CritRepository critRepo = new CritRepositoryFile();
    private static final int DEFAULT_MIN = 1;
    private static final int DEFAULT_MAX = 10;
    private static final boolean DEFAULT_IS_WHITELIST = true;

    public static List<Crit> getAll() {
        return critRepo.findAll();
    }

    public static Crit getByName(String name) {
        return critRepo.getByName(name);
    }

    public static Crit getOne(long id) {
        return critRepo.getOne(id);
    }

    public static void remove(Crit crit) {
        if (CritHardcode.isStandardCrit(crit.getName())) return;
        critRepo.delete(crit);
    }

    public static Crit save(String name) {
        return save(name, DEFAULT_MIN, DEFAULT_MAX, DEFAULT_IS_WHITELIST);
    }

    public static Crit save(String name, int min, int max) {
        return save(name, min, max, DEFAULT_IS_WHITELIST);
    }

    public static Crit save(String name, boolean whitelist) {
        return save(name, DEFAULT_MIN, DEFAULT_MAX, whitelist);
    }

    public static Crit save(String name, int min, int max, boolean whitelist) {
        if (CritHardcode.isStandardCrit(name)) return getByName(name);
        min = CritHardcode.critValueToBreakpoint(min);
        max = CritHardcode.critValueToBreakpoint(max);
        if (min >= max)
            throw new InputMismatchException("Cannot create crit: after cropping min=" + min + ", max=" + max +
                    ". min must be less than max!");
        name = name.toLowerCase();

        Crit crit = new Crit(Settings.getNextId(), name, min, max, whitelist);
        return critRepo.save(crit);
    }
}
