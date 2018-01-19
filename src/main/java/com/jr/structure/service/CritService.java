package com.jr.structure.service;

import com.jr.structure.dao.CritRepository;
import com.jr.structure.dao.CritRepositoryFile;
import com.jr.structure.model.Crit;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritService {
    private static final CritRepository critRepo = new CritRepositoryFile();
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 10;
    public static final boolean DEFAULT_INCLUDE_UNDEFINED = false;
    private static final int[] breakpoints = new int[]{0, 1, 5, 10, 20, 50, 100, 200, 500, 1000, 10000};

    public static List<Crit> findAll() {
        return critRepo.findAll();
    }

    public static Crit getByName(String name) {
        return critRepo.getByName(name);
    }

    public static Crit getOne(long id) {
        return critRepo.getOne(id);
    }

    public static void remove(Crit crit) {
        critRepo.delete(crit);
    }

    public static boolean save(String name) {
        return save(name, DEFAULT_MIN, DEFAULT_MAX);
    }

    public static boolean save(String name, int min, int max) {
        return save(name, min, max, DEFAULT_INCLUDE_UNDEFINED);
    }

    public static boolean save(String name, int min, int max, boolean includeUndefined) {
        if (getByName(name) != null)
            return false;

        int minValue = valueToBreakpoint(min);
        int maxValue = valueToBreakpoint(max);

        Crit crit = new Crit(name, minValue, maxValue, includeUndefined);
        critRepo.save(crit);
        return true;
    }

    private static int valueToBreakpoint(int value) {
        int result = value;
        boolean negative = value < 0;
        if (negative)
            value = -value;

        for (int point : breakpoints) {
            if (value >= point)
                result = point;
            else break;
        }

        if (negative)
            result = -result;

        return result;
    }
}
