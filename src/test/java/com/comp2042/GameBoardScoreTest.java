package com.comp2042;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardScoreTest {
    private SimpleGameBoard board;

    @BeforeEach
    void setup(){
        board= new SimpleGameBoard(10,23);
        board.newGame();
    }
    public ClearRow clearRowTest(int rowIndex) {
        int[][] currentGameMatrix= board.getBoardMatrix();
        int[][] rowMajorMatrix = new int[currentGameMatrix[0].length][currentGameMatrix.length];
        for (int col = 0; col < currentGameMatrix.length; col++) {
            for (int row = 0; row < currentGameMatrix[0].length; row++) {
                rowMajorMatrix[row][col] = currentGameMatrix[col][row];
            }
        }

        for (int col = 0; col < rowMajorMatrix[0].length; col++) {
            rowMajorMatrix[rowIndex][col] = 1;
        }

        return MatrixOperations.checkRemoving(rowMajorMatrix);
    }

    @Test
    void testClearOneLineScoreBonus(){

        ClearRow clearRow= clearRowTest(22);
        board.getScore().add(clearRow.getScoreBonus());

        assertEquals(50, clearRow.getScoreBonus());
        assertEquals(50,board.getScore().scoreProperty().get());
    }

    @Test
    void testSoftDropPointsadded(){
        int intialScore= board.getScore().scoreProperty().get();

        for(int i=0; i<3; i++){
            board.moveBrickDown();
            board.getScore().add(1);
        }

        assertEquals(intialScore+3, board.getScore().scoreProperty().get());
    }

    @Test
    void testHardDropPointsAdded(){
        int initialScore= board.getScore().scoreProperty().get();

        QuickDropData data= board.quickDrop();
        int dropDistance= data.getDropDistance();

        assertEquals(initialScore+dropDistance*2, board.getScore().scoreProperty().get());
    }


}