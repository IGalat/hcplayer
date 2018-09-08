package front.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class GlobalVBoxController extends AbstractController implements Initializable {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    Button addNew;

    @FXML
    TabPane centerTable;

    @FXML
    VBox globalVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.globalVBoxController = this;

        //autosize tab headers
        centerTable.tabMinWidthProperty().bind(centerTable.widthProperty().divide(centerTable.getTabs().size()).subtract(20));

        addNew.setOnAction(event -> {
            //playlists
            if (centerTable.getSelectionModel().getSelectedIndex() == 0) {
                GController.playListController.addNewRow();
            }
            //crits
            if (centerTable.getSelectionModel().getSelectedIndex() == 1) {
                GController.critsController.addNewRow();
            }
        });
    }

}
