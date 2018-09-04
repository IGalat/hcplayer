package com.jr.dao;

import com.jr.model.Crit;
import com.jr.model.Song;
import com.jr.service.CritService;
import com.jr.util.FileOps;
import javafx.collections.FXCollections;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Galatyuk Ilya
 */
public class SongRepositoryFile implements SongRepository {
    private static final String ID_NAME = "id";
    private static final String PATH_NAME = "path";
    private static List<Song> songs;

    static {
        List<Map<String, String>> allSongsMap = FileOps.getAll(FileOps.getSongsName());

        songs = FXCollections.observableArrayList();
        for (Map<String, String> songMap : allSongsMap) {
            //necessary to remove after read properties: only crits left after
            Long id = Long.parseLong(songMap.get(ID_NAME));
            songMap.remove(ID_NAME);

            String pathString = songMap.get(PATH_NAME);
            songMap.remove(PATH_NAME);
            Path path = FileSystems.getDefault().getPath(pathString);

            Map<Crit, Integer> crits = new HashMap<>();
            for (Map.Entry<String, String> critEntry : songMap.entrySet()) {
                Crit crit = CritService.getByName(critEntry.getKey());
                Integer value;
                if (critEntry.getValue().equals("null")) {
                    value = null;
                } else {
                    value = Integer.parseInt(critEntry.getValue());
                }

                crits.put(crit, value);
            }

            songs.add(new Song(id, path, crits));
        }
    }

    @Override
    public List<Song> findAll() {
        return songs;
    }

    @Override
    public synchronized List<Song> save(Iterable<Song> items) {
        for (Song songToSave : items) {
            for (int i = songs.size(); i > 0; i--) {
                Song song = songs.get(i - 1);
                if (songToSave.getId() == song.getId()
                        || songToSave.getPath().equals(song.getPath())) {
                    songToSave.setId(song.getId());
                    songs.remove(song);
                }
            }
            songs.add(songToSave);
        }
        rewriteFile();
        return (List<Song>) items;
    }

    @Override
    public synchronized void delete(Iterable<Song> items) {
        for (Song songToDelete : items) {
            songs.remove(songToDelete);
        }
        rewriteFile();
    }

    public static synchronized void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (Song song : songs) {
            Map<String, String> mapOfSong = new HashMap<>();

            mapOfSong.put(ID_NAME, Long.toString(song.getId()));
            mapOfSong.put(PATH_NAME, song.getPath().toString());

            if (song.getCrits() != null) {
                for (Map.Entry<Crit, Integer> critEntry : song.getCrits().entrySet()) {
                    if (critEntry.getKey() == null) continue;
                    String value;
                    if (critEntry.getValue() == null) value = "null";
                    else value = critEntry.getValue().toString();

                    mapOfSong.put(critEntry.getKey().getName(), value);
                }
            }

            listToSave.add(mapOfSong);
        }

        FileOps.put(FileOps.getSongsName(), listToSave, false);
    }
}
