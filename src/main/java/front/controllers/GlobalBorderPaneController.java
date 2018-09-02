package front.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.ResourceBundle;

public class GlobalBorderPaneController extends AbstractController implements Initializable {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @FXML
    Button addNew;

    @FXML
    TabPane centerTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.globalBorderPaneController = this;

        //autosize tab headers
        centerTable.tabMinWidthProperty().bind(centerTable.widthProperty().divide(centerTable.getTabs().size()).subtract(20));

        addNew.setOnAction(event -> {
            //crits
            if (centerTable.getSelectionModel().getSelectedIndex() == 1) {
                GController.critsController.addNewRow();
            }
        });
    }

}
