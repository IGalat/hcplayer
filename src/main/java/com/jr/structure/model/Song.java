package com.jr.structure.model;

import lombok.Data;
import lombok.NonNull;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Song {
    private long id;
    @NonNull
    private String name;
    @NonNull
    private Map<Crit, Integer> crits;
}
