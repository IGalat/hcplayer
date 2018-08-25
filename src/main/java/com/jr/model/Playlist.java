package com.jr.model;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
interface Playlist {

    long getId();

    String getName();

    Flavor getFlavor();

    List<Song> getSongs();

    default boolean isNormal() {
        return false;
    }

    default boolean isFiltered() {
        return false;
    }

}
