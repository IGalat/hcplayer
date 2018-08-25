package com.jr.structure.dao;

import com.jr.structure.model.NormalPlaylist;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface NormalPlaylistRepository extends CommonRepository<NormalPlaylist, Long> {

    @Override
    default NormalPlaylist getOne(Long id) {
        ArrayList<Long> oneId = new ArrayList<>();
        oneId.add(id);
        List<NormalPlaylist> playlist = findAll(oneId);
        if (playlist.size() < 1) {
            return null;
        }
        return playlist.get(0);
    }

    @Override
    default List<NormalPlaylist> findAll(Iterable<Long> ids) {
        ArrayList<NormalPlaylist> playlists = new ArrayList<>();
        List<NormalPlaylist> allPlaylists = findAll();
        for (NormalPlaylist playlist : allPlaylists) {
            for (Long id : ids) {
                if (id.equals(playlist.getId())) {
                    playlists.add(playlist);
                    break;
                }
            }
        }
        return playlists;
    }

    default NormalPlaylist getByName(String name) {
        List<NormalPlaylist> playlists = findAll();
        for (NormalPlaylist playlist : playlists) {
            if (playlist.getName().equals(name)) {
                return playlist;
            }
        }
        return null;
    }

    @Override
    default void delete(Long id) {
        NormalPlaylist playlist = getOne(id);
        if (playlist == null)
            return;

        List<NormalPlaylist> onePlaylist = new ArrayList<>();
        onePlaylist.add(playlist);
        delete(onePlaylist);
    }

    @Override
    default void delete(String name) {
        NormalPlaylist playlist = getByName(name);
        if (playlist == null)
            return;

        List<NormalPlaylist> onePlaylist = new ArrayList<>();
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
