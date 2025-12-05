package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public NextShapeInfo getNextShape() {
        int next = (currentShape + 1) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getRotation(next), next);
    }

    public int getCurrentIndex(){
        return currentShape;
    }

    public int[][] getCurrentShape() {
        return brick.getRotation(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
