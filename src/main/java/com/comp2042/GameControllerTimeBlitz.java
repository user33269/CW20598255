package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GameControllerTimeBlitz implements InputEventListener {

    private Board board = new SimpleBoard(23, 10);

    private int highestScoreTime =0;
    private final String HIGHEST_SCORE_FILE_TIME = "highestScore_timeblitz.txt";
    private final GuiControllerTimeBlitz viewGuiController;

    private Timeline timeLine;
    public GameControllerTimeBlitz(GuiControllerTimeBlitz c) {
        viewGuiController = c;
        loadHighestScoreTime();
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

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
        int current= board.getScore().scoreProperty().get();
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
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            
            if (board.getLockDelayStart()<0){
                board.setLockDelayStart(System.currentTimeMillis());
                return new DownData(null,board.getViewData());
            }

            if (System.currentTimeMillis()- board.getLockDelayStart()<board.getMaxLockDelay()){
                return new DownData(null,board.getViewData());
            }

            board.setLockDelayStart(-1);
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {

            board.setLockDelayStart(-1);
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
                checkHighestScoreTime();
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        ViewData viewData = board.holdBrick();

        int[][] heldShape = board.getHeldBrickShape();
        viewGuiController.updateHeldBrick(heldShape);
        return viewData;
    }

    @Override
    public int[][] getHeldBrickShape() {
        return board.getHeldBrickShape();}

    @Override
    public Point getGhostBrickPosition () {
            return board.getGhostBrickPosition();
    }


    @Override
    public QuickDropData onQuickDropEvent(MoveEvent event){
        int dropDistance=0;

        boolean canMove = true;
        while (canMove) {
                canMove = board.moveBrickDown();
                if(canMove) dropDistance++;
        }
        board.mergeBrickToBackground();

        board.getScore().add(dropDistance*2);
        checkHighestScoreTime();
        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            checkHighestScoreTime();
        }

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new QuickDropData(clearRow, board.getViewData());
        }

    }




