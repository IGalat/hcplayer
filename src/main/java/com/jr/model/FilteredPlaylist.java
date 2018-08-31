package com.jr.model;

import lombok.Data;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */

@Data
public class FilteredPlaylist implements Playlist {
    private long id;
    private String name;
    private Flavor flavor;
    private boolean defaultFlavorUsed;
    private Filter filter;


    @Override
    public List<Song> getSongs() {
        return filter.getFilteredSongs();
    }

    @Override
    public boolean isFiltered(){
        return true;
    }

}
