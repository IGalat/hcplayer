package front;

import com.jr.structure.model.Song;
import com.jr.service.SongService;
import com.mpatric.mp3agic.ID3v2;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MusicLibTableView implements Initializable {

    MediaPlayer mediaPlayer;

    @FXML
    TableView<ID3v2> MusicLibTable = new TableView<>();

    @FXML
    TableColumn<ID3v2, String> artistCol = new TableColumn<>();
    @FXML
    TableColumn<ID3v2, String> titleCol = new TableColumn<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Song> songs = SongService.getAll();
        List<ID3v2> tags = new ArrayList<>();
        for (Song x : songs) {
            if (x.getMp3File() != null)
                tags.add(x.getTags());
        }
        artistCol.setCellValueFactory(new PropertyValueFactory<ID3v2, String>("artist"));
        titleCol.setCellValueFactory(new PropertyValueFactory<ID3v2, String>("title"));

        MusicLibTable.setItems(FXCollections.observableArrayList(tags));

        MusicLibTable.setRowFactory(tv -> {
            TableRow<ID3v2> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    ID3v2 clickedSong = row.getItem();
                    getSongAndPlay(clickedSong);
                }
            });
            return row;
        });
    }

    @FXML
    public void playSong() {
        System.out.println("123");
    }

    public void getSongAndPlay(ID3v2 tags) {
        for (Song song : SongService.getAll()) {
            if (song.getMp3File() != null && tags.equals(song.getTags())) {
                Media media = new Media(song.getPath().toUri().toString());
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer = null;
                } else {
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setVolume(0.2);
                    mediaPlayer.play();
                }
                return;
            }
        }
    }
}

