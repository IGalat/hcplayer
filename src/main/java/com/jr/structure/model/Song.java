package com.jr.structure.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class Song {
    private long id;
    private String name; // absolute_path/name.mp3
    private Map<Crit, Integer> crits;
}
