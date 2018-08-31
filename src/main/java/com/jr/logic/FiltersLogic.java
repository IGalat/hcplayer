package com.jr.logic;

import com.jr.model.FilteredPlaylist;
import com.jr.model.Song;

import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public class FiltersLogic {

    public static List<Song> getSongList(FilteredPlaylist playlist) {
        List<Song> cached = playlist.getFilter().getCachedSongList();
        if (cached != null) return cached;

        return getFreshSongList(playlist);
    }

    public static List<Song> getFreshSongList(FilteredPlaylist playlist) {
        return null; //todo
    }

}
