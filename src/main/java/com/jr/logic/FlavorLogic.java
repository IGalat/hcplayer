package com.jr.logic;

import com.jr.model.Crit;
import com.jr.model.Flavor;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.CritService;
import com.jr.util.Settings;
import lombok.Getter;

import java.util.*;

/**
 * не нравится, много спец случаев: novelty, отрицательный influence - и обработка этого во многих местах
 *
 * @author Galatyuk Ilya
 */

public class FlavorLogic {
    public static final Flavor DEFAULT_DEFAULT_FLAVOR = new Flavor();
    @Getter
    private static Flavor defaultFlavor;
    public static final double DEFAULT_UNDEFINED_CRIT_NORMALIZED_VALUE_PERCENT = 0.1;

    static {
        DEFAULT_DEFAULT_FLAVOR.getFlavorMap().put(CritHardcode.weightCrit, 10000);
        DEFAULT_DEFAULT_FLAVOR.getFlavorMap().put(CritHardcode.ratingCrit, 10);
        DEFAULT_DEFAULT_FLAVOR.getFlavorMap().put(CritHardcode.noveltyCrit, 10);

        Flavor defFlavor = Settings.getDefaultFlavor();
        if (defFlavor == null)
            defaultFlavor = (Flavor) DEFAULT_DEFAULT_FLAVOR.clone();
        else
            defaultFlavor = defFlavor;
    }

    public static void saveDefaultFlavor(Flavor newDefaultFlavor) {
        defaultFlavor = newDefaultFlavor;
        Settings.saveDefaultFlavor(defaultFlavor);
    }

    public static Map<Song, Integer> getWeightMap(Playlist playlist) {
        Flavor flavor = playlist.isDefaultFlavorUsed() ? defaultFlavor : playlist.getFlavor();
        return getWeightMap(playlist.getSongs(), flavor);
    }

    public static Map<Song, Integer> getWeightMap(List<Song> songs, Flavor flavor) {
        CritHierarchyPower[] critPowerMap = flavor.getCritPowerMap();
        if (critPowerMap == null)
            critPowerMap = getCritPowerMap(flavor);
        Map<Song, Integer> weightMap = new HashMap<>();
        double initialWeight = calcInitialWeight(flavor);

        for (Song song : songs) {
            Integer weight = getSongWeight(initialWeight, song, critPowerMap);
            weightMap.put(song, weight);
        }

        return weightMap;
    }

    //so that songs don't have billions of weight in case of flavors with a ton of crits/big influence
    private static double calcInitialWeight(Flavor flavor) {
        double initialWeight = 100000;
        for (Map.Entry<Crit, Integer> flavorEntry : flavor.getFlavorMap().entrySet()) {
            initialWeight /= Math.sqrt(flavorEntry.getValue());
        }
        return initialWeight;
    }

    private static CritHierarchyPower[] getCritPowerMap(Flavor flavor) {
        int quantityOfPrimals = flavor.getFlavorMap().size();
        List<List<List<Crit>>> critsByFlavorByGeneration = new ArrayList<>(quantityOfPrimals);
        List<Integer> influences = new ArrayList<>(quantityOfPrimals);
        List<Long> addedIds = new ArrayList<>();
        List<Long> idsToAdd = new ArrayList<>();

        for (Map.Entry<Crit, Integer> flavorEntry : flavor.getFlavorMap().entrySet()) {
            List<List<Crit>> flavorCritByGeneration = new ArrayList<>();
            critsByFlavorByGeneration.add(flavorCritByGeneration);
            List<Crit> generationZero = Collections.singletonList(flavorEntry.getKey());
            flavorCritByGeneration.add(generationZero);

            influences.add(flavorEntry.getValue());
            addedIds.add(flavorEntry.getKey().getId());
        }

        boolean atLeastOneCritAdded;
        int currentGeneration = 0;
        do {
            atLeastOneCritAdded = false;
            for (List<List<Crit>> flavorCritByGeneration : critsByFlavorByGeneration) {
                if (flavorCritByGeneration.size() < currentGeneration + 1) continue;
                List<Crit> newGeneration = CritService.getNextGenerationOf(flavorCritByGeneration.get(currentGeneration));
                for (int i = newGeneration.size() - 1; i >= 0; i--) {
                    Crit crit = newGeneration.get(i);
                    if (addedIds.contains(crit.getId())) {
                        newGeneration.remove(crit);
                    } else {
                        atLeastOneCritAdded = true;
                        idsToAdd.add(crit.getId());
                    }
                }
                if (newGeneration.size() > 0)
                    flavorCritByGeneration.add(newGeneration);
            }
            addedIds.addAll(idsToAdd);
            idsToAdd = new ArrayList<>();

            currentGeneration++;
        } while (atLeastOneCritAdded);

        CritHierarchyPower[] critHierarchyPowers = new CritHierarchyPower[quantityOfPrimals];
        for (int i = 0; i < quantityOfPrimals; i++) {
            critHierarchyPowers[i] = new CritHierarchyPower(critsByFlavorByGeneration.get(i), influences.get(i));
        }
        return critHierarchyPowers;
    }

