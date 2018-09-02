package com.jr.execution;

import com.jr.util.Defaults;
import com.jr.util.Settings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;

import java.nio.file.Path;

/**
 * Setting mediaPlayer properties should be done via setters in this class,
 * while getters can be(unreliably - mediaPlayer doesn't exist if no song is up) used directly
 * <p>
 * This class should not be used for managing which song to play! Use HCPlayer.
 *
 * @author Galatyuk Ilya
 */
public class MediaPlayerAdapter {
    @Getter
    private static MediaPlayer mediaPlayer;
    @Getter
    private static double volume = Settings.getPlayerVolume();
    static final Thread onEndOfSong = new Thread(new SongEndRunnable());

    //todo here could be your equalizer!

    static {
        JFXPanel fxPanel = new JFXPanel(); //javafx toolkit init
    }


    static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    static void play(Path path) throws RuntimeException {
        Media media = new Media(path.toUri().toString());

        stop();
        mediaPlayer = new MediaPlayer(media);
        ObservableForPlayer.getInstance().update();

        mediaPlayer.setOnEndOfMedia(() -> {
            onEndOfSong.start();
        });

        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    static void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    static void play() {
        if (mediaPlayer != null)
            mediaPlayer.play();
    }

    public static void destruct() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    static void setVolume(double volume) {
        if (volume > 1)
            volume = 1;
        MediaPlayerAdapter.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    static class SongEndRunnable implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(Defaults.TIME_BETWEEN_SONGS_MILLISEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mediaPlayer.dispose(); //or else memory leak courtesy of javafx?
            HCPlayer.playNextSong();
        }
    }
}
