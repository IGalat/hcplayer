package com.jr.execution;

import com.jr.util.Defaults;
import com.jr.util.Settings;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;

import java.nio.file.Path;

/**
 * @author Galatyuk Ilya
 */
class MediaPlayerAdapter {
    @Getter
    private static MediaPlayer mediaPlayer;
    @Getter
    private static double volume = Settings.getPlayerVolume();

    //todo here could be your equalizer!

    static {
        JFXPanel fxPanel = new JFXPanel(); //javafx toolkit init
    }


    public static void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
    }

    public static void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public static void play(Path path) throws RuntimeException {
        Media media = new Media(path.toUri().toString());

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            try {
                Thread.currentThread().sleep(Defaults.TIME_BETWEEN_SONGS_MILLISEC);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HCPlayer.playNextSong();
        });

        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
    }

    public static void setVolume(double volume) {
        if (volume > 1)
            volume = 1;
        MediaPlayerAdapter.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }
}
