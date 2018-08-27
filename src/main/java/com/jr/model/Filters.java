package com.jr.model;

import com.jr.model.sub.Comparison;
import com.jr.model.sub.ComparisonOption;
import com.jr.model.sub.Filter;
import com.jr.service.SongService;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

/**
 * @author Galatyuk Ilya
 */

@EqualsAndHashCode
public class Filters {
    @Getter
    private final List<Filter> filters = new ArrayList<>();

    public Filter addNewFilter() {
        Filter newFilter = new Filter();
        filters.add(newFilter);
        return newFilter;
    }

    public void removeEmptyFilters() {
        for (Filter filter : filters)
            if (filter.getFilter().isEmpty()) filters.remove(filter);
    }

    public void put(Filter filter, Crit crit, Comparison comparison) {
        put(filter, crit, comparison.getComparisonOption(), comparison.getValue());
    }

    public void put(Filter filter, Crit crit, ComparisonOption comparisonOption, Integer value) {
        comparisonOption = checkOrFixComparisonRange(crit, comparisonOption, value);
        Comparison comparison = new Comparison(comparisonOption, value);

        for (Filter iterFilter : filters)
            if (filter.equals(iterFilter)) {
                iterFilter.getFilter().put(crit, comparison);
                return;
            }
    }

    public void remove(Filter filter, Crit crit) {
        for (Filter iterFilter : filters)
            if (filter.equals(iterFilter)) {
                iterFilter.getFilter().remove(crit);
                return;
            }
    }

    private static ComparisonOption checkOrFixComparisonRange(Crit crit, ComparisonOption option, Integer value) {
        int min = crit.getMin();
        int max = crit.getMax();

        String thanOptional = (option == ComparisonOption.More || option == ComparisonOption.Less) ? " than" : "";
        String errorText = "Cannot add/change filter '" +
                crit.getName() + " " + option + thanOptional + " " + value + "'. ";

        if (value == null) {
            if (option == ComparisonOption.MoreOrEquals
                    || option == ComparisonOption.LessOrEquals
                    || option == ComparisonOption.Equals)
                option = ComparisonOption.Equals;
            else option = ComparisonOption.NotEquals;
        } else if (value < min || value > max)
            throw new InputMismatchException(errorText + "Value not in range (" + min + " to " + max + ")");
        else if ((value == min && option == ComparisonOption.Less)
                || (value == max && option == ComparisonOption.More))
            throw new InputMismatchException(errorText + option + " than border value of crit doesn't make sense.");
        else if ((value == min && option == ComparisonOption.LessOrEquals)
                || (value == max && option == ComparisonOption.MoreOrEquals))
            option = ComparisonOption.Equals;

        return option;
    }

    public List<Song> getFilteredSongs() {
        List<Song> allSongs = SongService.getAll();
        Set<Song> filteredSongsSet = new HashSet<>();

        for (Filter filter : filters)
            filteredSongsSet.addAll(getFilteredSongs(allSongs, filter));

        return new ArrayList<>(filteredSongsSet);
    }

    public Set<Song> getFilteredSongs(List<Song> songs, Filter filter) { //todo, remember about crit hierarchy
        return new HashSet<>();
    }

}
