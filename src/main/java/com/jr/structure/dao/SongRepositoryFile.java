package com.jr.structure.dao;

import com.jr.structure.model.Crit;
import com.jr.structure.model.Song;
import com.jr.structure.service.CritService;
import com.jr.util.FileOps;

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
    private List<Song> songs = findAll();

    @Override
    public List<Song> findAll() {
        List<Map<String, String>> allSongsMap = FileOps.getAll(FileOps.getSongsName());

        songs = new ArrayList<>();
        for (Map<String, String> songMap : allSongsMap) {
            //necessary to remove after read properties: only crits left after
            Long id = Long.parseLong(songMap.get(ID_NAME));
            songMap.remove(ID_NAME);

            String pathString = songMap.get(PATH_NAME);
            songMap.remove(PATH_NAME);
            Path path = FileSystems.getDefault().getPath(songMap.get(PATH_NAME));

            Map<Crit, Integer> crits = new HashMap<>();
            for (Map.Entry<String, String> critEntry : songMap.entrySet()) {
                Crit crit = CritService.getByName(critEntry.getKey());
                Integer value = Integer.parseInt(critEntry.getValue());

                crits.put(crit, value);
            }

            songs.add(new Song(id, path, crits));
        }

        return songs;
    }

    @Override
    public List<Song> save(Iterable<Song> items) {
        for (Song songToSave : items) {
            for (Song song : songs) {
                if (songToSave.getId() == song.getId()
                        || songToSave.getPath().equals(song.getPath())) {
                    songs.remove(song);
                }
            }
            songs.add(songToSave);
        }
        rewriteFile();
        return (List<Song>) items;
    }

    @Override
    public void delete(Iterable<Song> items) {
        for (Song songToDelete : songs) {
            songs.remove(songToDelete);
        }
        rewriteFile();
    }

    private void rewriteFile() {
        List<Map<String, String>> listToSave = new ArrayList<>();
        for (Song song : songs) {
            Map<String, String> mapOfSong = new HashMap<>();

            mapOfSong.put(PATH_NAME, song.getPath().toString());

            for (Map.Entry<Crit, Integer> crit : song.getCrits().entrySet()) {
                mapOfSong.put(crit.getKey().getName(), crit.getValue().toString());
            }

            listToSave.add(mapOfSong);
        }

        FileOps.put(FileOps.getSongsName(), listToSave, false);
    }
}
