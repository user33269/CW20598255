package com.comp2042.logic.bricks;

import com.comp2042.MatrixOperations;

import java.util.ArrayList;
import java.util.List;

final class OBrick implements Brick {

    private final List<int[][]> brickMatrix = new ArrayList<>();

    public OBrick() {
        brickMatrix.add(new int[][]{
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        });
    }
    private OBrick(OBrick other){
        for (int[][] shape: other.brickMatrix){
            brickMatrix.add(MatrixOperations.deepCopy(shape));
        }
    }
    @Override
    public int[][] getRotation(int index){
        return brickMatrix.get(index);
    }
    @Override
    public Brick clone() {
        return new OBrick(this);
    }
    @Override
    public List<int[][]> getShapeMatrix() {
        return MatrixOperations.deepCopyList(brickMatrix);
    }

}
