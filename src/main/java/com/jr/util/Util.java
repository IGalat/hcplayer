package com.jr.util;

import com.jr.dao.CritRepositoryFile;
import com.jr.dao.FilteredPlaylistRepositoryFile;
import com.jr.dao.NormalPlaylistRepositoryFile;
import com.jr.dao.SongRepositoryFile;
import com.jr.execution.MediaPlayerAdapter;
import com.jr.logic.CritHardcode;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.NormalPlaylistService;
import com.jr.service.SongService;
import javafx.collections.FXCollections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Galatyuk Ilya
 */
public class Util {
    private static boolean isInitialized = false;
    private static final Logger log = LogManager.getLogger(Util.class);

    public static long roll(long max) {
        return (long) (Math.random() * max + 1);
    }

    public static boolean isNameBad(String name) {
        return !Pattern.matches(Defaults.GOOD_NAME_PATTERN, name);
    }

    public static void init() {
        if (isInitialized) return;

        log.info("init() called");
        CritHardcode.saveStandardCrits();

        try {
            Class.forName("com.jr.execution.HCPlayer");
        } catch (ClassNotFoundException e) {
            log.error(e);
        }

        isInitialized = true;
    }

    public static void shutdown() {
        log.info("shutdown() called");
        MediaPlayerAdapter.destruct();
        saveData();
    }

    public static void saveData() {
        CritRepositoryFile.rewriteFile();
        SongRepositoryFile.rewriteFile();
        NormalPlaylistRepositoryFile.rewriteFile();
        FilteredPlaylistRepositoryFile.rewriteFile();
        Settings.saveSettings();
    }

    public static Playlist getInitialPlaylist() {
        Playlist playlist = null;
        Long id = Settings.getPlaylistId();
        if (id != null) {
            playlist = NormalPlaylistService.getOne(id);
            if (playlist == null) { //todo ask filtered playlist service
            }
        }
        if (playlist != null) return playlist;

        return NormalPlaylistService.anonPlaylist(SongService.getAll());
    }

    public static List<Song> parseSongsFromIds(String songIds) {
        if (songIds == null)
            return SongService.getByIds(FXCollections.observableArrayList());

        List<Long> ids = new ArrayList<>();
        String[] idsString = songIds.split("[,]");

        for (String idString : idsString)
            ids.add(Long.parseLong(idString));

        return SongService.getByIds(ids);
    }

}
