package com.comp2042.logic.bricks;

import java.util.List;

public interface Brick {

    List<int[][]> getShapeMatrix();

    Brick clone();

    int[][] getRotation(int index);
}
