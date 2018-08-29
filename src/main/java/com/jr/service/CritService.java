package com.jr.service;

import com.jr.dao.CritRepository;
import com.jr.dao.CritRepositoryFile;
import com.jr.logic.CritHardcode;
import com.jr.model.Crit;
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

    public static List<Crit> getAll() {
        Util.init(); //todo перекинуть куда-то в подходящее место
        return critRepo.findAll();
    }

    public static List<Crit> getByIds(List<Long> ids) {
        return critRepo.findAll(ids);
    }

    public static Crit getByName(String name) {
        return critRepo.getByName(name);
    }

    public static Crit getOne(long id) {
        return critRepo.getOne(id);
    }

    public static synchronized void remove(Crit crit) {
        if (CritHardcode.isProtectedCrit(crit.getName())) return;

        List<Crit> allCrits = getAll();
        for (int i = allCrits.size(); i > 0; i--) {
            Crit iterCrit = allCrits.get(i - 1);
            removeChild(crit, iterCrit);
        }

        critRepo.delete(crit);
    }

    public static Crit save(String name) {
        return save(name, DEFAULT_MIN, DEFAULT_MAX);
    }

    public static Crit save(String name, int min, int max) {
        return save(name, min, max, null);
    }

    public static Crit save(String name, Crit... parents) {
        Crit crit = save(name);
        for (Crit parent : parents)
            addChild(crit, parent);
        return crit;
    }

    public static synchronized Crit save(String name, int min, int max, List<Crit> children) { //todo make it impossible to change min/max without song/filter/flavor overhaul; also, force change of all children's stat
        if (CritHardcode.isProtectedCrit(name)) return getByName(name);

        Crit existingCrit = getByName(name);
        Long id = existingCrit == null ? Settings.getNextId() : existingCrit.getId();

        name = name.toLowerCase();
        if (Util.isNameBad(name))
            throw new InputMismatchException("Bad name for crit : '" + name + "'. Correct pattern: " + Util.GOOD_NAME_PATTERN);

        min = CritHardcode.critValueToBreakpoint(min);
        max = CritHardcode.critValueToBreakpoint(max);
        if (min > max)
            throw new InputMismatchException("Cannot create crit: after cropping min=" + min + ", max=" + max +
                    ". min must be less than max!");
        if (min == max || (min == 0 && max == 1)) {
            min = 1;
            max = 1;
        }

        if (children == null)
            children = new ArrayList<>();

        Crit crit = new Crit(id, name, min, max, children);
        return critRepo.save(crit);
    }

    public static synchronized Crit rename(Crit crit, String newName) { //todo find everything that uses the crit and overwrite?
        if (CritHardcode.isProtectedCrit(newName)) return getByName(newName);
        newName = newName.toLowerCase();
        if (Util.isNameBad(newName))
            throw new InputMismatchException("Cannot rename crit '" + crit.getName() + "' to '" + newName +
                    "'. Correct pattern: " + Util.GOOD_NAME_PATTERN);

        return critRepo.save(new Crit(crit.getId(), newName, crit.getMin(), crit.getMax(),  crit.getChildren()));
    }

    public static synchronized Crit save(Crit crit) {
        return save(crit.getName(), crit.getMin(), crit.getMax(), crit.getChildren());
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

        parent.getChildren().add(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.getChildren());
    }

    public static Crit removeChild(Crit child, Crit parent) {
        if (parent.getChildren().size() == 0)
            return parent;
        if (!parent.getChildren().contains(child))
            return parent;

        parent.getChildren().remove(child);
        return save(parent.getName(), parent.getMin(), parent.getMax(), parent.getChildren());
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

    public static List<Crit> getNextGenerationOf(List<Crit> crits) {
        List<Crit> generation = new ArrayList<>();
        for (Crit crit : crits) {
            generation.addAll(crit.getChildren());
        }
        return generation;
    }
}
