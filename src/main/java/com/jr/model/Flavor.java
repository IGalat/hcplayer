package com.jr.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@EqualsAndHashCode
public class Flavor implements Cloneable {
    @Getter
    private Map<Crit, Integer> flavorMap = new HashMap<>(); // from 1 to influence; if influence is negative - inverse

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
        getFlavorMap().put(crit, influence);
        return this;
    }

    @Override
    public Object clone() {
        Flavor clone = new Flavor();
        clone.flavorMap = new HashMap<>(this.flavorMap);
        return clone;
    }
}
