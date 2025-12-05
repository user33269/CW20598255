package com.comp2042;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * This class represents the "Game Over" screen panel for Tetris game.
 * It is intended to be displayed when player loses and game ends.
 */
public class GameOverPanel extends BorderPane {

    /**
     * Initialise a "GAME OVER" text centered label and "gameOverStyle" CSS style class applied.
     */
    public GameOverPanel() {
        final Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("gameOverStyle");
        setCenter(gameOverLabel);
    }

}
