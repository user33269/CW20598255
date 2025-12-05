package com.comp2042;

import com.comp2042.logic.bricks.Brick;

/**
 * This class handles rotation logics for a brick.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Computes next rotation state of specific brick.
     * @return a brick containing next rotation matrix and its rotation index.
     */
    public NextShapeInfo getNextShape() {
        int next = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getRotation(next), next);
    }

    /**
     * Returns current rotation index of brick.
     * @return current rotation index.
     */
    public int getCurrentIndex(){
        return currentShape;
    }

    /**
     * Returns the matrix of brick's current rotated shape.
     * @return a 2D int array for current rotation matrix.
     */
    public int[][] getCurrentShape() {
        return brick.getRotation(currentShape);
    }

    /**
     * Sets brick's rotation index.
     * @param currentShape the rotation index to set.
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Assign new brick to BrickRotator and resets rotation index to 0.
     * @param brick the brick to perform rotations.
     */
    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
