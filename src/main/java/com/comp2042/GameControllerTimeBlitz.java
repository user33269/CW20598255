package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GameControllerTimeBlitz implements InputEventListener {

    private GameBoard gameBoard = new SimpleGameBoard(23, 10);

    private int highestScoreTime =0;
    private final String HIGHEST_SCORE_FILE_TIME = "highestScore_timeblitz.txt";
    private final GuiControllerTimeBlitz viewGuiController;

    private Timeline timeLine;
    public GameControllerTimeBlitz(GuiControllerTimeBlitz c) {
        viewGuiController = c;
        loadHighestScoreTime();
        gameBoard.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        viewGuiController.bindScore(gameBoard.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScoreTime);

        timeLine= new Timeline(new KeyFrame(
                Duration.millis(700),
                e-> onDownEvent(new MoveEvent(
                        EventType.DOWN,
                        EventSource.THREAD))
                ));
                timeLine.setCycleCount(Timeline.INDEFINITE);
                timeLine.play();

                startCountdownTimer();

    }
    private void loadHighestScoreTime(){
        try{
            File file= new File(HIGHEST_SCORE_FILE_TIME);
            if (!file.exists()){
                highestScoreTime =0;
                return;
            }
            Scanner scanner= new Scanner(file);
            if(scanner.hasNextInt()){
                highestScoreTime =scanner.nextInt();
            }
            scanner.close();
        }catch (Exception e){
            e.printStackTrace();
            highestScoreTime =0;
        }
    }

    private void saveHighestScoreTime(){
        try{
            FileWriter writer= new FileWriter(HIGHEST_SCORE_FILE_TIME);
            writer.write(Integer.toString(highestScoreTime));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void checkHighestScoreTime(){
        int current= gameBoard.getScore().scoreProperty().get();
        if(current> highestScoreTime){
            highestScoreTime = current;
            saveHighestScoreTime();
            viewGuiController.updateHighestScore(highestScoreTime);
        }
    }

    private int timeLeft= 120;
    private Timeline countdownTimer;

    private void startCountdownTimer(){
        countdownTimer= new Timeline(new KeyFrame(
                Duration.seconds(1),
                e->{
                    timeLeft--;
                    viewGuiController.updateTime(timeLeft);

                if(timeLeft<=0){
                    countdownTimer.stop();
                    endTimeBlitz();
        }}

        ));
        countdownTimer.setCycleCount(Timeline.INDEFINITE);
        countdownTimer.play();
    }

    private void endTimeBlitz(){
        timeLine.stop();
        checkHighestScoreTime();
        viewGuiController.gameOver();
    }

    public void stopTimer(){
        if(countdownTimer != null){
            countdownTimer.stop();
        }
    }

    public void pauseTimer(){
        if(countdownTimer != null){
            countdownTimer.pause();
        }
    }
    public void resumeTimer(){
        if(countdownTimer != null){
            countdownTimer.play();
        }
    }

    public void restartTimer(){
        countdownTimer.stop();
        timeLeft=120;
        viewGuiController.updateTime(timeLeft);
        startCountdownTimer();
    }




    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = gameBoard.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            
            if (gameBoard.getLockDelayStart()<0){
                gameBoard.setLockDelayStart(System.currentTimeMillis());
                return new DownData(null, gameBoard.getViewData(), false);
            }

            if (System.currentTimeMillis()- gameBoard.getLockDelayStart()< gameBoard.getMaxLockDelay()){
                return new DownData(null, gameBoard.getViewData(), false);
            }

            gameBoard.setLockDelayStart(-1);
            gameBoard.mergeBrickToBackground();
            clearRow = gameBoard.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                gameBoard.getScore().add(clearRow.getScoreBonus());
            }
            if (gameBoard.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());

        } else {

            gameBoard.setLockDelayStart(-1);
            if (event.getEventSource() == EventSource.USER) {
                gameBoard.getScore().add(1);
                checkHighestScoreTime();
            }
        }
        return new DownData(clearRow, gameBoard.getViewData(), false);
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
        int dropDistance=0;

        boolean canMove = true;
        while (canMove) {
                canMove = gameBoard.moveBrickDown();
                if(canMove) dropDistance++;
        }
        gameBoard.mergeBrickToBackground();

        gameBoard.getScore().add(dropDistance*2);
        checkHighestScoreTime();
        gameBoard.mergeBrickToBackground();

        ClearRow clearRow = gameBoard.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            gameBoard.getScore().add(clearRow.getScoreBonus());
            checkHighestScoreTime();
        }

        if (gameBoard.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());
        return new QuickDropData(clearRow, gameBoard.getViewData(),false    );
        }

    }




