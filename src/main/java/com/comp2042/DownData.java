package com.comp2042;

/**
 * This is an immutable class that contains the result of attempting to move current brick downwards.
 * It contains:
 * <ul>
 *     <li>any row-clear information from brick moving downwards</li>
 *     <li>updated view data for game board</li>
 *     <li>a flag indicating if game is over. </li>
 * </ul>
 */
public final class DownData {
    private final ClearRow clearRow;
    private final ViewData viewData;
    private final boolean gameOver;

    /**
     * creates new DownData result object
     * @param clearRow information about cleared rows.
     * @param viewData updated view data after downwards movement
     * @param gameOver returns if the move cause game to end.
     */
    public DownData(ClearRow clearRow, ViewData viewData, boolean gameOver) {
        this.clearRow = clearRow;
        this.viewData = viewData;
        this.gameOver= gameOver;
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
