package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.model.Playlist;
import com.jr.model.Song;
import com.jr.service.SongService;
import com.jr.util.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.*;
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

        songsTableView.setItems((ObservableList) songs);

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
            if (event.getDragboard().hasString() && db.getString().startsWith("musicSong_")) {
                songsTableView.getSelectionModel().selectAll();
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        songsTableView.setOnDragExited(event -> {
            songsTableView.getSelectionModel().clearSelection();
        });

        songsTableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasString() && db.getString().startsWith("musicSong_")) {
                log.info("Trying to drag and drop (" + db.getString() + ") into playList: " + playlist.getName());
                String[] s = db.getString().substring(db.getString().indexOf('_') + 1).split(",");
                ArrayList<Long> longs = new ArrayList<>(s.length);
                for (String ss : s)
                    longs.add(Long.valueOf(ss));
                playlist.getSongs().addAll(SongService.getByIds(longs));
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

        songsTableView.setOnDragDetected(event -> {
            ObservableList<Song> selectedItems = songsTableView.getSelectionModel().getSelectedItems();
            if (selectedItems != null && selectedItems.size() > 0) {
                Dragboard db = songsTableView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();

                StringBuilder sb = new StringBuilder(selectedItems.size() * 5);
                sb.append(selectedItems.get(0).getClass().getSimpleName() + "_" + selectedItems.get(0).getId());
                for (int i = 1; i < selectedItems.size(); i++)
                    sb.append("," + selectedItems.get(i).getId());

                content.putString(sb.toString());
                db.setContent(content);
                event.consume();
            }
        });

    }
}
