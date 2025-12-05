package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button zenButton;

    @FXML
    private void ZenMode(ActionEvent event)throws IOException {
        GuiControllerZen guiControllerZen = SceneLoader.load("/gameLayoutZen.fxml");
        new GameControllerZen(guiControllerZen);


    }

    @FXML
    private void timeMode(ActionEvent event)throws IOException{
        GuiControllerTimeBlitz guiControllerTimeBlitz= SceneLoader.load("/gameLayoutTimeBlitz.fxml");
        GameControllerTimeBlitz game= new GameControllerTimeBlitz(guiControllerTimeBlitz);
        guiControllerTimeBlitz.setGameControllerTimeBlitz(game);
    }

    @FXML
    private void HowtoPlayMenu(){
        try{
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/HowtoplayMenu.fxml"));
            Parent root= loader.load();

            HowToPlayController controller= loader.getController();

            Stage popupStage= new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.setScene(new Scene(root,450,350));

            controller.setStage(popupStage);
            popupStage.centerOnScreen();
            popupStage.showAndWait();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
