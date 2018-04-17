package com.jr.structure.dao;

import com.jr.structure.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public interface SongRepository extends CommonRepository<Song, Long> {
    @Override
    default Song getOne(Long id) {
        ArrayList<Long> oneId = new ArrayList<>();
        oneId.add(id);
        List<Song> song = findAll(oneId);
        if (song.size() < 1) {
            return null;
        }
        return song.get(0);
    }

    @Override
    default List<Song> findAll(Iterable<Long> ids) {
        ArrayList<Song> songs = new ArrayList<>();
        List<Song> allSongs = findAll();
        for (Song song : allSongs) {
            for (Long id : ids) {
                if (id.equals(song.getId())) {
                    songs.add(song);
                    break;
                }
            }
        }
        return songs;
    }

    default Song getByPath(String path) {
        List<Song> songs = findAll();
        for (Song song : songs) {
            if (song.getPath().toString().equals(path)) {
                return song;
            }
        }
        return null;
    }

    @Override
    default void delete(Long id) {
        Song song = getOne(id);
        if (song == null)
            return;

        List<Song> oneSong = new ArrayList<>();
        oneSong.add(song);
        delete(oneSong);
    }

    @Override
    default void delete(String path) {
        Song song = getByPath(path);
        if (song == null)
            return;

        List<Song> oneSong = new ArrayList<>();
        oneSong.add(song);
        delete(oneSong);
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
