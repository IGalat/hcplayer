package com.jr.structure.model;

import com.jr.structure.model.sub.Comparison;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Playlist {
    private long id;
    private String name;
    private Map<Crit, Comparison> constraints;
}
