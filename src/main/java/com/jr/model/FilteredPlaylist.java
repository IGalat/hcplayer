package com.jr.model;

import com.jr.logic.FilterLogic;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */

@Data
@AllArgsConstructor
public class FilteredPlaylist implements Playlist {
    private long id;
    private String name;
    private Flavor flavor;
    private boolean defaultFlavorUsed;
    private Filter filter;
    private List<Song> blacklist;

    @Override
    public List<Song> getSongs() {
        return FilterLogic.getSongList(filter, blacklist);
    }

    @Override
    public boolean isFiltered() {
        return true;
    }

}
