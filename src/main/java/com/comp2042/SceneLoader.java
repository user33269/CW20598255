package com.comp2042;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * This is a Utility class for loading JavaFX FXML scene when switching to different game modes FXML at home screen layout.
 */
public class SceneLoader {

    private  static Stage stage;

    /**
     * Sets primary stage for application
     * @param s the primary stage of application
     */
    public static void  setStage (Stage s){
        stage=s;
    }

    /**
     * Loads an FXML file, and sets the scene on primary stage.
     * It automatically creates a new Scene from loaded FXML and display it on primary stage.
     * @param fxml the path to the FXML file
     * @return the controller of loaded FXML
     * @param <T> the type of controller class associated with the FXML file loaded.
     */
    public static <T> T load (String fxml){
        try{
            URL location= SceneLoader.class.getResource(fxml);

            FXMLLoader loader= new FXMLLoader(location);
            Parent root=loader.load();
            T controller= loader.getController();
            stage.setScene(new Scene(root));
            stage.show();

            return controller;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
