package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * This class represents player's score in the game.
 */
public final class Score {

    private final IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * Returns the IntegerProperty representing current score.
     * @return score property.
     */
    public IntegerProperty scoreProperty() {
        return score;
    }

    /**
     * Increases current score by certain specified amount.
     * @param i amount to add to current score
     */
    public void add(int i){
        score.setValue(score.getValue() + i);
    }

    /**
     * Resets score to zero.
     */
    public void reset() {
        score.setValue(0);
    }


}
