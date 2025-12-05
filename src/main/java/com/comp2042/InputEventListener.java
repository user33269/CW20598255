package com.comp2042;

import java.awt.*;

/**
 * This is the listening interface that define all user input events for Tetris game.
 * Implementations of this interface will respond to keyboard inputs.
 */
public interface InputEventListener {

    /**
     * Handles event when user presses "Down" key
     * @param event information about the move event
     * @return a Downdata object containing updated game state after movement.
     */
    DownData onDownEvent(MoveEvent event);
    /**
     * Handles event when user presses "Left" key
     * @param event information about the move event
     * @return updated ViewData after movement.
     */
    ViewData onLeftEvent(MoveEvent event);
    /**
     * Handles event when user presses "Right" key
     * @param event information about the move event
     * @return updated ViewData after movement.
     */
    ViewData onRightEvent(MoveEvent event);
    /**
     * Handles event when user presses "Rotate" key
     * @param event information about the move event
     * @return updated ViewData after movement.
     */
    ViewData onRotateEvent(MoveEvent event);

    /**
     * Starts a new game.
     */
    void createNewGame();
    /**
     * Handles event when user presses "QuickDrop" key
     * @param event information about the move event
     * @return QuickDropData object containing landing position and score gained.
     */
    QuickDropData onQuickDropEvent(MoveEvent event);
    /**
     * Handles event when user presses "Hold" key
     * @param event information about the move event
     * @return updated ViewData of new held brick.
     */
    ViewData onHoldEvent(MoveEvent event);

    /**
     * Returns shape matrix of currently held brick
     * @return 2D int array of held brick shape.
     */
    int[][] getHeldBrickShape();

    /**
     * Returns the ghost brick's position
     * @return a Point representing ghost brick location.
     */
    Point getGhostBrickPosition();

}
