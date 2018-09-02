package com.jr.execution;

import lombok.Getter;

import java.util.Observable;

/**
 * This is purely to notify when mediaPlayer needs to be updated or exception occurs
 *
 * @author Galatyuk Ilya
 */
public class ObservableForPlayer extends Observable {
    @Getter
    private static final ObservableForPlayer instance = new ObservableForPlayer();

    public void update() {
        setChanged();
        notifyObservers();
    }

}
