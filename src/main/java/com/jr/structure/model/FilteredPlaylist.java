package com.jr.structure.model;

import com.jr.structure.model.sub.Filters;
import com.jr.structure.model.sub.Flavor;
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
    private Filters filters;
    private static final String TYPE = "Filtered";

    @Override
    public List<Long> getSongIds() {
        return filters.getFilteredSongIds();
    }
}
