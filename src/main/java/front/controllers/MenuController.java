package front.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController extends AbstractController implements Initializable {

    @FXML
    MenuItem do_shit;

    @FXML
    MenuItem about;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GController.menuController = this;

        do_shit.setOnAction(event -> {
            GController.playerController.buttonStatus.setText("TEST");
        });

        about.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "this is player.", ButtonType.OK);
            alert.setHeaderText(null);

            //exmapple of adding icons
            Stage window = (Stage) alert.getDialogPane().getScene().getWindow();
            window.getIcons().add(new Image("images/player_icon.png"));

            alert.showAndWait();
        });
    }
}
