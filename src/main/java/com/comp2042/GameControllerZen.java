package com.comp2042;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class GameControllerZen implements InputEventListener {

    private GameBoard gameBoard = new SimpleGameBoard(23, 10);

    private int highestScoreZen=0;
    private final String HIGHEST_SCORE_FILE_ZEN = "highestScoreZen.txt";
    private final GuiControllerZen viewGuiController;

    public GameControllerZen(GuiControllerZen c) {
        viewGuiController = c;
        loadHighestScoreZen();
        gameBoard.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(gameBoard.getBoardMatrix(), gameBoard.getViewData());
        viewGuiController.bindScore(gameBoard.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScoreZen);
    }
    private void loadHighestScoreZen(){
        try{
            File file= new File(HIGHEST_SCORE_FILE_ZEN);
            if (!file.exists()){
                highestScoreZen=0;
                return;
            }
            Scanner scanner= new Scanner(file);
            if(scanner.hasNextInt()){
                highestScoreZen=scanner.nextInt();
            }
            scanner.close();
        }catch (Exception e){
            e.printStackTrace();
            highestScoreZen=0;
        }
    }

    private void saveHighestScore(){
        try{
            FileWriter writer= new FileWriter(HIGHEST_SCORE_FILE_ZEN);
            writer.write(Integer.toString(highestScoreZen));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void checkHighestScore(){
        int current= gameBoard.getScore().scoreProperty().get();
        if(current>highestScoreZen){
            highestScoreZen= current;
            saveHighestScore();
            viewGuiController.updateHighestScore(highestScoreZen);
        }
    }


    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = gameBoard.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            
            if (gameBoard.getLockDelayStart()<0){
                gameBoard.setLockDelayStart(System.currentTimeMillis());
                return new DownData(null, gameBoard.getViewData());
            }

            if (System.currentTimeMillis()- gameBoard.getLockDelayStart()< gameBoard.getMaxLockDelay()){
                return new DownData(null, gameBoard.getViewData());
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
                checkHighestScore();
            }
        }
        return new DownData(clearRow, gameBoard.getViewData());
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
        checkHighestScore();
        gameBoard.mergeBrickToBackground();

        ClearRow clearRow = gameBoard.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            gameBoard.getScore().add(clearRow.getScoreBonus());
            checkHighestScore();
        }

        if (gameBoard.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(gameBoard.getBoardMatrix());
        return new QuickDropData(clearRow, gameBoard.getViewData());
        }

    }




