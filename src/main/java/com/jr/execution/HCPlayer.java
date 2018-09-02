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
    @Getter
    private static List<Long> playingHistory = new ArrayList<>(); // only for current playlist
    @Setter
    @Getter
    private static int minSongsWithoutRepeat = Settings.getMinSongsWithoutRepeat();
    @Setter
    @Getter
    private static double minSongsWithoutRepeatInPlaylistPercentage = Settings.getMinSongsWithoutRepeatInPlaylistPercentage();
    @Getter
    private static List<Exception> exceptionList = new ArrayList<>();

    public static void setPlaylist(Playlist playlist) {
        setPlaylist(playlist, null);
    }

    public static void setPlaylist(Playlist playlist, Song songToPlayFirst) {
        HCPlayer.playlist = playlist;
        playingHistory = new ArrayList<>();

        if (songToPlayFirst != null) {
            playNewSong(songToPlayFirst);
        } else {
            playNextSong();
        }
    }

    public static void playNextSong() {
        //todo songs' novelty. Class with separate thread and timer, when timer's up refresh novelties?
        if (playlist.getSongs() == null || playlist.getSongs().size() < 1) {
            exceptionList.add(new Exception("Playlist is nonexistent or empty, cannot playNewSong next song"));
            return;
            //todo observable update
        }

        try {
            Thread.sleep(Defaults.TIME_BETWEEN_SONGS_MILLISEC);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        playlist.getSongs().removeIf(Objects::isNull);

        Song nextSong = playOrder.getNextSong(playlist, playingHistory);
        if (nextSong != null)
            playNewSong(nextSong);
        else {
            playNextSong(); //todo clean infinite loop if songs all absent or
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
            exceptionList.add(e);
            //todo observable update
        }
    }

    public static void stop() {
        //todo song to settings(so start with it, but don't save time), ?
        MediaPlayerAdapter.stop();
    }

    public static void pause() {
        MediaPlayerAdapter.pause();
    }

    public static void resume() {
        MediaPlayerAdapter.resume();
    }

}
