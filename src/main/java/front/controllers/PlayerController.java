package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.execution.MediaPlayerAdapter;
import com.jr.execution.ObservableForPlayer;
import com.jr.logic.PlayOrder;
import com.jr.util.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class PlayerController extends AbstractController implements Initializable, Observer {
    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

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
    Button buttonNext;
    @FXML
    Button buttonPrev;
    @FXML
    Label labelMusicInfo;
    @FXML
    ChoiceBox<String> playOrder;

    @FXML
    Slider timeSlider;
    @FXML
    Slider soundSlider;
    @FXML
    Label soundLabel;

    @FXML
    ImageView imPlay;
    Image gPlay = new Image("images/play_green.png");
    Image rPlay = new Image("images/play_red.png");

    @FXML
    ImageView imStop;
    Image gStop = new Image("images/stop_green.png");
    Image rStop = new Image("images/stop_red.png");

    @FXML
    ImageView imPause;
    Image gPause = new Image("images/pause_green.png");
    Image rPause = new Image("images/pause_red.png");

    MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.playerController = this;
        ObservableForPlayer.getInstance().addObserver(this);

        playOrder.setItems(FXCollections.observableArrayList(PlayOrder.playOrdersArray));
        playOrder.setValue(Settings.getPlayOrder().toString());
        playOrder.setOnAction(event -> {
            HCPlayer.setPlayOrder(PlayOrder.parse(playOrder.getValue()));
        });

        soundSlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                soundLabel.textProperty().setValue(String.valueOf(Math.round(soundSlider.getValue() * 100)) + '%');
            }
        });
        soundSlider.setOnMouseReleased(event -> {
            MediaPlayerAdapter.setVolume(soundSlider.getValue());
        });
        soundSlider.setValue(Settings.getPlayerVolume());

        buttonStatus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
//                System.out.println(toolBar.getParent().getChildrenUnmodifiable().get(2).getLayoutBounds().getWidth());
//                GController.playListController.playLists.forEach(o -> System.out.println(((Record) o).getName()));
            }
        });

        buttonPlay.setOnAction(event -> {
            HCPlayer.play();
        });
        buttonStop.setOnAction(event -> {
            HCPlayer.stop();
        });
        buttonPause.setOnAction(event -> {
            HCPlayer.pause();
        });
        buttonNext.setOnAction(event -> {
            HCPlayer.playNextSong();
        });
        buttonPrev.setOnAction(event -> {
            HCPlayer.playPreviousSong();
        });
    }

    private void attachMediaPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnEndOfMedia(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
        });
        mediaPlayer.setOnError(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
        });
        mediaPlayer.setOnStalled(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
        });
        mediaPlayer.setOnPaused(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
            imPlay.setImage(gPlay);
            imStop.setImage(gStop);
            imPause.setImage(rPause);
        });
        mediaPlayer.setOnPlaying(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
            imPlay.setImage(rPlay);
            imStop.setImage(gStop);
            imPause.setImage(gPause);

            timeSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        });
        mediaPlayer.setOnReady(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());

            //слайдер звука
            soundSlider.valueProperty().unbind();
            soundSlider.valueProperty().bindBidirectional(mediaPlayer.volumeProperty());

            //слайдер времени в песне
//            Duration duration = mediaPlayer.getMedia().getDuration();
            mediaPlayer.currentTimeProperty().addListener((o, old, currentDuration) -> {
//                playTime.setText(formatTime(currentDuration, duration));
                if (!timeSlider.isValueChanging()) {
                    timeSlider.setValue(currentDuration.toSeconds());
                }
            });

            timeSlider.setMinorTickCount(0);
            timeSlider.setMajorTickUnit(Math.round(mediaPlayer.getTotalDuration().toSeconds() / 10));

            timeSlider.valueProperty().addListener(o -> {
                if (timeSlider.isValueChanging()) {
                    mediaPlayer.seek(Duration.seconds(timeSlider.getValue()));
                }
            });
            timeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    timeSlider.setValueChanging(true);
                    double value = (event.getX() / timeSlider.getWidth()) * timeSlider.getMax();
                    timeSlider.setValue(value);
                    timeSlider.setValueChanging(false);
                }
            });
        });
        mediaPlayer.setOnStopped(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
            imPlay.setImage(gPlay);
            imStop.setImage(rStop);
            imPause.setImage(gPause);
        });
        mediaPlayer.setOnRepeat(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        log.debug("observer update started");
        if (mediaPlayer == null || MediaPlayerAdapter.getMediaPlayer() != null && mediaPlayer != MediaPlayerAdapter.getMediaPlayer()) {
            mediaPlayer = MediaPlayerAdapter.getMediaPlayer();
            attachMediaPlayer(mediaPlayer);

            GController.playerController.labelMusicInfo.setText(HCPlayer.getCurrentSong().getTags().getArtist() + " / " + HCPlayer.getCurrentSong().getTags().getTitle());
        }
        log.debug("observer update ended");
    }
}
