package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class SongsController extends AbstractController implements Initializable {
    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Song> songsTableView;

    @FXML
    TableColumn<Song, String> artistCol;
    @FXML
    TableColumn<Song, String> titleCol;

    private ObservableList<Song> songs;
    public Playlist playlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.songsController = this;
        songsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        songs = (ObservableList) Util.getInitialPlaylist().getSongs();

//        artistCol.setCellValueFactory(new PropertyValueFactory<Song, String>("id"));

        artistCol.setCellValueFactory(i -> {
            if (i.getValue().exists())
                return new SimpleStringProperty(i.getValue().getTags().getArtist());
            else
                return new SimpleStringProperty(i.getValue().getPath().getFileName().toString());
        });
//        artistCol.setCellValueFactory(
//                new Callback<TableColumn.CellDataFeatures<Song, String>, ObservableValue<String>>() {
//                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Song, String> song) {
//                        if (song.getValue().exists())
//                            return new SimpleStringProperty(song.getValue().getTags().getArtist());
//                        else
//                            return new SimpleStringProperty(song.getValue().getPath().getFileName().toString());
//                    }
//                }
//        );
        titleCol.setCellValueFactory(i -> {
            if (i.getValue().exists())
                return new SimpleStringProperty(i.getValue().getTags().getTitle());
            else
                return new SimpleStringProperty(i.getValue().getPath().getFileName().toString());
        });
        songsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        songsTableView.setItems(FXCollections.observableArrayList(songs));

        songsTableView.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
//                    HCPlayer.playPlaylist(NormalPlaylistService.anonPlaylist(songsTableView.getSelectionModel().getSelectedItems()));
                    HCPlayer.playPlaylist(GController.songsController.playlist, songsTableView.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });


    }
}
