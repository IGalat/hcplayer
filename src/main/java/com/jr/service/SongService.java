package com.jr.service;

import com.jr.logic.CritHardcode;
import com.jr.dao.SongRepository;
import com.jr.dao.SongRepositoryFile;
import com.jr.model.Crit;
import com.jr.model.Song;
import com.jr.util.Settings;
import javafx.util.Pair;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class SongService {
    private static final SongRepository songRepo = new SongRepositoryFile();

    public static List<Song> getAll() {
        return songRepo.findAll();
    }

    public static List<Song> getByIds(List<Long> ids) {
        return songRepo.findAll(ids);
    }

    public static Song getByPath(Path path) {
        return songRepo.getByPath(path.toString());
    }

    public static Song getByPath(String path) {
        return songRepo.getByPath(path);
    }

    public static Song getOne(long id) {
        return songRepo.getOne(id);
    }

    public static void remove(Song song) {
        songRepo.delete(song);
    }

    public static Song save(String path, Pair<Crit, Integer>... crits) {
        return save(FileSystems.getDefault().getPath(path), crits);
    }

    public static Song save(Path path, Pair<Crit, Integer>... crits) {
        Map<Crit, Integer> critsMap = new HashMap<>();
        for (Pair<Crit, Integer> critPair : crits) {
            critsMap.put(critPair.getKey(), critPair.getValue());
        }

        return save(critsMap, path);
    }

    public static Song save(Map<Crit, Integer> crits, String path) {
        return save(crits, FileSystems.getDefault().getPath(path));
    }

    public static Song save(Map<Crit, Integer> crits, Path path) {
        Song existingSong = getByPath(path);
        Long id = existingSong == null ? Settings.getNextId() : existingSong.getId();

        crits = CritHardcode.addStandardCritsToSongIfAbsent(crits);
        checkCritRanges(path, crits);

        Song song = new Song(id, path, crits);
        songRepo.save(song);
        return song;
    }

    public static Song changePath(Song song, String newPath) {
        return changePath(song, FileSystems.getDefault().getPath(newPath));
    }

    public static Song changePath(Song song, Path newPath) {
        return songRepo.save(new Song(song.getId(), newPath, song.getCrits()));
    }

    private static void checkCritRanges(Path path, Map<Crit, Integer> crits) {
        for (Map.Entry<Crit, Integer> critEntry : crits.entrySet()) {
            int min = critEntry.getKey().getMin();
            int max = critEntry.getKey().getMax();
            Integer value = critEntry.getValue();

            if (value == null) continue;
            if (value < min || value > max)
                throw new InputMismatchException("Song " + path.toString() +
                        " cannot be saved with '" + critEntry.getKey().getName() + "' " + value +
                        ". Value must be between " + min + " and " + max);
        }
    }
}
