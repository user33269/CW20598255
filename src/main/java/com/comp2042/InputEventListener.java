package com.comp2042;

import java.awt.*;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    void createNewGame();

    QuickDropData onQuickDropEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event);

    int[][] getHeldBrickShape();

    Point getGhostBrickPosition();

}
