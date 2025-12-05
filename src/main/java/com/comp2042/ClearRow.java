package com.comp2042;

/**
 * This is an immutable class that contains the results of when rows are cleared in the Tetris game.
 * This class stores:
 * <ul>
 *     <li>number of cleared lines</li>
 *     <li>updated game matrix after rows cleared</li>
 *     <li>compute any scoreBonus obtained for clearing rows.</li>
 * </ul>
 */
public final class ClearRow {

    private final int linesRemoved;
    private final int[][] newMatrix;
    private final int scoreBonus;

    /**
     * Creates a new ClearRow result object .
     * @param linesRemoved is the number of lines cleared.
     * @param newMatrix is the updated board matrix after lines cleared.
     * @param scoreBonus is the bonus score awarded for clearing rows.
     */
    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    }

    /**
     * Returns the number of rows removed when lines were cleared.
     * @return number of cleared rows.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Returns a copy of updated game matrix after lines cleared.
     * @return a copy of updated game matrix.
     */
    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix);
    }

    /**
     * Returns the score bonus awarded for row clearing.
     * @return awarded score bonus
     */
    public int getScoreBonus() {
        return scoreBonus;
    }
}
