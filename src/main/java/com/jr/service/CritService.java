package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.structure.dao.CritRepository;
import com.jr.structure.dao.CritRepositoryFile;
import com.jr.structure.model.Crit;
import com.jr.util.Settings;
import com.jr.util.Util;

import java.util.*;

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
        if (CritHardcode.isProtectedCrit(crit.getName())) return;

        List<Crit> allCrits = getAll();
        for (Crit iterCrit : allCrits)
            removeChild(crit, iterCrit);

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

    public static Crit save(String name, int min, int max, boolean whitelist, List<Crit> children) { //todo make it impossible to change min/max without song/filter/flavor overhaul; also, force change of all children's stat
        if (CritHardcode.isProtectedCrit(name)) return getByName(name);

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

        if (children == null)
            children = new ArrayList<>();

        Crit crit = new Crit(id, name, min, max, whitelist, children);
        return critRepo.save(crit);
    }

    public static Crit rename(Crit crit, String newName) {
        newName = newName.toLowerCase();
        if (Util.isNameBad(newName))
            throw new InputMismatchException("Cannot rename crit '" + crit.getName() + "' to '" + newName +
                    "'. Correct pattern: " + Util.GOOD_NAME_PATTERN);

        return critRepo.save(new Crit(crit.getId(), newName, crit.getMin(), crit.getMax(), crit.isWhitelist(), crit.getChildren()));
    }

    public static Crit addChild(Crit child, Crit parent) {
        for (Crit existingChild : parent.getChildren())
            if (existingChild.equals(child))
                return parent;
        String errorText = "Can't make '" + child.getName() + "' child of '" + parent.getName() + "'. ";
        if (CritHardcode.PROTECTED_CRITS_NAMES.contains(parent.getName())
                || CritHardcode.PROTECTED_CRITS_NAMES.contains(parent.getName()))
            throw new InputMismatchException(errorText + "Protected crit name detected.");
        if (getAllHierarchyDown(child).contains(parent))
            throw new InputMismatchException(errorText + "It's already a (possibly distant) child of " + child.getName());
        if (child.getMin() != parent.getMin())
            throw new InputMismatchException(errorText + "Child's min = " + child.getMin() + ", parent's = " + parent.getMin());
        if (child.getMax() != parent.getMax())
            throw new InputMismatchException(errorText + "Child's max = " + child.getMax() + ", parent's = " + parent.getMax());
        if (child.isWhitelist() != parent.isWhitelist())
            throw new InputMismatchException(errorText + "Child's whitelist = " + child.isWhitelist() + ", parent's = " + parent.isWhitelist());

        parent.getChildren().add(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.isWhitelist(), parent.getChildren());
    }

    public static Crit removeChild(Crit child, Crit parent) {
        if (parent.getChildren().size() == 0)
            return parent;
        if (!parent.getChildren().contains(child))
            return parent;

        parent.getChildren().remove(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.isWhitelist(), parent.getChildren());
    }

    public static Set<Crit> getAllHierarchyDown(Crit crit) {
        Set<Crit> hierarchy = new HashSet<>();
        List<Crit> generation = new ArrayList<>();
        generation.add(crit);

        while (generation.size() > 0) {
            hierarchy.addAll(generation);
            generation = getNextGenerationOf(generation);
        }

        return hierarchy;
    }

    private static List<Crit> getNextGenerationOf(List<Crit> crits) {
        List<Crit> generation = new ArrayList<>();
        for (Crit crit : crits) {
            generation.addAll(crit.getChildren());
        }
        return generation;
    }
}
