package com.comp2042;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerZenTest {

    private GameControllerZen gameControllerZen;
    private TestGuiController testGui;

    @BeforeEach
    void setup(){
        testGui= new TestGuiController();
        gameControllerZen = new GameControllerZen(testGui);
    }


    @Test
    void testHoldBrick(){
        ViewData result= gameControllerZen.onHoldEvent(
                new MoveEvent(EventSource.USER)
        );

        //test if ViewData is returned
        assertTrue(result != null);

        //test if gui is updated
        assertTrue(testGui.updateHeldBrickCalled);

        //test if held shape is stored
        assertTrue(testGui.lastHeldShape != null);
    }


    //mock guicontroller
    public static class TestGuiController extends GuiControllerZen {
        public boolean updateHeldBrickCalled= false;
        public int[][] lastHeldShape= null;

        @Override
        public void updateHeldBrick(int [][]shape){
            updateHeldBrickCalled=true;
            lastHeldShape=shape;
        }

        @Override public void setEventListener(InputEventListener eventListener) {}
        @Override public void initGameView(int[][] board, ViewData viewData) {}
        @Override public void refreshGameBackground(int[][] board) {}
        @Override public void bindScore(javafx.beans.property.IntegerProperty scoreProperty) {}
        @Override public void updateHighestScore(int score) {}
        @Override public void gameOver() {}

    }
}