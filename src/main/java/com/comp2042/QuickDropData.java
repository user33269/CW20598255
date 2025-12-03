package com.comp2042;

public final class QuickDropData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    public final boolean gameOver;
    private final int dropDistance;

    public QuickDropData(ClearRow clearRow, ViewData viewData, boolean gameOver, int dropDistance) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.gameOver= gameOver;
        this.dropDistance= dropDistance;
    }

    public int getDropDistance(){
        return dropDistance;
    }
    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }

    public boolean isGameOver(){
        return gameOver;
    }
}
