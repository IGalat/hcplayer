package com.jr.model;

import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Crit {
    @NonNull
    private int id;
    @NonNull
    private String name;
    @NonNull
    private int min;
    @NonNull
    private int max;
    @NonNull
    private boolean includeUndefined;
}
