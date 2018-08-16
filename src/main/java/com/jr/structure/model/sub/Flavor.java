package com.jr.structure.model.sub;

import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import lombok.Data;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Flavor {
    private static final int DEFAULT_WEIGHT = 1;
    private Map<Crit, ValueWeightMap> flavorMap; //todo to remake or not to remake for simpler dependency, 2x power=2x weight?

    //this->logic
    public int calcFlavorWeight(Song song) {
        int weight = DEFAULT_WEIGHT;

        for (Map.Entry<Crit, Integer> entry : song.getCrits().entrySet()) {
            ValueWeightMap critMap = flavorMap.get(entry.getKey());
            if (critMap == null) continue;
            weight *= critMap.calcWeightByValue(entry.getValue());
        }
        return weight;
    }
}
