package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;
import com.jr.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SongsController extends AbstractController implements Initializable {
    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Song> songsTableView;

    @FXML
    TableColumn<Song, String> artistCol;
    @FXML
    TableColumn<Song, String> titleCol;

    private List<Song> songs;
    public Playlist playlist;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.songsController = this;
        songsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        playlist = Util.getInitialPlaylist();
        songs = playlist.getSongs();

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

        songsTableView.setItems((ObservableList)songs);

        songsTableView.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
//                    HCPlayer.playPlaylist(NormalPlaylistService.anonPlaylist(songsTableView.getSelectionModel().getSelectedItems()));
                    HCPlayer.playPlaylist(playlist, songsTableView.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });


        songsTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE && songsTableView.getSelectionModel().getSelectedIndex() >= 0) {
                ObservableList<Song> selectedItems = songsTableView.getSelectionModel().getSelectedItems();
                String s = (selectedItems.size() > 1) ? String.valueOf(selectedItems.size())
                        + " " + selectedItems.get(0).getClass().getSimpleName() : selectedItems.get(0).getPath().getFileName().toString();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete\n"
                        + (s.lastIndexOf('.') != -1 ? s.substring(0, s.lastIndexOf('.')) : s)
                        + "\nfrom\n" + playlist.getName() + "\nplaylist?", ButtonType.YES, ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    List<Integer> selectedIndices = new ArrayList<>(songsTableView.getSelectionModel().getSelectedIndices());
                    for (int i = selectedIndices.size() - 1; i >= 0; i--) {
                        Song song = songsTableView.getItems().get(selectedIndices.get(i));
                        log.info(this.getClass().getSimpleName() + " trying to remove " + song);
                        songsTableView.getItems().remove(song);
                    }
                }
            }
        });

        songsTableView.setOnDragOver(event -> {
            // data is dragged over the target
            Dragboard db = event.getDragboard();
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        songsTableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasString()) {
                log.info("Trying to drag and drop song id(" + db.getString() + ") from musicLib into playList: " + playlist.getName());
                playlist.getSongs().add(SongService.getOne(Integer.valueOf(db.getString())));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }
}
