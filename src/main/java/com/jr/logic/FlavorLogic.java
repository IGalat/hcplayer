package com.jr.logic;

import com.jr.model.Crit;
import com.jr.model.Flavor;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.CritService;
import com.jr.util.Settings;
import javafx.util.Pair;
import lombok.Getter;

import java.util.*;

/**
 * не нравится, много спец случаев: novelty, отрицательный influence - и обработка этого во многих местах
 *
 * @author Galatyuk Ilya
 */

public class FlavorLogic {
    private static final Flavor DEFAULT_DEFAULT_FLAVOR = new Flavor();
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
        Pair<List<Crit>, Double>[] critPowerMap = getCritPowerMap(flavor);
        Map<Song, Integer> weightMap = new HashMap<>();
        double initialWeight = calcInitialWeight(flavor);

        for (Song song : songs) {
            Integer weight = getSongWeight(initialWeight, song.getCrits(), critPowerMap);
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

    static Pair<List<Crit>, Double>[] getCritPowerMap(Flavor flavor) {
        List<List<List<Crit>>> critsByFlavorByGeneration = new ArrayList<>(flavor.getFlavorMap().size());
        List<Map.Entry<Crit, Integer>> flavorEntryList = new ArrayList<>(flavor.getFlavorMap().entrySet());

        for (Map.Entry<Crit, Integer> flavorEntry : flavorEntryList) {
            List<List<Crit>> flavorCritByGeneration = new ArrayList<>();
            critsByFlavorByGeneration.add(flavorCritByGeneration);

            List<Crit> generationZero = Collections.singletonList(flavorEntry.getKey());
            flavorCritByGeneration.add(generationZero);
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
                    if (critIsAbsent(crit, critsByFlavorByGeneration, currentGeneration)) {
                        atLeastOneCritAdded = true;
                    } else {
                        newGeneration.remove(crit);
                    }
                }
                if (newGeneration.size() > 0)
                    flavorCritByGeneration.add(newGeneration);
            }
            currentGeneration++;
        } while (atLeastOneCritAdded);

        Pair<List<Crit>, Double>[] critPowerMap = new Pair[flavor.getFlavorMap().size()];
        for (int i = 0; i < flavorEntryList.size(); i++) {
            List<List<Crit>> flavorCritByGeneration = critsByFlavorByGeneration.get(i);
            List<Crit> crits = new ArrayList<>();
            for (List<Crit> generation : flavorCritByGeneration)
                crits.addAll(generation);

            Pair<List<Crit>, Double> flavorCritPower = new Pair<>(crits, calcPower(flavorEntryList.get(i)));
            if (crits.get(0).getId() == CritHardcode.noveltyCrit.getId()) {
                Double noveltyInfluence = Double.valueOf(flavorEntryList.get(i).getValue());
                flavorCritPower = new Pair<>(crits, noveltyInfluence);
            }
            critPowerMap[i] = flavorCritPower;
        }
        return critPowerMap;
    }

    private static boolean critIsAbsent(Crit crit, List<List<List<Crit>>> critsByFlavorByGeneration, int upToGen) {
        for (List<List<Crit>> flavorCritByGeneration : critsByFlavorByGeneration) {
            int searchUpToGeneration = Math.min(upToGen, flavorCritByGeneration.size() - 1);
            for (int i = 0; i <= searchUpToGeneration; i++)
                if (flavorCritByGeneration.get(i).contains(crit)) return false;
        }
        return true;
    }

    private static double calcPower(Map.Entry<Crit, Integer> entry) {
        int range = normalize(entry.getKey().getMax(), entry.getKey().getMin());
        int influence = entry.getValue();
        if (range == 1) return (double) influence;

        if (influence > 0)
            return Math.log(influence) / Math.log(range); // =log of influence with base of range
        else return -Math.log(-influence) / Math.log(range);
    }

    private static int normalize(int number, int min) {
        return number - min + 1;
    }

    static int getSongWeight(double initialWeight, Map<Crit, Integer> critMap, Pair<List<Crit>, Double>[] critPowerMap) {
        double weight = initialWeight;

        for (Pair<List<Crit>, Double> singleCritHierarchy : critPowerMap) {
            double power = singleCritHierarchy.getValue();
            List<Crit> hierarchy = singleCritHierarchy.getKey();

            Crit crit = hierarchy.get(0);
            Integer value = null;
            for (Map.Entry<Crit, Integer> critAndValue : critMap.entrySet()) {
                if (hierarchy.contains(critAndValue.getKey())) {
                    value = critAndValue.getValue();
                    break;
                }
            }
            double weightOfCrit;
            if (crit.getId() == CritHardcode.noveltyCrit.getId())
                weightOfCrit = calcNoveltyWeight(value, power);
            else weightOfCrit = calcCritWeight(crit, value, power);
            weight *= weightOfCrit;
        }

        return Math.max(1, (int) Math.round(weight));
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
        double curve = 2; //maxNormalized*2 = ~4M
        double multiplier = (power - 1) / Math.pow(normalize(novelty.getMax(), novelty.getMin()), curve);

        double weight = calcCritWeight(novelty, (int) Math.round(valueD), curve);
        weight = weight * multiplier + 1;
        return weight;
    }

}
