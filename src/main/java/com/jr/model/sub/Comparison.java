package com.jr.model.sub;

import com.jr.model.Crit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.InputMismatchException;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class Comparison {
    @NonNull
    private final Crit crit;
    @NonNull
    private final ComparisonOption comparisonOption;
    private final Integer value;

    public static Comparison parse(String comparisonString) {
        return null; //todo, учитывать []
    }

    private static ComparisonOption checkOrFixComparison(Crit crit, ComparisonOption option, Integer value) {
        int min = crit.getMin();
        int max = crit.getMax();

        String errorText = "Cannot add/change filter '" +
                crit.getName() + " " + option + " " + value + "'. ";

        //todo min=max option: either null or !null
        if (value == null) {
            if (option == ComparisonOption.MoreOrEquals
                    || option == ComparisonOption.LessOrEquals
                    || option == ComparisonOption.Equals)
                option = ComparisonOption.Equals;
            else option = ComparisonOption.NotEquals;
        } else if (value < min || value > max)
            throw new InputMismatchException(errorText + "Value not in range (" + min + " to " + max + ")");
        else if ((value == min && option == ComparisonOption.LessThan)
                || (value == max && option == ComparisonOption.MoreThan))
            throw new InputMismatchException(errorText + option + " than border value of crit doesn't make sense.");
        else if ((value == min && option == ComparisonOption.LessOrEquals)
                || (value == max && option == ComparisonOption.MoreOrEquals))
            option = ComparisonOption.Equals;

        return option;
    }
}
