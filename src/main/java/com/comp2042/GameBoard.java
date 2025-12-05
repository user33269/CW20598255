package com.comp2042;

import java.awt.*;

/**
 * This interface represents the main operations of Tetris GameBoard such as bricks movements,
 * board state updates, bricks collision, line clearing, scoring and view data
 */

public interface GameBoard {

    /**
     * It moves brick down one row downwards.
     * @return true if brick is moved successfully, false otherwise
     */
    boolean moveBrickDown();

    /**
     * It checks if brick can still move further.
     * If couldnt it is merged onto the board, rows filled are cleared, scores updated and new brick may spawn.
     *
     * @return DownData containing cleared rows info, view data and gameOver status
     */
    DownData moveDown();
    /**
     * It moves brick down one column to the left
     * @return true if brick is moved successfully, false otherwise
     */
    boolean moveBrickLeft();
    /**
     * It moves brick down one column to the right
     * @return true if brick is moved successfully, false otherwise
     */
    boolean moveBrickRight();
    /**
     * It rotates brick anti-clockwise
     * @return true if brick is rotated successfully, false otherwise
     */
    boolean rotateLeftBrick();
    /**
     * It created new falling brick.
     * If new brick collides with GameOver area , game is considered over.
     * @return true if brick if gameOver
     */
    boolean createNewBrick();

    /**
     *Returns current state of game board as a matrix.
     * @return current gameBoard matrix containing all placed bricks.
     */
    int[][] getBoardMatrix();

    /**
     * Produces view-related data for rendering
     * @return ViewData including active brick and its position, next brick and ghost brick position
     */
    ViewData getViewData();

    /**
     * Merges currect activie brick to background matrix once it couldnt move downwards anymore
     */
    void mergeBrickToBackground();

    /**
     * Clears any filled rows from the board
     * @return ClearRow containing rows removed and updated matrix
     */
    ClearRow clearRows();

    /**
     *Return player's current score.
     * Score object tracks points earned from line clears and other scoreBonus.
     * @return tracked player's score
     */
    Score getScore();

    /**
     * Resets gameboard and score, and span a new brick to start game
     */
    void newGame();

    /**
     * Execute holdbrick mechanices, allow storing of current brick and swapping with previous held brick.
     * @return updated ViewData after hold action
     */
    ViewData holdBrick();

    /**
     * Return shape matrix of held brick.
     * @return shape matrix of heldbrick, null if none is held
     */
    int[][] getHeldBrickShape();

    /**
     * Computes landing coordinates of ghost brick piece
     * It shows where the current brick would fall without player's input
     * @return a Point representing ghost brick position
     */
    Point getGhostBrickPosition();

    /**
     * Drops current brick downwards immediately.
     * Completed rows will be cleared and ScoreBonus calculated.
     * @return QuickDropData containing cleared rows info, viewdata,gameOver status, and drop distance.
     */
    QuickDropData quickDrop();
}
