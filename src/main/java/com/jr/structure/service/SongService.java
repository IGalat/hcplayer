package com.jr.structure.service;

import com.jr.structure.dao.SongRepository;
import com.jr.structure.dao.SongRepositoryFile;
import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import com.jr.util.Settings;

import java.nio.file.FileSystems;
import java.nio.file.Path;
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
        Song song = new Song(Settings.getNextId(), path, crits);
        songRepo.save(song);
        return song;
    }


}
