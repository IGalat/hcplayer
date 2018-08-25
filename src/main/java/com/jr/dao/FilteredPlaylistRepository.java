package com.jr.dao;

import com.jr.model.FilteredPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface FilteredPlaylistRepository extends CommonRepository<FilteredPlaylist, Long> {

    @Override
    default FilteredPlaylist getOne(Long id) {
        ArrayList<Long> oneId = new ArrayList<>();
        oneId.add(id);
        List<FilteredPlaylist> playlist = findAll(oneId);
        if (playlist.size() < 1) {
            return null;
        }
        return playlist.get(0);
    }

    @Override
    default List<FilteredPlaylist> findAll(Iterable<Long> ids) {
        ArrayList<FilteredPlaylist> playlists = new ArrayList<>();
        List<FilteredPlaylist> allPlaylists = findAll();
        for (FilteredPlaylist playlist : allPlaylists) {
            for (Long id : ids) {
                if (id.equals(playlist.getId())) {
                    playlists.add(playlist);
                    break;
                }
            }
        }
        return playlists;
    }

    default FilteredPlaylist getByName(String name) {
        List<FilteredPlaylist> playlists = findAll();
        for (FilteredPlaylist playlist : playlists) {
            if (playlist.getName().equals(name.toLowerCase())) {
                return playlist;
            }
        }
        return null;
    }

    @Override
    default void delete(Long id) {
        FilteredPlaylist playlist = getOne(id);
        if (playlist == null)
            return;

        List<FilteredPlaylist> onePlaylist = new ArrayList<>();
        onePlaylist.add(playlist);
        delete(onePlaylist);
    }

    @Override
    default void delete(String name) {
        FilteredPlaylist playlist = getByName(name);
        if (playlist == null)
            return;

        List<FilteredPlaylist> onePlaylist = new ArrayList<>();
        onePlaylist.add(playlist);
        delete(onePlaylist);
    }

    @Override
    default void deleteAll() {
        delete(findAll());
    }

    @Override
    default boolean exists(Long id) {
        return getOne(id) != null;
    }

}
