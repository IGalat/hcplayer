package com.jr.dao;

import com.jr.model.Crit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class CritDAO {
    private static final List<Crit> crits = new ArrayList<>();
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_MAX = 10;
    public static final boolean DEFAULT_INCLUDE_UNDEFINED = false;
    private static final int[] breakpoints = new int[]{0, 1, 5, 10, 20, 50, 100, 200, 500, 1000, 10000};
    private static int maxId = 0;

    static {
        //todo extract crits from persistent storage

        for (Crit crit : crits)
            if (maxId < crit.getId())
                maxId = crit.getId();
    }

    public static List<Crit> getAll() {
        return crits;
    }

    public static Crit get(String name) {
        for (Crit crit : crits)
            if (crit.getName().equals(name))
                return crit;
        return null;
    }

    public static Crit get(int id) {
        for (Crit crit : crits)
            if (crit.getId() == id)
                return crit;
        return null;
    }

    public static boolean remove(Crit crit) {
        return crits.remove(crit);
    }

    public static boolean add(String name) {
        return add(name, DEFAULT_MIN, DEFAULT_MAX);
    }

    public static boolean add(String name, int min, int max) {
        return add(name, min, max, DEFAULT_INCLUDE_UNDEFINED);
    }

    public static boolean add(String name, int min, int max, boolean includeUndefined) {
        if (CritDAO.get(name) != null)
            return false;

        int minValue = valueToBreakpoint(min);
        int maxValue = valueToBreakpoint(max);

        Crit crit = new Crit(++maxId, name, minValue, maxValue, includeUndefined);
        crits.add(crit);
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
