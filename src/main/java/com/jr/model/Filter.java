package com.jr.model;

import com.jr.model.sub.Comparison;
import lombok.Data;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Stack;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Filter {
    private final String logicExpression;
    private final Comparison[] comparisons;

    public Filter(String logicExpression, List<Comparison> comparisons) {
        this.logicExpression = checkLogicExpression(logicExpression);
        this.comparisons = comparisons.toArray(new Comparison[comparisons.size()]);
    }

    public Filter(String logicExpression, Comparison... comparisons) {
        this.logicExpression = checkLogicExpression(logicExpression);
        this.comparisons = comparisons;
    }

    public Filter(String filterString) {
        if (filterString == null || filterString.isEmpty()) {
            this.logicExpression = "";
            this.comparisons = new Comparison[0];
            return;
        }
        List<Comparison> comparisons = new ArrayList<>();

        while (filterString.contains("]")) {
            int nextComparisonIndex = comparisons.size();
            int openBracePosition = filterString.indexOf('[');
            int closeBracePosition = filterString.indexOf(']');
            String comparisonString = filterString.substring(openBracePosition, closeBracePosition + 1);

            Comparison comparison = Comparison.parse(comparisonString);
            filterString = filterString.replace(comparisonString, "f" + nextComparisonIndex);
            comparisons.add(comparison);
        }

        this.logicExpression = checkLogicExpression(filterString);
        this.comparisons = comparisons.toArray(new Comparison[comparisons.size()]);
    }

    private String checkLogicExpression(String logicExpression) throws InputMismatchException {
        if (!isLogicExpressionOk(logicExpression))
            throw new InputMismatchException("Incorrect logic expression: " + logicExpression);
        return logicExpression;
    }

    private boolean isLogicExpressionOk(String logicExpression) {
        if (!logicExpression.matches("[^]}\\[{]*")) return false;

        Stack<Character> stack = new Stack<>();

        char c;
        for (int i = 0; i < logicExpression.length(); i++) {
            c = logicExpression.charAt(i);

            if (c == '(')
                stack.push(c);
            else if (c == ')')
                if (stack.empty())
                    return false;
                else if (stack.peek() == '(')
                    stack.pop();
                else
                    return false;
        }
        return stack.empty();
    }

    @Override
    public String toString() {
        String result = logicExpression;
        for (int i = 0; i < comparisons.length; i++) {
            String compString = comparisons[i].toString();
            result = result.replace("f" + i, compString);
        }
        return result;
    }
}
