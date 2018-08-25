package front;

import com.jr.model.Crit;
import com.jr.service.CritService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CritTableViewController implements Initializable {

    @FXML
    TableView<Crit> CritsTable = new TableView<>();

    @FXML
    TableColumn<Crit, Integer> idCol = new TableColumn<>();
    @FXML
    TableColumn<Crit, String> nameCol = new TableColumn<>();
    @FXML
    TableColumn<Crit, Integer> minCol = new TableColumn<>();
    @FXML
    TableColumn<Crit, Integer> maxCol = new TableColumn<>();
    @FXML
    TableColumn<Crit, Boolean> whitelistCol = new TableColumn<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<Crit, String>("name"));
        minCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("min"));
        maxCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("max"));
        whitelistCol.setCellValueFactory(new PropertyValueFactory<Crit, Boolean>("whitelist"));

        CritsTable.setItems(FXCollections.observableArrayList(CritService.getAll()));
    }
}
