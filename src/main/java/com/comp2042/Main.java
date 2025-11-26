package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneLoader.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/home.fxml"));

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(736*1.2);
        primaryStage.setHeight(460*1.2);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
