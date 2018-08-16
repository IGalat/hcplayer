package com.jr.service;

import com.jr.structure.dao.SongRepository;
import com.jr.structure.dao.SongRepositoryFile;
import com.jr.logic.CritHardcode;
import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import com.jr.util.Settings;

import java.nio.file.FileSystems;
import java.nio.file.Path;
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

    public static Song save(String path, Map<Crit, Integer> crits) {
        return save(FileSystems.getDefault().getPath(path), crits);
    }

    public static Song save(Path path, Map<Crit, Integer> crits) {
        crits = CritHardcode.addStandardCritsToSongIfAbsent(crits);
        checkCritRanges(path, crits);

        Song song = new Song(Settings.getNextId(), path, crits);
        songRepo.save(song);
        return song;
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
