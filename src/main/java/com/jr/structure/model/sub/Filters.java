package com.jr.structure.model.sub;

import com.jr.structure.model.Crit;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */


public class Filters {
    private final Map<Crit, Comparison> filters = new HashMap<>(); //todo multiple filters!

    public void put(Crit crit, ComparisonOption comparisonOption, Integer value) {
        comparisonOption = checkOrFixComparisonRange(crit, comparisonOption, value);
        Comparison comparison = new Comparison(comparisonOption, value);
        filters.put(crit, comparison);
    }

    public void put(Crit crit, Comparison comparison) {
        put(crit, comparison.getComparisonOption(), comparison.getValue());
    }

    public void remove(Crit crit) {
        filters.remove(crit);
    }

    private static ComparisonOption checkOrFixComparisonRange(Crit crit, ComparisonOption option, Integer value) {
        int min = crit.getMin();
        int max = crit.getMax();
        String errorText = "Cannot add/change filter '" +
                crit.getName() + " " + option + " than " + value + "'. ";

        if (value < min || value > max)
            throw new InputMismatchException(errorText + "Value not in range (" + min + " to " + max + ")");
        else if ((value == min && option == ComparisonOption.Less)
                || (value == max && option == ComparisonOption.More))
            throw new InputMismatchException(errorText + option + " than border value of crit doesn't make sense.");
        else if ((value == min && option == ComparisonOption.LessOrEquals)
                || (value == max && option == ComparisonOption.MoreOrEquals))
            option = ComparisonOption.Equals;

        return option;
    }

    public List<Long> getFilteredSongIds() { //todo; filter out or filter in? need to expect multi-filter
        return null;
    }
}
