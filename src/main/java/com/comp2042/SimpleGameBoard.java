package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleGameBoard implements GameBoard {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;
    private Brick heldBrick= null ;
    private boolean canHold=true;
    private Brick currentBrick;
    private int heldRotation;

    public SimpleGameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    public Point getGhostBrickPosition(){
       Point ghost= new Point(currentOffset);
       while(true){
           Point next= new Point(ghost.x,ghost.y+1);
           boolean conflict= MatrixOperations.intersect(
                   currentGameMatrix,
                   brickRotator.getCurrentShape(),
                   (int)next.getX(),
                   (int)next.getY()
           );
           if (conflict) break;
           ghost= next;
       }
       return ghost;
    }

    private long lockDelayStart =-1;
    private static final long MAX_LOCK_DELAY =500;

    @Override
    public long getLockDelayStart(){
        return  lockDelayStart;
    }
    @Override
    public void setLockDelayStart(long value){
        lockDelayStart=value;
    }
    @Override
    public long getMaxLockDelay(){
        return  MAX_LOCK_DELAY;
    }

    @Override
    public int[][] getHeldBrickShape(){
        if (heldBrick == null) return null;

        return heldBrick.getRotation(heldRotation);
    }

    @Override
    public boolean moveBrickDown() {

        int[][] currentMatrix = currentGameMatrix;
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    public DownData moveDown(){

        boolean canMove = moveBrickDown();
        ClearRow clearRow;

        if (!canMove) {

            mergeBrickToBackground();

            clearRow = clearRows();
            if (clearRow.getLinesRemoved() > 0) {
               getScore().add(clearRow.getScoreBonus());
            }

            boolean gameOver= createNewBrick();

            return new DownData(clearRow,getViewData(), gameOver);
        } else {
            return new DownData(null, getViewData(), false);
        }

    }

    public QuickDropData quickDrop(){
        int dropDistance=0;

        while (moveBrickDown()) {
            dropDistance++;
        }
        mergeBrickToBackground();

        score.add(dropDistance*2);

        ClearRow clearRow= clearRows();
        if(clearRow.getLinesRemoved()>0){
            score.add(clearRow.getScoreBonus());
        }

        boolean gameOver= createNewBrick();

        return  new QuickDropData(clearRow,getViewData(),gameOver);
    }



    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        this.currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 8);

        canHold=true;
        return MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    private int calcGhostY(){
        int ghostY= (int) currentOffset.getY();

        while(!MatrixOperations.intersect(
                currentGameMatrix,
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                ghostY
        )){
            ghostY++;
        }

        return ghostY;
    }

    @Override
    public ViewData getViewData() {
        int ghostY= calcGhostY();

        return new ViewData(
                brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0),ghostY);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    @Override
    public ViewData holdBrick() {
        if (!canHold || currentBrick == null){
            return getViewData();
        }

        if (heldBrick==null){

            heldBrick= currentBrick.clone();
            heldRotation= brickRotator.getCurrentIndex();
            createNewBrick();
        }else{
            Brick tempBrick= currentBrick.clone();
            int tempRot= brickRotator.getCurrentIndex();

            currentBrick=heldBrick.clone();
            heldBrick=tempBrick;

            brickRotator.setBrick(currentBrick);
            brickRotator.setCurrentShape(heldRotation);

            heldRotation= tempRot;
        }

        currentOffset= new Point(4,10);

        canHold= false;
        return getViewData();
    }


}
