package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.execution.MediaPlayerAdapter;
import com.jr.execution.ObservableForPlayer;
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
import javafx.scene.media.MediaPlayer;
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
    Label labelMusicInfo;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.playerController = this;
        ObservableForPlayer.getInstance().addObserver(this);

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
    }

    private void setupPlayer(MediaPlayer mediaPlayer) {
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
        });
        mediaPlayer.setOnReady(() -> {
            GController.playerController.buttonStatus.setText(mediaPlayer.getStatus().toString());
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
        MediaPlayer mediaPlayer = MediaPlayerAdapter.getMediaPlayer();

        setupPlayer(mediaPlayer);
        GController.playerController.labelMusicInfo.setText(HCPlayer.getCurrentSong().getTags().getArtist() + " / " + HCPlayer.getCurrentSong().getTags().getTitle());
        log.debug("observer update ended");
    }
}
