package com.jr.execution;

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

    //todo here could be your equalizer!

    static {
        JFXPanel fxPanel = new JFXPanel(); //javafx toolkit init
    }


    static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    static void play(Path path) throws RuntimeException {
        Media media = new Media(path.toUri().toString());

        stop();
        mediaPlayer = new MediaPlayer(media);
        ObservableForPlayer.getInstance().update();

        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose(); //or else memory leak courtesy of javafx?
            HCPlayer.playNextSong();
        });

        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    static void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    static void resume() {
        if (mediaPlayer != null)
            mediaPlayer.play();
    }

    static void setVolume(double volume) {
        if (volume > 1)
            volume = 1;
        MediaPlayerAdapter.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
