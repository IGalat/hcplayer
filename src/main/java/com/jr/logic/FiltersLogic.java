package com.jr.logic;

import com.jr.model.Filter;
import com.jr.model.Song;
import com.jr.util.Defaults;
import com.jr.util.Settings;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class FiltersLogic {

    public static List<Song> getSongList(Filter filter) {
        List<Song> cached = filter.getCachedSongList();
        if (cached != null) return cached;

        return getFreshSongList(filter);
    }

    public static List<Song> getFreshSongList(Filter filter) {
        List<Song> songs = null; //todo

        if (Defaults.USE_FILTER_CACHE)
            filter.setCachedSongList(songs);
        return songs;
    }

}
