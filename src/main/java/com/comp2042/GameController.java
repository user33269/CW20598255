package com.comp2042;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

public class GameController  implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);

    private int highestScore=0;
    private final String HIGHEST_SCORE_FILE= "highestScore.txt";
    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        loadHighestScore();
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());

        viewGuiController.updateHighestScore(highestScore);
    }
    private void loadHighestScore(){
        try{
            File file= new File(HIGHEST_SCORE_FILE);
            if (!file.exists()){
                highestScore=0;
                return;
            }
            Scanner scanner= new Scanner(file);
            if(scanner.hasNextInt()){
                highestScore=scanner.nextInt();
            }
            scanner.close();
        }catch (Exception e){
            e.printStackTrace();
            highestScore=0;
        }
    }

    private void saveHighestScore(){
        try{
            FileWriter writer= new FileWriter(HIGHEST_SCORE_FILE);
            writer.write(Integer.toString(highestScore));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void checkHighestScore(){
        int current= board.getScore().scoreProperty().get();
        if(current>highestScore){
            highestScore= current;
            saveHighestScore();;
            viewGuiController.updateHighestScore(highestScore);
        }
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
                checkHighestScore();
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
        checkHighestScore();
        board.mergeBrickToBackground();

        ClearRow clearRow = board.clearRows();

        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            checkHighestScore();
        }

        if (board.createNewBrick()) {
            viewGuiController.gameOver();
        }

        viewGuiController.refreshGameBackground(board.getBoardMatrix());
        return new QuickDropData(clearRow, board.getViewData());
        }

    }




