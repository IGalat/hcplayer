package com.jr.structure.model;

import com.jr.structure.model.sub.Flavor;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
interface Playlist {
    Integer FILTERED = 1;
    Integer NORMAL = 2;

    Flavor getFlavor();

    List<Long> getSongIds();

}
