package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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

}
