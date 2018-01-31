package com.jr.structure.model;

import com.jr.util.FileOps;
import lombok.Data;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Song {
    private long id = FileOps.getId();
    private String name;
    private Map<Crit, Integer> crits;
}
