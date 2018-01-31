package com.jr.structure.model;

import com.jr.util.FileOps;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Crit {
    private long id = FileOps.getId();
    @NonNull
    private String name;
    @NonNull
    private int min;
    @NonNull
    private int max;
    @NonNull
    private boolean includeUndefined;
}
