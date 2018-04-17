package com.jr.structure.model.sub;

import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Comparison {
    private final int value;
    @NonNull
    private final ComparisonOption comparisonOption;

    public Comparison(int value, ComparisonOption comparisonOption) {
        this.value = value;
        this.comparisonOption = comparisonOption;
    }
}
