package com.comp2042;

/**
 * This is an immutable data class representing the state of a Tetris brick for rendering.
 * It contains information such as current brick shape, position, next brick shape and ghost brick position.
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final int ghostY;

    /**
     * Constructs a new ViewData object with given brick information.
     * @param brickData 2D array of current brick's shape
     * @param xPosition current brick's x coordinate
     * @param yPosition current brick's y coordinate
     * @param nextBrickData 2D array of next brick shape
     * @param ghostY y- coordinate of ghost brick
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.ghostY= ghostY;
    }

    /**
     * Returns a copy of current brick's shape
     * @return 2D array of current brick
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns current brick's x- coordinate
     * @return x-coordinate
     */
    public int getxPosition() {
        return xPosition;
    }

    /**
     * Returns current brick's y- coordinate
     * @return y-coordinate
     */
    public int getyPosition() {
        return yPosition;
    }

    /**
     * Returns a copy of next brick shape
     * @return 2D array of next brick
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Returns y -coordinate of ghost brick
     * @return ghost brick's y-coordinate
     */
    public int getGhostY() {
        return ghostY;
    }
}
