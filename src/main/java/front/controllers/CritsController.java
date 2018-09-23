package front.controllers;

import com.jr.model.Crit;
import com.jr.service.CritService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
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
    TreeTableView<Crit> CritsTable;

    @FXML
    TreeTableColumn<Crit, String> nameCol;
    @FXML
    TreeTableColumn<Crit, String> minMaxCol;
    @FXML
    TreeTableColumn<Crit, String> childsCol;

    private static TreeItem<Crit> root = new TreeItem<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.critsController = this;

        CritsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        nameCol.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getValue().getName()));
//        minCol.setCellValueFactory(new PropertyValueFactory<Crit, Integer>("min"));
        minMaxCol.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getValue().getMin() + " .. " + i.getValue().getValue().getMax()));
        childsCol.setCellValueFactory(i -> new SimpleStringProperty(String.valueOf(i.getValue().getChildren().size())));

        root.setExpanded(true);
        CritsTable.setShowRoot(false);
        CritsTable.setRoot(root);

        refreshCritTable();

        nameCol.setCellFactory(TextFieldTreeTableCell.<Crit>forTreeTableColumn());
        nameCol.setOnEditCommit(
                (TreeTableColumn.CellEditEvent<Crit, String> t) -> {
                    Crit crit = t.getRowValue().getValue();
                    if (!crit.getName().equals(t.getNewValue())) {
                        log.info(CritService.rename(crit, t.getNewValue()));
                    }
                });

        CritsTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE && CritsTable.getSelectionModel().getSelectedIndex() >= 0) {
                List<Crit> selectedItems = new ArrayList<>();
                CritsTable.getSelectionModel().getSelectedItems().forEach(critTreeItem -> {
                    selectedItems.add(critTreeItem.getValue());
                });

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete "
                        + selectedItems.get(0).getName() + " ?\n\n"
                        + "WARNING: CHILDREN WILL NOT BE DELETED", ButtonType.YES, ButtonType.CANCEL);
                alert.setHeaderText(null);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    TreeItem<Crit> selectedItem = CritsTable.getSelectionModel().getSelectedItem();
                    Crit crit = selectedItem.getValue();
                    log.info(this.getClass().getSimpleName() + " trying to remove " + crit);
                    if (CritService.remove(crit))
//                        selectedItem.getParent().getChildren().remove(selectedItem);
                        refreshCritTable();
                }
            }
        });
    }

    public void refreshCritTable() {
        root = new TreeItem<>();
        CritsTable.setRoot(root);
        updateCritTable(root, (ObservableList) CritService.getAll(), new ArrayList<>(CritService.getAll()));
    }

    private void updateCritTable(TreeItem<Crit> temp, ObservableList<Crit> crits, List<Crit> all) {
        crits.forEach(crit -> {
            if (all.contains(crit))
                all.remove(crit);
            else
                return;

            TreeItem<Crit> critTreeItem = new TreeItem<>(crit);
            updateCritTable(critTreeItem, FXCollections.observableArrayList(crit.getChildren()), all);
            temp.getChildren().add(critTreeItem);
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
