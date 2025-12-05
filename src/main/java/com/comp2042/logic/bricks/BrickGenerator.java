package com.comp2042.logic.bricks;

/**
 * This is the interface for generating Tetris bricks.
 * It contains methods to obtain current shape and preview of next brick.
 */
public interface BrickGenerator {
    /**
     * Returns current brick that to be placed.
     * @return Brick representing current brick.
     */
    Brick getBrick();

    /**
     * Returns next brick that will appear.
     * @return Brick representing upcoming brick.
     */
    Brick getNextBrick();
}
