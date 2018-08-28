package com.jr.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class Crit implements Cloneable {
    private long id;
    private String name;
    private int min;
    private int max;
    private boolean whitelist;
    private List<Crit> children;

    @Override
    public Object clone() {
        return new Crit(this.getId(), this.getName(), this.getMin(), this.getMax(), this.isWhitelist(), new ArrayList<>(this.getChildren()));
    }
}
