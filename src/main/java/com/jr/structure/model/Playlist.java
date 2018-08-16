package com.jr.structure.model;

import com.jr.structure.model.sub.Flavor;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
interface Playlist {

    Flavor getFlavor();

    List<Long> getSongIds();

}
