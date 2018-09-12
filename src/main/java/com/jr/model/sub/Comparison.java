package com.jr.model.sub;

import com.jr.model.Crit;
import com.jr.service.CritService;
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
        if (comparisonString == null || comparisonString.length() < 7)
            return null;
        comparisonString = comparisonString.substring(1, comparisonString.length() - 1); //cut []
        int lastSpace = comparisonString.lastIndexOf(" ");
        int preLastSpace = comparisonString.substring(0, lastSpace).lastIndexOf(" ");

        Integer value = null;
        String valueString = comparisonString.substring(lastSpace + 1, comparisonString.length());
        if (!valueString.equals("null"))
            value = Integer.parseInt(valueString);

        String critName = comparisonString.substring(0, preLastSpace);
        Crit crit = CritService.getByName(critName);

        ComparisonOption comparisonOption = ComparisonOption.parse(comparisonString.substring(preLastSpace + 1, lastSpace));
        comparisonOption = checkOrFixComparison(crit, comparisonOption, value);

        return new Comparison(crit, comparisonOption, value);
    }

    private static ComparisonOption checkOrFixComparison(Crit crit, ComparisonOption option, Integer value) {
        int min = crit.getMin();
        int max = crit.getMax();

        String errorText = "Cannot add/change filter '" +
                crit.getName() + " " + option + " " + value + "'. ";

        if (min == max) {
            if (option == ComparisonOption.MoreOrEquals || option == ComparisonOption.LessOrEquals)
                option = ComparisonOption.Equals;
            else if (option == ComparisonOption.MoreThan || option == ComparisonOption.LessThan)
                option = ComparisonOption.NotEquals;
        } else if (value == null) {
            if (option == ComparisonOption.MoreOrEquals
                    || option == ComparisonOption.LessOrEquals
                    || option == ComparisonOption.Equals)
                option = ComparisonOption.Equals;
            else option = ComparisonOption.NotEquals;
        } else if (value < min || value > max)
            throw new InputMismatchException(errorText + "Value not in range (" + min + " to " + max + ")");
        else if ((value == min && option == ComparisonOption.LessThan)
                || (value == max && option == ComparisonOption.MoreThan))
            throw new InputMismatchException(errorText + option + " border value of crit doesn't make sense.");
        else if ((value == min && option == ComparisonOption.LessOrEquals)
                || (value == max && option == ComparisonOption.MoreOrEquals))
            option = ComparisonOption.Equals;

        return option;
    }

    public boolean isOk(Integer variable) {
        return comparisonOption.isOk(variable, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append(crit.getName());
        sb.append(" ").append(comparisonOption);
        sb.append(" ").append(value);
        sb.append(']');
        return sb.toString();
    }
}
