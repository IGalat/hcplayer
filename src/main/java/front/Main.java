package front;

import com.jr.util.Util;
import front.controllers.GlobalBorderPaneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public class Main extends Application {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            log.info("init");
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/fxml/GlobalBorderPane.fxml"));
            GlobalBorderPaneController globalBorderPaneController = loader.getController();
            primaryStage.setScene(new Scene(root, 1024, 768));

            primaryStage.setTitle("hcplayer");
            primaryStage.getIcons().add(new Image("/images/player_icon.png"));
            primaryStage.show();
        } catch (Throwable t) {
            log.error(t);
        }
    }

    @Override
    public void stop() {
        log.info("shutdown");
        Util.shutdown();
        log.info("\n");
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            log.error(t);
        }
    }
}
