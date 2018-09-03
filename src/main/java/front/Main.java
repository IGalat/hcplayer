package front;

import com.jr.util.Util;
import front.controllers.GlobalBorderPaneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;

public class Main extends Application {

    private static final Logger log = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            log.info("init started");

            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                log.error(t, e);
            });

            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/fxml/GlobalBorderPane.fxml"));
            GlobalBorderPaneController globalBorderPaneController = loader.getController();
            primaryStage.setScene(new Scene(root, 1024, 768));

            primaryStage.setTitle("hcplayer");
            primaryStage.getIcons().add(new Image("/images/player_icon.png"));
            primaryStage.show();
        } catch (Throwable t) {
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            t.printStackTrace(pw);
            String s = writer.toString();
            log.error(s);
            System.exit(1);
        }
    }

    @Override
    public void stop() {
        log.info("shutdown start");
        try {
            Util.shutdown();
        } catch (Throwable t) {
            log.error(t);
        }
        log.info("shutdown end\n");
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Throwable t) {
            log.error(t);
        }
        Platform.exit();
        System.exit(0);
    }
}
