package com.jr.structure.model;

import lombok.NonNull;

/**
 * @author Galatyuk Ilya
 */

public class Crit {
    private long id;
    @NonNull
    private String name;
    private int min;
    private int max;
    private boolean includeUndefined;

    public Crit() {
    }

    public Crit(long id, String name, int min, int max, boolean includeUndefined) {
        this.id = id;
        this.name = name;
        this.min = min;
        this.max = max;
        this.includeUndefined = includeUndefined;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isIncludeUndefined() {
        return includeUndefined;
    }

    public void setIncludeUndefined(boolean includeUndefined) {
        this.includeUndefined = includeUndefined;
    }
}
