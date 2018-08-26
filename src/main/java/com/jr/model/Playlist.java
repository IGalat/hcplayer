package com.jr.model;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface Playlist {

    long getId();

    String getName();

    Flavor getFlavor();

    boolean isDefaultFlavorUsed();

    List<Song> getSongs();

    default boolean isNormal() {
        return false;
    }

    default boolean isFiltered() {
        return false;
    }

}
