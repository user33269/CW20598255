package com.comp2042;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SceneLoader {

    private  static Stage stage;

    public static void  setStage (Stage s){
        stage=s;
    }

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