    private static double calcPower(Crit crit, int influence) {
        int range = normalize(crit.getMax(), crit.getMin());
        if (range == 1) return (double) influence;

        if (influence > 0)
            return Math.log(influence) / Math.log(range); // =log of influence with base of range
        else return -Math.log(-influence) / Math.log(range);
    }

    private static int normalize(int number, int min) {
        return number - min + 1;
    }

    static int getSongWeight(double initialWeight, Song song, CritHierarchyPower[] critPowerMap) {
        Map<Crit, Integer> songCritMap = song.getCrits();
        double weight = initialWeight;

        for (CritHierarchyPower critHierarchyPower : critPowerMap) {
            Integer value = null;
            for (Map.Entry<Crit, Integer> critAndValue : songCritMap.entrySet()) {
                if (critHierarchyPower.hierarchyIds.contains(critAndValue.getKey().getId())) {
                    value = critAndValue.getValue();
                    break;
                }
            }
            double weightOfCrit;
            if (critHierarchyPower.crit.getId() == CritHardcode.noveltyCrit.getId())
                weightOfCrit = calcNoveltyWeight(value, critHierarchyPower.power);
            else weightOfCrit = calcCritWeight(critHierarchyPower.crit, value, critHierarchyPower.power);
            weight *= weightOfCrit;
        }

        return Math.max(1, (int) weight);
    }

    private static double calcCritWeight(Crit crit, Integer value, double power) {
        if (crit.getMin() == crit.getMax()) {
            if (value == null) return 1;
            else return power;
        }

        double normalizedValue;
        if (value == null)
            normalizedValue = Math.round(normalize(crit.getMax(), crit.getMin()) * DEFAULT_UNDEFINED_CRIT_NORMALIZED_VALUE_PERCENT) + crit.getMin();
        else normalizedValue = normalize(value, crit.getMin());

        if (power > 0)
            return Math.pow(normalizedValue, power);
        else
            return Math.pow(flipValue(crit, normalizedValue), -power);
    }

    private static double flipValue(Crit crit, Double value) {
        int min = crit.getMin();
        int max = crit.getMax();

        double diffWithMax = max - value;
        return min + diffWithMax;
    }

    private static double calcNoveltyWeight(Integer value, double power) {
        Crit novelty = CritHardcode.noveltyCrit;
        double valueD = value;

        if (power < 0) {
            valueD = flipValue(novelty, valueD);
            power = -power;
        }

        // чтобы была квадратичная зависимость даже при малых value(в отличие от обратной кв. зависимости в таких случаях):
        double curve = 2; //maxNormalized(2001)*2 = ~4M
        double multiplier = (power - 1) / Math.pow(normalize(novelty.getMax(), novelty.getMin()), curve);

        double weight = calcCritWeight(novelty, (int) Math.round(valueD), curve);
        weight = weight * multiplier + 1;
        return weight;
    }

    public static class CritHierarchyPower {
        List<Long> hierarchyIds;
        Crit crit;
        double power;

        CritHierarchyPower(List<List<Crit>> hierarchyByGenerations, Integer influence) {
            hierarchyIds = new ArrayList<>();
            for (List<Crit> generation : hierarchyByGenerations)
                for (Crit crit : generation)
                    hierarchyIds.add(crit.getId());

            crit = hierarchyByGenerations.get(0).get(0);
            if (crit.getId() == CritHardcode.noveltyCrit.getId()) {
                power = influence;
            } else {
                power = calcPower(crit, influence);
            }
        }
    }
}
