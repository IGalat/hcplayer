package com.jr.model.sub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class Comparison {
    @NonNull
    private final ComparisonOption comparisonOption;
    private final Integer value;
}
