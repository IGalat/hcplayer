package front;

import com.jr.structure.model.Song;
import com.jr.structure.service.SongService;
import com.mpatric.mp3agic.ID3v2;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MusicLibTableView implements Initializable {

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
    }

    @FXML
    public void playSong() {
        System.out.println("123");
    }

}
