package com.comp2042;

/**
 * This is an immutable class that gives information about next rotation of a Tetris Brick.
 * It holds rotated shape and rotation index of the brick.
 */
public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Contruct a new instance with specified shape and position
     * @param shape 2D matrix of brick shape
     * @param position the index of this rotation
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns a copy of the brick shape
     * @return a 2D array of the brick shape.
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns rotation index of the shape
     * @return the position of this rotation within the brick's rotation sequence.
     */
    public int getPosition() {
        return position;
    }
}
