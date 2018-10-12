package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.model.Song;
import com.jr.service.NormalPlaylistService;
import com.jr.service.SongService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class MusicLibraryController extends AbstractController implements Initializable {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Song> musicLibTableView;

    @FXML
    TableColumn<Song, String> artistCol;
    @FXML
    TableColumn<Song, String> titleCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.musicLibraryController = this;

        ObservableList<Song> songs = (ObservableList) SongService.getAll();
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
        musicLibTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ;

        musicLibTableView.setItems(FXCollections.observableArrayList(songs));

        musicLibTableView.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
//                    HCPlayer.playPlaylist(NormalPlaylistService.anonPlaylist(musicLibTableView.getSelectionModel().getSelectedItems()));
                }
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    HCPlayer.playPlaylist(NormalPlaylistService.anonPlaylist(musicLibTableView.getSelectionModel().getSelectedItems()));
                }
            });
            return row;
        });

        musicLibTableView.setOnDragDetected(event -> {
            ObservableList<Song> selectedItems = musicLibTableView.getSelectionModel().getSelectedItems();
            if (selectedItems != null && selectedItems.size() > 0) {
                Dragboard db = musicLibTableView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();

                StringBuilder sb = new StringBuilder(selectedItems.size() * 5);
                sb.append("musicSong_" + selectedItems.get(0).getId());
                for (int i = 1; i < selectedItems.size(); i++)
                    sb.append("," + selectedItems.get(i).getId());

                content.putString(sb.toString());
                db.setContent(content);
                event.consume();
            }
        });
    }
}

