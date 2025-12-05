package com.comp2042;

import java.awt.*;

public interface Board {

    boolean moveBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

    ViewData holdBrick();

    int[][] getHeldBrickShape();

    Point getGhostBrickPosition();

    long getLockDelayStart();

    void setLockDelayStart(long value);

    long getMaxLockDelay();
}
