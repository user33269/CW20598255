package com.comp2042;

public final class QuickDropData {
    private final ClearRow clearRow;
    private final ViewData viewData;

    public QuickDropData(ClearRow clearRow, ViewData viewData) {
        this.clearRow = clearRow;
        this.viewData = viewData;
    }

    public ClearRow getClearRow() {
        return clearRow;
    }

    public ViewData getViewData() {
        return viewData;
    }
}
