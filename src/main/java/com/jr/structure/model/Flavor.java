package com.jr.structure.model;

import com.jr.logic.CritHardcode;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@EqualsAndHashCode
public class Flavor {

    @Getter
    private Map<Crit, Integer> flavorMap = new HashMap<>(); // from 1 to influence; if influence is negative - inverse
    @Getter
    private static final Flavor DEFAULT_FLAVOR = new Flavor(); //todo another variable - defaultFlavor; user can change it; saving it separately(in settings?)
    public static final double DETAULT_UNDEFINED_CRIT_NORMALIZED_VALUE_PERCENT = 0.6;

    static {
        DEFAULT_FLAVOR.flavorMap.put(CritHardcode.weightCrit, 10000);
        DEFAULT_FLAVOR.flavorMap.put(CritHardcode.ratingCrit, 10);
        DEFAULT_FLAVOR.flavorMap.put(CritHardcode.noveltyCrit, 10);
    }

    public int calcFlavorWeight(List<Song> songs) {

        //todo; gotta get children of crit by generation, then exclude those that are more generations afar than in other hierarchies

        return 1;
    }

    private double calcNoveltyWeight(Integer power) { //todo, как вариант, >750 делать х4, <-300 считать как -300
        // вообще нет, лучше сделать свою формулу для этого
        // хм! можно считать отрицательное значение положительным. тогда песня, которую слушают много, будет чаще
        // проигрываться, чем та, которую слушают средне
        return 1;
    }

    private static double calcPower(Map.Entry<Crit, Integer> entry) {
        int range = entry.getKey().getMax() - entry.getKey().getMin() + 1;
        int infuence = entry.getValue();
        return Math.log(infuence) / Math.log(range); // =log of influence with base of range
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Crit, Integer> entry : flavorMap.entrySet()) {
            stringBuilder.append(entry.getKey().getName()).append("'").append(entry.getValue()).append(",");
        }
        if (flavorMap.size() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
