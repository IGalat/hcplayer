package com.jr.execution;

import com.jr.model.IPlayPolicy;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.util.Settings;
import com.jr.util.Util;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Galatyuk Ilya
 */
public final class HCPlayer {
    @Getter
    @Setter
    private static IPlayPolicy playPolicy = Settings.getPlayPolicy();
    @Getter
    private static Playlist playlist = Util.getInitialPlaylist();
    @Getter
    private static Song currentSong;
    @Getter
    private static List<Long> playingHistory = new ArrayList<>(); // only for current playlist //todo fill it when needed
    @Setter
    @Getter
    private static int minSongsWithoutRepeat = Settings.getMinSongsWithoutRepeat();
    @Setter
    @Getter
    private static double minSongsWithoutRepeatInPlaylistPercentage = Settings.getMinSongsWithoutRepeatInPlaylistPercentage();

    public static MediaPlayer getMediaPlayer() {
        return MediaPlayerAdapter.getMediaPlayer();
    }

    public static void setPlaylist(Playlist playlist) {
        setPlaylist(playlist, null);
    }

    public static void setPlaylist(Playlist playlist, Long songToPlayFirst) {
        HCPlayer.playlist = playlist;
        playingHistory = new ArrayList<>();
        //todo set playing song to play first in playlist; set currentSong; start playing
        //play(songToPlayFirst);
    }

    public static Song playNextSong() {
        if (playlist.getSongs() == null || playlist.getSongs().size() < 1) {
            play(currentSong);
            return currentSong;
        }
        playlist.getSongs().removeIf(Objects::isNull);

        Song nextSong = playPolicy.getNextSong(playlist, playingHistory);
        if (nextSong != null)
            currentSong = nextSong;
        if (currentSong == null)
            return null;

        playingHistory.add(currentSong.getId());
        play(currentSong);
        return currentSong;
    }

    public static Song playPreviousSong() {
        return null;
    }

    public static void stop() {
        //todo lower/up song's novelty depending on time of stop, song to settings(so start with it, but don't save time), ?
        //wait, it doesn't necessarily get called for next song. gotta either call or change place of to do
        MediaPlayerAdapter.stop();
    }

    public static void play(Song songToPlay) {
        MediaPlayerAdapter.play(songToPlay.getPath());
    }

    public static void pause() {
        MediaPlayerAdapter.pause();
    }

    public static double getVolume() {
        return MediaPlayerAdapter.getVolume();
    }

    public static void setVolume(double volume) {
        MediaPlayerAdapter.setVolume(volume);
    }

}
