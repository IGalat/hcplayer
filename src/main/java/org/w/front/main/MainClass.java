/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.w.front.main;

//import com.fnaf.CallMePlayer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Random;

/**
 * @author admin
 */
public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) {

        //StackPane root = new StackPane();
        Pane root = new Pane();
        int width = 1024;
        int height = 768;

        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setLayoutX(width / 2);
        btn.setLayoutY(height / 2);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Hello World!");
                for (int i = 0; i < 100; i++) {
                    Button btn = new Button();
                    btn.setText("Say 'Hello World'");
                    btn.setLayoutX(new Random().nextInt(width));
                    btn.setLayoutY(new Random().nextInt(height));
                    btn.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Hello World!");
                            alert.showAndWait();
                        }
                    });
                    root.getChildren().add(btn);
                }
            }
        });

        root.getChildren().add(btn);

        Scene scene = new Scene(root, width, height);

        primaryStage.setTitle("hcplayer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
