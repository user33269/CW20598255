package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.awt.*;

public class GameControllerTimeBlitz implements InputEventListener {

    private GameBoard gameBoard = new SimpleGameBoard(23, 10);

    private final HighestScoreManager highestScoreManager;
    private final GuiControllerTimeBlitz viewGuiController;

    private Timeline timeLine;
    private TimeManager timeManager;

    public GameControllerTimeBlitz(GuiControllerTimeBlitz c) {
        viewGuiController = c;
        this.highestScoreManager= new HighestScoreManager("highestScore_timeblitz.txt");

        gameBoard.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        viewGuiController.bindScore(gameBoard.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());

        setupBrickFallTimer();
        setupCountdownTimer();

    }

    private void setupBrickFallTimer(){
        timeLine= new Timeline(new KeyFrame(
                Duration.millis(600),
                e-> onDownEvent(new MoveEvent(
                        EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();

    }

    private void setupCountdownTimer(){
        timeManager= new TimeManager(120, new TimeManager.TimeListener() {

            @Override
            public void updateTime(int timeLeft){
                viewGuiController.updateTime(timeLeft);
            }

            @Override
            public void endTime(){
                endTimeblitz();
            }
        });
        timeManager.start();
    }

    public void stopTimer(){
        timeManager.stop();
    }

    public void pauseTimer(){
        timeManager.pause();
    }
    public void resumeTimer(){
        timeManager.resume();
    }

    public void restartTimer(){
        timeManager.restart(120);
        viewGuiController.updateTime(120);
    }

    private void endTimeblitz(){
        timeLine.stop();
        int current= gameBoard.getScore().scoreProperty().get();
        highestScoreManager.checkHighestScore(current);
        viewGuiController.updateHighestScore(highestScoreManager.getHighestScore());
        viewGuiController.gameOver();
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




