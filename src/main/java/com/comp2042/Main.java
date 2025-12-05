package com.comp2042;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class serves as the entry of TetrisJFX application.
 * It initialises JavaFX, loads home screen layout configure primary window settings.
 */
public class Main extends Application {
    /**
     * Starts JavaFX application by setting up main window and loading home screen layout.
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception if FXML file could not be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneLoader.setStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("/homeLayout.fxml"));

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setWidth(736*1.2);
        primaryStage.setHeight(460*1.2);
        primaryStage.show();

    }

    /**
     * Launches JavaFX runtime.
     * @param args runtime arguments (unused)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
