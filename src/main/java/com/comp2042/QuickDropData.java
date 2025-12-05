package com.comp2042;

/**
 * This is an immutable class that contains the result of attempting to quickdropping current brick downwards.
 * It contains:
 * <ul>
 *     <li>any row-clear information from brick moving downwards</li>
 *     <li>updated view data for game board</li>
 *     <li>a flag indicating if game is over. </li>
 *     <li> drop distance </li>
 * </ul>
 */
public final class QuickDropData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    public final boolean gameOver;
    private final int dropDistance;

    /**
     * creates new QuickDropData result object
     * @param clearRow information about cleared rows.
     * @param viewData updated view data after downwards movement
     * @param gameOver returns if the move cause game to end.
     * @param dropDistance the number of rows brick has moved down when quickdropping.
     */
    public QuickDropData(ClearRow clearRow, ViewData viewData, boolean gameOver, int dropDistance) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.gameOver= gameOver;
        this.dropDistance= dropDistance;
    }

    /**
     * Returns distance of brick has quickdropped.
     * @return number or rows the brick has dropped during quickdrop.
     */
    public int getDropDistance(){
        return dropDistance;
    }
    /**
     * Returns information about any cleared rows.
     * @return a ClearRow object or null if no rows are cleared.
     */
    public ClearRow getClearRow() {
        return clearRow;
    }
    /**
     * Returns updated view data representing the current state of board after downwards movement.
     * @return updated view data
     */
    public ViewData getViewData() {
        return viewData;
    }
    /**
     * Returns whether game is over after this downward movement.
     * @return true if game is over, false if otherwise.
     */
    public boolean isGameOver(){
        return gameOver;
    }
}
