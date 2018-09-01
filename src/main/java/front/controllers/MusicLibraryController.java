package front.controllers;

import com.jr.model.Song;
import com.jr.service.SongService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MusicLibraryController extends AbstractController implements Initializable {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Song> MusicLibTable;

    @FXML
    TableColumn<Song, String> artistCol;
    @FXML
    TableColumn<Song, String> titleCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.musicLibraryController = this;

        List<Song> songs = SongService.getAll();
//        artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("id"));

        artistCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Song, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Song, String> song) {
                        if (song.getValue().exists())
                            return new SimpleStringProperty(song.getValue().getTags().getArtist());
                        else
                            return new SimpleStringProperty(song.getValue().getPath().getFileName().toString());
                    }
                }
        );

        titleCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Song, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Song, String> song) {
                        if (song.getValue().exists())
                            return new SimpleStringProperty(song.getValue().getTags().getTitle());
                        else
                            return new SimpleStringProperty(song.getValue().getPath().getFileName().toString());
                    }
                }
        );

        MusicLibTable.setItems(FXCollections.observableArrayList(songs));

        MusicLibTable.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    Song clickedSong = row.getItem();
                    PlayerController.play(clickedSong);
                }
            });
            return row;
        });
    }
}

