package com.jr.execution;

import com.jr.util.Defaults;
import com.jr.util.Settings;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

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
    static final Timer timer = new Timer();
    //todo here could be your equalizer!
    private static final Logger log = LogManager.getLogger(MediaPlayerAdapter.class);

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
            timer.schedule(new SongEndRunnable(), Defaults.TIME_BETWEEN_SONGS_MILLIS);
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

    public static void setVolume(double volume) {
        if (volume > 1)
            volume = 1;
        MediaPlayerAdapter.volume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(volume);
        }
    }

    static class SongEndRunnable extends TimerTask {

        @Override
        public void run() {
            Platform.runLater(()->{
                try {
                    Thread.sleep(Defaults.TIME_BETWEEN_SONGS_MILLIS);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                mediaPlayer.dispose(); //or else memory leak courtesy of javafx?
                HCPlayer.playNextSong();
            });
        }
    }
}
