package com.jr.logic;

import com.jr.service.CritService;
import com.jr.structure.model.Crit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class CritHardcode {
    private static final int[] breakpointsOfCrit = new int[]{0, 1, 5, 10, 20, 50, 100, 200, 500, 1000, 10000};

    public static final String RATING_CRIT_NAME = "rating";
    public static final String NOVELTY_CRIT_NAME = "novelty";
    public static final String WEIGHT_CRIT_NAME = "weight";
    public static final List<String> PROTECTED_CRITS_NAMES = new ArrayList<>();
    public static final Integer DEFAULT_WEIGHT_CRIT = 100;
    public static final Crit ratingCrit = CritService.getByName(RATING_CRIT_NAME);
    public static final Crit noveltyCrit = CritService.getByName(NOVELTY_CRIT_NAME);
    public static final Crit weightCrit = CritService.getByName(WEIGHT_CRIT_NAME);

    private static boolean saveStandardCritsCalled = false;
    private static boolean standardCritsSaved = false;

    static {
        PROTECTED_CRITS_NAMES.add(RATING_CRIT_NAME);
        PROTECTED_CRITS_NAMES.add(NOVELTY_CRIT_NAME);
        PROTECTED_CRITS_NAMES.add(WEIGHT_CRIT_NAME);
        PROTECTED_CRITS_NAMES.add("children");
    }


    public static int critValueToBreakpoint(int value) {
        int result = value;
        boolean negative = value < 0;
        if (negative)
            value = -value;

        for (int point : breakpointsOfCrit) {
            if (value >= point)
                result = point;
            else break;
        }

        if (negative)
            result = -result;

        return result;
    }

    public static void saveStandardCrits() {
        if (saveStandardCritsCalled) return;
        saveStandardCritsCalled = true;
        CritService.save(RATING_CRIT_NAME, false);
        CritService.save(NOVELTY_CRIT_NAME, -1000, 1000, false);
        CritService.save(WEIGHT_CRIT_NAME, 1, 10000, false);
        standardCritsSaved = true;
    }

    public static Map<Crit, Integer> addStandardCritsToSongIfAbsent(Map<Crit, Integer> crits) {
        if (crits == null) crits = new HashMap<>();
        if (!crits.containsKey(ratingCrit)) crits.put(ratingCrit, null);
        if (!crits.containsKey(noveltyCrit)) crits.put(noveltyCrit, noveltyCrit.getMax());
        if (!crits.containsKey(weightCrit)) crits.put(weightCrit, DEFAULT_WEIGHT_CRIT);
        return crits;
    }

    public static boolean isStandardCrit(String name) {
        if (!standardCritsSaved) return false;
        if (PROTECTED_CRITS_NAMES.contains(name)) return true;
        return false;
    }
}
