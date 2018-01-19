package com.jr.structure.model;

import com.jr.structure.dao.CritRepositoryFile;
import lombok.Data;
import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

@Data
public class Crit {
    private long id = CritRepositoryFile.getIdForNewCrit();
    @NonNull
    private String name;
    @NonNull
    private int min;
    @NonNull
    private int max;
    @NonNull
    private boolean includeUndefined;
}
