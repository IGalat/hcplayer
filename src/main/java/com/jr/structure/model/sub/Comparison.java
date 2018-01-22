package com.jr.structure.model.sub;

import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Comparison {
    @NonNull
    private final int value;
    @NonNull
    private final ComparisonOption comparisonOption;
}
