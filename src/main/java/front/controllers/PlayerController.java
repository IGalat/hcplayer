package front.controllers;

import com.jr.model.Song;
import front.test.Record;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ResourceBundle;

public class PlayerController extends AbstractController implements Initializable {

    @FXML
    ToolBar toolBar;
    @FXML
    Button buttonStatus;
    @FXML
    Button buttonPlay;
    @FXML
    Button buttonStop;
    @FXML
    Button buttonPause;
    @FXML
    Label musicInfo;

    @FXML
    ImageView iPlay;
    @FXML
    ImageView iStop;
    @FXML
    ImageView iPause;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.playerController = this;

        buttonStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//                System.out.println(toolBar.getParent().getChildrenUnmodifiable().get(2).getLayoutBounds().getWidth());
                GController.playListController.records.forEach(o -> System.out.println(((Record)o).getName()));
            }
        });

        buttonPlay.setOnAction(event -> {
            if (play(null)) {
                iPlay.setImage(new Image("/images/play_red.png"));
                iPause.setImage(new Image("/images/pause_green.png"));
                iStop.setImage(new Image("/images/stop_green.png"));
            }
        });
        buttonStop.setOnAction(event -> {
            if (stop()) {
                iPlay.setImage(new Image("/images/play_green.png"));
                iPause.setImage(new Image("/images/pause_green.png"));
                iStop.setImage(new Image("/images/stop_red.png"));
            }
        });
        buttonPause.setOnAction(event -> {
            if (pause()) {
                iPlay.setImage(new Image("/images/play_green.png"));
                iPause.setImage(new Image("/images/pause_red.png"));
                iStop.setImage(new Image("/images/stop_red.png"));
            }
        });
    }

    static MediaPlayer mediaPlayer = null;

    static synchronized boolean stop() {
        mediaPlayer.stop();
        return true;
    }

    static synchronized boolean pause() {
        mediaPlayer.pause();
        return true;
    }

    static synchronized boolean play(Song song) {
//        if (song == null) {
//            if (scene.focusOwnerProperty().get() instanceof TextArea) {
//                TextArea focusedTextArea = (TextArea) scene.focusOwnerProperty().get();
//            }
//        }
        Media media = new Media(song.getPath().toUri().toString());
        if (mediaPlayer != null) {
            switch (mediaPlayer.getStatus()) {
                case PAUSED:
                case STOPPED:
                    break;
                case HALTED:
                case DISPOSED:
                case READY:
                default:
                    mediaPlayer.dispose();
                    mediaPlayer = null;
                    mediaPlayer = new MediaPlayer(media);
                    setupPlayer();
                    break;
            }
        } else {
            mediaPlayer = new MediaPlayer(media);
            setupPlayer();
        }
        try {
            GController.playerController.musicInfo.setText(song.getTags().getArtist());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mediaPlayer.play();
        return true;
    }

    static synchronized void setupPlayer() {
        mediaPlayer.setOnEndOfMedia(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnError(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnStalled(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnPaused(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnPlaying(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnReady(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnStopped(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));
        mediaPlayer.setOnRepeat(() -> GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString()));

        mediaPlayer.setVolume(0.2);
    }
}
