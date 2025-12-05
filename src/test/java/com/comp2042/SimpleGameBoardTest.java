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
    void rotateLeftBrick() {
        int beforeRotation= board.getRotationIndex();

        boolean result= board.rotateLeftBrick();
        int afterRotation= board.getRotationIndex();

        assertTrue(result);
        assertNotEquals(beforeRotation,afterRotation);
    }

    @Test
    void QuickDrop() {
        Point before= board.getCurrentOffset();
        Point after= board.getCurrentOffset();
        int initialScore= board.getScore().scoreProperty().get();
        int finalScore= board.getScore().scoreProperty().get();

        board.quickDrop();

        //test is brick falls to bottom
        assertTrue(after.y>before.y);

        //test if there's an increase of score
        assertTrue(finalScore>initialScore);
    }

    @Test
    void moveDown() {

        //when brick falls to the bottom
        while(board.moveBrickDown()){}
        Point landed= board.getCurrentOffset();
        Point newPosition= board.getCurrentOffset();

        //test if new brick is spawned when currect brick reached the bottom
        assertNotEquals(landed.y,newPosition.y);

    }

    @Test
    void checkforCollision() {
        Point p = board.getCurrentOffset();

        //test when there's no collision
        boolean noCollision= board.checkforCollision(p, null);
        assertFalse(noCollision);

        //test for true case of collision
        int[][]matrix= board.getBoardMatrix();
        matrix[p.y][p.x]=1;

        boolean collision= board.checkforCollision(p,null);
        assertTrue(collision);
    }
}