package com.jr.logic;

import com.bpodgursky.jbool_expressions.ExprUtil;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import com.jr.model.Crit;
import com.jr.model.Filter;
import com.jr.model.Song;
import com.jr.model.sub.Comparison;
import com.jr.service.CritService;
import com.jr.service.SongService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Galatyuk Ilya
 */
public class FilterLogic {

    public static List<Song> getSongList(Filter filter, List<Song> blacklist) {
        List<Long> blacklistIds = blacklist == null ? new ArrayList<>()
                : blacklist.stream().map(Song::getId).collect(Collectors.toList());

        if (filter == null || filter.getLogicExpression().equals(""))
            return SongService.getAll().parallelStream()
                    .filter(song -> !blacklistIds.contains(song.getId())).collect(Collectors.toList());
        List<Long>[] hierarchies = getHierarchies(filter.getComparisons());
        Expression<String> logicExpression = RuleSet.simplify(ExprParser.parse(filter.getLogicExpression()));
        List<String> byWeight = ExprUtil.getConstraintsByWeight(logicExpression);
        int[] expressionIndices = getComparisonIndices(byWeight);

        return SongService.getAll().parallelStream()
                .filter(song -> !blacklistIds.contains(song.getId()))
                .filter(song -> isSongChosen(song, hierarchies, filter.getComparisons(), logicExpression, expressionIndices, byWeight))
                .collect(Collectors.toList());
    }

    private static List<Long>[] getHierarchies(Comparison[] comparisons) {
        List<Long>[] hierarchies = new List[comparisons.length];
        for (int i = 0; i < comparisons.length; i++) {
            Set<Crit> hierarchy = CritService.getAllHierarchyDown(comparisons[i].getCrit());
            hierarchies[i] = new ArrayList<>(hierarchy.size());
            for (Crit crit : hierarchy) {
                hierarchies[i].add(crit.getId());
            }
        }
        return hierarchies;
    }

    private static int[] getComparisonIndices(List<String> byWeight) {
        int[] indices = new int[byWeight.size()];
        for (int i = 0; i < indices.length; i++) {
            String f = byWeight.get(i);
            f = f.substring(1, f.length());
            indices[i] = Integer.parseInt(f);
        }
        return indices;
    }

    private static boolean isSongChosen(
            Song song
            , List<Long>[] hierarchies
            , Comparison[] comparisons
            , Expression<String> logicExpression
            , int[] expressionIndices
            , List<String> byWeight) {

        for (int i = 0; i < expressionIndices.length; i++) {
            int index = expressionIndices[i];
            boolean isComparisonOk = hasComparisonPassed(song.getCrits(), hierarchies[index], comparisons[index]);

            Map<String, Boolean> assignMap = Collections.singletonMap(byWeight.get(i), isComparisonOk);
            logicExpression = RuleSet.simplify(RuleSet.assign(logicExpression, assignMap));
            if (logicExpression.getAllK().isEmpty())
                break;
        }

        return Boolean.parseBoolean(logicExpression.toLexicographicString());
    }

    private static boolean hasComparisonPassed(Map<Crit, Integer> songCrits, List<Long> hierarchy, Comparison comparison) {
        List<Integer> values = new ArrayList<>();

        for (Map.Entry<Crit, Integer> critValue : songCrits.entrySet())
            if (hierarchy.contains(critValue.getKey().getId()))
                values.add(critValue.getValue());

        if (comparison.getValue() == null) {
            for (Integer value : values)
                if (value != null)
                    return false;
            return true;
        }

        for (Integer value : values)
            if (comparison.isOk(value))
                return true;
        return false;
    }

}
