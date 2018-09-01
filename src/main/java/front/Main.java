package front;

import com.jr.execution.HCPlayer;
import com.jr.logic.PlayPolicy;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/GlobalBorderPane.fxml"));
        primaryStage.setTitle("hcplayer");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();

        HCPlayer.setPlayPolicy(new PlayPolicy.RepeatTrack());
        HCPlayer.playNextSong();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
