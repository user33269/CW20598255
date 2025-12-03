package com.comp2042;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
class SimpleGameBoardTest {

    private SimpleGameBoard board;

    @BeforeEach
    void setup(){
        board= new SimpleGameBoard(10,23);
        board.createNewBrick();
    }

    @Test
    void getGhostBrickPosition() {

        Point spawn= board.getCurrentOffset();
        Point ghost= board.getGhostBrickPosition();

        assertTrue(ghost.y>= spawn.y);
        assertTrue(ghost.y< board.getBoardHeight());

    }

    @Test
    void moveBrickDown() {

        Point before= board.getCurrentOffset();

        boolean result= board.moveBrickDown();
        Point after= board.getCurrentOffset();

        assertTrue(result);
        assertEquals(before.y+1, after.y);
    }

    @Test
    void moveBrickLeft() {

        Point before= board.getCurrentOffset();

        boolean result= board.moveBrickLeft();
        Point after= board.getCurrentOffset();

        assertTrue(result);
        assertEquals(before.x-1, after.x);
    }

    @Test
    void moveBrickRight(){
        Point before= board.getCurrentOffset();

        boolean result= board.moveBrickRight();
        Point after= board.getCurrentOffset();

        assertTrue(result);
        assertEquals(before.x+1, after.x);
    }

    @Test
    void rotateLeftBrick() {

        int beforeRotation= board.getRotationIndex();

        boolean result= board.rotateLeftBrick();
        int afterRotation= board.getRotationIndex();

        assertTrue(result);
        assertNotEquals(beforeRotation,afterRotation);
    }

    @Test
    void QuickDrop() {

        int initialScore= board.getScore().scoreProperty().get();
        QuickDropData data = board.quickDrop();

        int finalScore= board.getScore().scoreProperty().get();

        //test is brick falls to bottom
        assertTrue(data.getDropDistance()>0);

        //test if there's an increase of score
        assertTrue(finalScore>initialScore);
    }



}