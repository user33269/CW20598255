package com.comp2042;


import java.awt.*;

/**
 * This is the GameController class for Zen mode of the Tetris game.
 * This controller manages game logic, scoring, and updates to the UI.
 */
public class GameControllerZen implements InputEventListener {

    private GameBoard gameBoard = new SimpleGameBoard(23, 10);
    private final HighestScoreManager highestScoreManager;
    private final GuiControllerZen viewGuiController;

    /**
     * Creates a new controller for Zen mode.
     * @param c the GUI controller responsible for rendering this game mode game state.
     */
    public GameControllerZen(GuiControllerZen c) {
        viewGuiController = c;
        this.highestScoreManager= new HighestScoreManager("highestScoreZen.txt");

        gameBoard.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        viewGuiController.bindScore(gameBoard.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());
    }

    /**
     * Handles down movement event that is either triggered by user or thread.
     * If game is over,timer is stopped and triggers GUI's gameOver handler.
     * @param event the movement event containing its source
     * @return DownData containing updated state information.
     */
    @Override
    public DownData onDownEvent(MoveEvent event) {
        DownData data= gameBoard.moveDown();

        if(event.getEventSource()== EventSource.USER){
            gameBoard.getScore().add(1);
            highestScoreManager.checkHighestScore(gameBoard.getScore().scoreProperty().get());
            viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());
        }

        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());

        if (data.isGameOver()){
            viewGuiController.gameOver();
        }
        return data;
    }

    /**
     * Handles move Left input event by shifting current brick one column to the left.
     * Brick's new position is updated on the gameBoard.
     * @param event the movement event containing its source.
     * @return ViewData of updated game state after action.
     */
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        gameBoard.moveBrickLeft();
        return gameBoard.getViewData();
    }

    /**
     * Handles move Right input event by shifting current brick one column to the right.
     * Brick's new position is updated on the gameBoard.
     * @param event the movement event containing its source.
     * @return ViewData of updated game state after action.
     */
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        gameBoard.moveBrickRight();
        return gameBoard.getViewData();
    }
    /**
     * Handles rotate input event by rotating current brick.
     * Brick's new shape is updated on the gameBoard if movement is valid.
     * @param event the movement event containing its source.
     * @return ViewData of updated game state after action.
     */
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        gameBoard.rotateLeftBrick();
        return gameBoard.getViewData();
    }

    /**
     * Starts a new game by resetting game board and refreshing UI.
     */
    @Override
    public void createNewGame() {
        gameBoard.newGame();
        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());

    }
    /**
     * Handles hold brick input event.
     * If conditions met, swaps current brick with held brick and updates UI.
     * @param event the movement event that triggered this action.
     * @return updated viewData of current game state after holding brick
     */
    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        ViewData viewData = gameBoard.holdBrick();

        int[][] heldShape = gameBoard.getHeldBrickShape();
        viewGuiController.updateHeldBrick(heldShape);
        return viewData;
    }

    /**
     * Returns shape matrix of currently held brick
     * @return a 2D int array of currently held brick shape, returns null if no shape held.
     */
    @Override
    public int[][] getHeldBrickShape() {
        return gameBoard.getHeldBrickShape();}
    /**
     * Returns position of ghost brick.
     * @return a Point representing ghost brick position.
     */
    @Override
    public Point getGhostBrickPosition () {
            return gameBoard.getGhostBrickPosition();
    }

    /**
     * Handles quickdrop input event by instantly drop current brick downwards till it collides.
     * Updates new board state, scores and GUI.
     * @param event the movement event that triggered this action.
     * @return QuickDropData that contains updated game state.
     */
    @Override
    public QuickDropData onQuickDropEvent(MoveEvent event){
        QuickDropData data= gameBoard.quickDrop();

        if(data.isGameOver()){
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());
        viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());

        return data;
    }

}




