package front.controllers;

import com.jr.model.Crit;
import com.jr.service.CritService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CritsController extends AbstractController implements Initializable {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    TableView<Crit> CritsTable;

    @FXML
    TableColumn<Crit, Integer> idCol;
    @FXML
    TableColumn<Crit, String> nameCol;
    @FXML
    TableColumn<Crit, String> minMaxCol;
    @FXML
    TableColumn<Crit, String> parentsChildsCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.critsController = this;

        CritsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        nameCol.setCellValueFactory(cellData -> cellData.getValue().getName());
        nameCol.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getName()));

//        minCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("min"));
        minMaxCol.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getMin() + " .. " + i.getValue().getMax()));
        parentsChildsCol.setCellValueFactory(i -> new SimpleStringProperty(CritService.getParents(i.getValue()).size() + " / " + String.valueOf(i.getValue().getChildren().size())));

        CritsTable.setItems((ObservableList) CritService.getAll());

        nameCol.setCellFactory(TextFieldTableCell.<Crit>forTableColumn());
        nameCol.setOnEditCommit(
                (TableColumn.CellEditEvent<Crit, String> t) -> {
                    Crit crit = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (!crit.getName().equals(t.getNewValue())) {
                        log.info(CritService.rename(crit, t.getNewValue()));
                    }
                });

        CritsTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE && CritsTable.getSelectionModel().getSelectedIndex() >= 0) {
                ObservableList<Crit> selectedItems = CritsTable.getSelectionModel().getSelectedItems();
                String s = (selectedItems.size() > 1) ? String.valueOf(selectedItems.size()) + " " + selectedItems.get(0).getClass().getSimpleName() : selectedItems.get(0).getName();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + s + " ?", ButtonType.YES, ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    List<Integer> selectedIndices = new ArrayList<>(CritsTable.getSelectionModel().getSelectedIndices());
                    for (int i = selectedIndices.size() - 1; i >= 0; i--) {
                        Crit crit = CritsTable.getItems().get(selectedIndices.get(i));
                        log.info(this.getClass().getSimpleName() + " trying to remove " + crit);
                        CritService.remove(crit);
                    }
                }
            }
        });
    }

    public void addNewRow() {
        TextInputDialog dialog = new TextInputDialog("awesomeCritNameqt,1,10");
//        dialog.setResizable(true);
        dialog.getDialogPane().setPrefSize(400, 100);
        dialog.setHeaderText("format: critName,Min,Max");
        dialog.setContentText("Enter crit name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(s -> {
            String[] split = new String[3];
            try {
                split = s.split(",");
                log.info("Trying to save " + CritService.save(split[0], Integer.valueOf(split[1]), Integer.valueOf(split[2])));
            } catch (Exception e) {
            }
        });
    }

//    ObservableList<YourObjectClass> actualList = ...;
//    FilteredList<YourObjectClass> filteredList = new FilteredList<>(actualList);
//
//    TableView table = ...;
//table.setItems(filteredList);
//
//// to filter
//filteredList.setPredicate(
//        new Predicate<YourObjectClass>()
//
//    {
//        public boolean test (YourObjectClass t){
//        return false; // or true
//    }
//    }
//);

}
