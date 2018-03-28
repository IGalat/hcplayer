package com.jr.structure.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class Crit {
    private long id;
    private String name;
    private int min;
    private int max;
    private boolean includeUndefined;
}
