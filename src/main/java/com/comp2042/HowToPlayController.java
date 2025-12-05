package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This is the controller for the "How to Play" pop-up window on homescreen.
 * It handles button to close the pop-up window.
 */
public class HowToPlayController {
    @FXML
    private Button closeButton;

    private Stage stage;

    /**
     * Injects the stage of this pop up window.
     * @param stage The stage representing this pop-up window.
     */
    public void setStage(Stage stage){
        this.stage= stage;

    }

    @FXML
    private void initialize(){
        closeButton.setOnAction(e->{
            if(stage!= null){
                stage.close();
            }
        });


    }

}
