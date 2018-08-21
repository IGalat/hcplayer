package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.structure.dao.CritRepository;
import com.jr.structure.dao.CritRepositoryFile;
import com.jr.structure.model.Crit;
import com.jr.util.Settings;
import com.jr.util.Util;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritService {
    private static final CritRepository critRepo = new CritRepositoryFile();
    public static final int DEFAULT_MIN = 1;
    public static final int DEFAULT_MAX = 10;
    public static final boolean DEFAULT_IS_WHITELIST = true;

    public static List<Crit> getAll() {
        Util.init(); //todo перекинуть куда-то в подходящее место
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
        return save(name, min, max, whitelist, null);
    }

    public static Crit save(String name, int min, int max, boolean whitelist, List<Crit> children) {
        if (CritHardcode.isStandardCrit(name)) return getByName(name);

        min = CritHardcode.critValueToBreakpoint(min);
        max = CritHardcode.critValueToBreakpoint(max);
        if (min >= max)
            throw new InputMismatchException("Cannot create crit: after cropping min=" + min + ", max=" + max +
                    ". min must be less than max!");

        name = name.toLowerCase();
        if (Util.isNameBad(name))
            throw new InputMismatchException("Bad name for crit : '" + name + "'. Correct pattern: " + Util.GOOD_NAME_PATTERN);

        Crit existingCrit = getByName(name);
        Long id = existingCrit == null ? Settings.getNextId() : existingCrit.getId();

        Crit crit = new Crit(id, name, min, max, whitelist, children);
        return critRepo.save(crit);
    }

    public static Crit addChild(Crit parent, Crit child) {
        if (parent.getChildren() == null)
            parent.setChildren(new ArrayList<>());
        for (Crit existingChild : parent.getChildren())
            if (existingChild.equals(child))
                return parent;

        parent.getChildren().add(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.isWhitelist(), parent.getChildren());
    }

    public static Crit removeChild(Crit parent, Crit child) {
        if (parent.getChildren() == null || parent.getChildren().size() == 0)
            return parent;
        parent.getChildren().remove(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.isWhitelist(), parent.getChildren());
    }
}
