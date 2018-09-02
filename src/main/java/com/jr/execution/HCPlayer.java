package com.jr.execution;

import com.jr.model.IPlayOrder;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;
import com.jr.util.Defaults;
import com.jr.util.Settings;
import com.jr.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Use this for managing playlist/songs playback, and MediaPlayerAdapter for media properties.
 *
 * @author Galatyuk Ilya
 */
public final class HCPlayer {
    @Getter
    @Setter
    private static IPlayOrder playOrder = Settings.getPlayOrder();
    @Getter
    private static Playlist playlist = Util.getInitialPlaylist();
    @Getter
    private static Song currentSong;
    private static List<Long> playingHistory = new ArrayList<>(); // only for current playlist
    @Setter
    @Getter
    private static int minSongsWithoutRepeat = Settings.getMinSongsWithoutRepeat();
    @Setter
    @Getter
    private static double minSongsWithoutRepeatInPlaylistPercentage = Settings.getMinSongsWithoutRepeatInPlaylistPercentage();
    @Getter
    private static List<Exception> exceptionList = new ArrayList<>();
    private static final Logger log = LogManager.getLogger(HCPlayer.class);

    public static void setPlaylist(Playlist playlist) {
        setPlaylist(playlist, null);
    }

    public static void setPlaylist(Playlist playlist, Song songToPlayFirst) {
        HCPlayer.playlist = playlist;
        playingHistory = new ArrayList<>();
        stop();
        log.debug("Playlist set: " + playlist);

        if (songToPlayFirst != null) {
            playNewSong(songToPlayFirst);
        } else {
            playNextSong();
        }
    }

    public static void playNextSong() {
        stop();
        List<Song> songs = playlist.getSongs();
        //todo songs' novelty. Class with separate thread and timer, when timer's up refresh novelties?
        if (songs == null || songs.size() < 1) {
            addException(new Exception("Playlist '" + playlist.getName() +
                    "' songs are nonexistent or empty, cannot play next song"));
            return;
        }

        try {
            Thread.sleep(Defaults.TIME_BETWEEN_SONGS_MILLISEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        songs.removeIf(Objects::isNull);

        Song nextSong = playOrder.getNextSong(playlist, playingHistory);
        if (nextSong != null)
            playNewSong(nextSong);
        else {
            addException(new Exception("Song couldn't be selected from playlist '" + playlist.getName() + "' with play order " + playOrder.toString()));
            playNextSong(); //todo clean infinite loop if songs all absent or loops on absent song
        }
    }

    public static void playPreviousSong() {
        int size = playingHistory.size();
        if (size > 0)
            playNewSong(SongService.getOne(playingHistory.get(size - 1)));
        else
            playNewSong(currentSong);
    }

    private static void playNewSong(Song songToPlay) {
        currentSong = songToPlay;
        if (playingHistory.size() < 1
                || playingHistory.get(playingHistory.size() - 1) != currentSong.getId()) {
            playingHistory.add(currentSong.getId());
        }

        try {
            MediaPlayerAdapter.play(songToPlay.getPath());
        } catch (RuntimeException e) {
            addException(e);
        }
    }

    private static void addException(Exception e) {
        exceptionList.add(e);
        log.error(e);
        ObservableForPlayer.getInstance().update();
    }

    public static void stop() {
        MediaPlayerAdapter.stop();
    }

    public static void pause() {
        MediaPlayerAdapter.pause();
    }

    public static void resume() {
        MediaPlayerAdapter.resume();
    }

}
