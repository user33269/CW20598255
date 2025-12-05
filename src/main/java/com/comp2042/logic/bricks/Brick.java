package com.comp2042.logic.bricks;

import java.util.List;

/**
 * This is an interface representing a Tetris brick with multiple rotations.
 * It provides access to shape matrix and individual rotation.
 */
public interface Brick {
    /**
     * Returns a list of all rotation shapes of bricks
     * @return a List of 2D arrays representing all rotations.
     */
    List<int[][]> getShapeMatrix();

    /**
     * Returns shape of brick at a specific rotation index
     * @param index the rotation index
     * @return a 2D array of brick in the specified rotation index.
     */
    int[][] getRotation(int index);
}
