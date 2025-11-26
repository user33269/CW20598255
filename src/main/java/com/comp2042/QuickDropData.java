package com.comp2042;

public final class QuickDropData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    public final boolean gameOver;

    public QuickDropData(ClearRow clearRow, ViewData viewData, boolean gameOver) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.gameOver= gameOver;
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
