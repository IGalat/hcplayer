package front.controllers;

import front.test.Record;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayListController extends AbstractController implements Initializable {
    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Record> PlayList;

    @FXML
    TableColumn<Record, String> nameCol;

    ObservableList records;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.playListController = this;
//        idCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("id"));
//        nameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));


        records = FXCollections.observableArrayList();

        records.add(new Record("name"));
        records.add(new Record("name1"));
        records.add(new Record("name2"));

        PlayList.setItems(records);

        nameCol.setCellValueFactory(new PropertyValueFactory<Record, String>("name"));
        nameCol.setCellFactory(TextFieldTableCell.<Record>forTableColumn());
        nameCol.setOnEditCommit(
                (TableColumn.CellEditEvent<Record, String> t) -> {
                    Record record = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    records.forEach(o -> {
                        if (((Record) o).getName().equals(record.getName())) {
                            ((Record) o).setName(t.getNewValue());
                            return;
                        }
                    });
                });
    }

}
