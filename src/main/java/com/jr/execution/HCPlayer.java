package com.jr.execution;

import com.jr.model.IPlayPolicy;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Galatyuk Ilya
 */
public final class HCPlayer {
    @Getter
    @Setter
    private static IPlayPolicy playPolicy;
    @Getter
    private static Playlist playlist; //todo save/load chosen playlist; default all-encompassing playlist with standard flavor
    @Getter
    private static Song playingSong;
    @Getter
    private static List<Long> playingHistory = new ArrayList<>(); // only for current playlist //todo fill it when needed!

    //todo init block? with setting new playlist

    public static MediaPlayer getMediaPlayer() {
        return MediaPlayerAdapter.getMediaPlayer();
    }

    public static void setPlaylist(Playlist playlist) {
        setPlaylist(playlist, null);
    }

    public static void setPlaylist(Playlist playlist, Long songToPlayFirst) {
        HCPlayer.playlist = playlist;
        playingHistory = new ArrayList<>();
        //todo set playing song to play first in playlist; set playingSong; start playing
        play(songToPlayFirst);
    }

    public static Song playNextSong() {
        if (playlist.getSongs() == null || playlist.getSongs().size() < 1)
            return null; //todo


        playingSong = playPolicy.getNextSong(playlist, playingHistory);
        playingHistory.add(playingSong.getId());

        //todo actually play
        return null;
    }

    public static Song playPreviousSong() {
        return null;
    }

    public static void stop() {
        //todo lower/up song's novelty, song to settings(so start with it, but don't save time), ?
        MediaPlayerAdapter.stop();
    }

    public static void play(Long songToPlay) {
        //todo pick song and potentially start time; check it exists
        //also adjust volume and such
        Path path = SongService.getOne(songToPlay).getPath();
        MediaPlayerAdapter.play(path);
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

    //todo mediaPlayer -> onStop new song

}
