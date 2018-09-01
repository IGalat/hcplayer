package front.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends AbstractController implements Initializable {

    @FXML
    MenuItem do_shit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        do_shit.setOnAction(event -> {
            GController.playerController.buttonStatus.setText("TEST");
        });
    }
}
