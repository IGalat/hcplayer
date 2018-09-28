package front.controllers;

import com.jr.execution.HCPlayer;
import com.jr.model.NormalPlaylist;
import com.jr.model.Playlist;
import com.jr.service.NormalPlaylistService;
import com.jr.service.SongService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class PlayListController extends AbstractController implements Initializable {
    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Playlist> playlistTableView;

    @FXML
    TableColumn<Playlist, String> nameCol;
    @FXML
    TableColumn<Playlist, Number> countCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.playListController = this;

        nameCol.setCellValueFactory(new PropertyValueFactory<Playlist, String>("name"));
        countCol.setCellValueFactory(i -> {
            return new SimpleIntegerProperty(i.getValue().getSongs().size());
        });

        playlistTableView.setItems((ObservableList) NormalPlaylistService.getAll());

        nameCol.setCellFactory(TextFieldTableCell.<Playlist>forTableColumn());
        nameCol.setOnEditCommit(
                (TableColumn.CellEditEvent<Playlist, String> t) -> {
                    Playlist playlist = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (!playlist.getName().equals(t.getNewValue())) {
                        log.info(NormalPlaylistService.rename((NormalPlaylist) playlist, t.getNewValue()));
                    }
                });

        playlistTableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE && playlistTableView.getSelectionModel().getSelectedIndex() >= 0) {
                ObservableList<Playlist> selectedItems = playlistTableView.getSelectionModel().getSelectedItems();
                String s = (selectedItems.size() > 1) ? String.valueOf(selectedItems.size()) + " " + selectedItems.get(0).getClass().getSimpleName() : selectedItems.get(0).getName();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s + " ?", ButtonType.YES, ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    List<Integer> selectedIndices = new ArrayList<>(playlistTableView.getSelectionModel().getSelectedIndices());
                    for (int i = selectedIndices.size() - 1; i >= 0; i--) {
                        Playlist playlist = playlistTableView.getItems().get(selectedIndices.get(i));
                        log.info(this.getClass().getSimpleName() + " trying to remove " + playlist);
                        NormalPlaylistService.remove((NormalPlaylist) playlist);
                    }
                }
            }
        });

        playlistTableView.setRowFactory(tv -> {
            TableRow<Playlist> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Playlist selectedItem = playlistTableView.getSelectionModel().getSelectedItem();
                    GController.songsController.playlist = selectedItem;
                    GController.songsController.songsTableView.setItems((ObservableList) selectedItem.getSongs());
                }
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    HCPlayer.playPlaylist(playlistTableView.getSelectionModel().getSelectedItem());
                    //.anonPlaylist(playlistTableView.getSelectionModel().getSelectedItems()));
                }
            });

            row.setOnDragOver(event -> {
                // data is dragged over the targetSong_42
                Dragboard db = event.getDragboard();
                if (event.getDragboard().hasString()
                        && (db.getString().startsWith("Song_") || db.getString().startsWith("musicSong_"))) {
                    event.acceptTransferModes(TransferMode.COPY);
//                    playlistTableView.requestFocus();
                    playlistTableView.getSelectionModel().select(row.getIndex());
//                    playlistTableView.getFocusModel().focus(0);
                }
                event.consume();
            });

            row.setOnDragExited(event -> {
                playlistTableView.getSelectionModel().clearSelection();
                event.consume();
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (!row.isEmpty() && event.getDragboard().hasString()
                        && (db.getString().startsWith("Song_") || db.getString().startsWith("musicSong_"))) {
                    log.info("Trying to drag and drop (" + db.getString() + ") into playList: " + playlistTableView.getSelectionModel().getSelectedItem().getName());
                    String[] s = db.getString().substring(db.getString().indexOf('_') + 1).split(",");
                    ArrayList<Long> longs = new ArrayList<>(s.length);
                    for (String ss : s)
                        longs.add(Long.valueOf(ss));
                    playlistTableView.getItems().get(row.getIndex()).getSongs().addAll(SongService.getByIds(longs));
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            });
            return row;
        });
    }

    public void addNewRow() {
        TextInputDialog dialog = new TextInputDialog("awesomePlayListNameqt");
//        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(400, 100);
//        dialog.setHeaderText("format: playlistName");
        dialog.setContentText("Enter playList name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(s -> {
            log.info("Trying to save " + NormalPlaylistService.save(result.get()));
        });
    }
}
