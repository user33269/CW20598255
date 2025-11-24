package com.comp2042;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HowToPlayController {
    @FXML
    private Button closeButton;

    private Stage stage;

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
