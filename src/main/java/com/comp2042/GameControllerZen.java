package com.comp2042;


import java.awt.*;

public class GameControllerZen implements InputEventListener {

    private GameBoard gameBoard = new SimpleGameBoard(23, 10);
    private final HighestScoreManager highestScoreManager;
    private final GuiControllerZen viewGuiController;

    public GameControllerZen(GuiControllerZen c) {
        viewGuiController = c;
        this.highestScoreManager= new HighestScoreManager("highestScoreZen.txt");

        gameBoard.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        viewGuiController.bindScore(gameBoard.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());
    }


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

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        gameBoard.moveBrickLeft();
        return gameBoard.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        gameBoard.moveBrickRight();
        return gameBoard.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        gameBoard.rotateLeftBrick();
        return gameBoard.getViewData();
    }


    @Override
    public void createNewGame() {
        gameBoard.newGame();
        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        ViewData viewData = gameBoard.holdBrick();

        int[][] heldShape = gameBoard.getHeldBrickShape();
        viewGuiController.updateHeldBrick(heldShape);
        return viewData;
    }

    @Override
    public int[][] getHeldBrickShape() {
        return gameBoard.getHeldBrickShape();}

    @Override
    public Point getGhostBrickPosition () {
            return gameBoard.getGhostBrickPosition();
    }


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




