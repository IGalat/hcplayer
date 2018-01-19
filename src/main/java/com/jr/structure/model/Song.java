package com.jr.structure.model;

import lombok.Data;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Song {
    private String name;
    private Map<Crit, Integer> crits;
}
