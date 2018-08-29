package com.jr.model;

import com.jr.logic.FlavorLogic;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@EqualsAndHashCode
public class Flavor implements Cloneable {
    @Getter
    private Map<Crit, Integer> flavorMap = new HashMap<>(); // from 1 to influence; if influence is negative - inverse
    @Getter
    @Setter
    private transient FlavorLogic.CritHierarchyPower[] critPowerMap;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Crit, Integer> entry : flavorMap.entrySet()) {
            stringBuilder.append(entry.getKey().getName()).append("'").append(entry.getValue()).append(",");
        }
        if (flavorMap.size() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public Flavor putCritFlavor(Crit crit, Integer influence) {
        if (influence == 0 || influence == 1 || influence == -1)
            return this;

        critPowerMap = null;
        getFlavorMap().put(crit, influence);
        return this;
    }

    public Flavor removeCrit(Crit crit) {
        if (flavorMap.containsKey(crit)) {
            critPowerMap = null;
            flavorMap.remove(crit);
        }
        return this;
    }

    @Override
    public Object clone() {
        Flavor clone = new Flavor();
        clone.flavorMap = new HashMap<>(this.flavorMap);
        return clone;
    }
}
