package com.jr.structure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Crit {
    private long id;
    @NonNull
    private String name;
    private int min;
    private int max;
    private boolean includeUndefined;

    public Crit(long id, String name, int min, int max, boolean includeUndefined) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.includeUndefined = includeUndefined;
    }
}
