package front;

import com.jr.util.Util;
import front.controllers.GlobalVBoxController;
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
        log.info("init started");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            log.error(t, e);
        });

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/fxml/GlobalVBox.fxml"));
        GlobalVBoxController globalVBoxController = loader.getController();
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("/css/main.css").toURI().toString());

        primaryStage.setTitle("hcplayer");
        primaryStage.getIcons().add(new Image("/images/player_icon.png"));
        primaryStage.show();
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
            StringWriter writer = new StringWriter();
            PrintWriter pw = new PrintWriter(writer);
            t.printStackTrace(pw);
            log.error(writer.toString());
        }
        Platform.exit();
        System.exit(0);
    }
}
