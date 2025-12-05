package com.comp2042;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button zenButton;

    @FXML
    private void ZenMode(ActionEvent event)throws IOException {
        GuiController guiController= SceneLoader.load("/gameLayout.fxml");
        GameController gameController= new GameController(guiController);


    }
}
