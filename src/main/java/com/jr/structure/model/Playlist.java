package com.jr.structure.model;

import com.jr.structure.model.sub.Comparison;
import com.jr.util.FileOps;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Playlist {
    private long id = FileOps.getId();
    @NonNull
    private String name;
    @NonNull
    private Map<Crit, Comparison> constraints;
}
