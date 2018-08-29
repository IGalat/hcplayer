package com.jr.model;

import com.jr.logic.FlavorLogic;
import com.jr.service.CritService;
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
    private transient FlavorLogic.HierarchyPower[] critPowerMap;

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
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<Crit, Integer> entry : flavorMap.entrySet()) {
            if (entry.getKey() != null)
                stringBuilder.append(entry.getKey().getName()).append("'").append(entry.getValue()).append(",");
        }
        if (flavorMap.size() > 0) stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static Flavor parse(String flavorMapString) {
        Flavor result = new Flavor();
        if (flavorMapString == null) return result;

        String[] flavors = flavorMapString.split("[,]");
        for (String flavorString : flavors) {
            String[] elements = flavorString.split("[']");
            Crit crit = CritService.getByName(elements[0]);
            Integer influence = Integer.parseInt(elements[1]);

            if (crit != null)
                result.getFlavorMap().put(crit, influence);
        }
        return result;
    }

    @Override
    public Object clone() {
        Flavor clone = new Flavor();
        clone.flavorMap = new HashMap<>(this.flavorMap);
        return clone;
    }
}
