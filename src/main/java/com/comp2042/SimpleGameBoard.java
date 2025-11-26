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
    private boolean checkforCollision(Point currentOffset, int[][] shape){
        return  MatrixOperations.intersect(currentGameMatrix,brickRotator.getCurrentShape(),(int)currentOffset.getX(),
                (int)currentOffset.getY());
    }
    private boolean moveIfNoCollision(int dx, int dy){
        Point newOffset= new Point(currentOffset);
        newOffset.translate(dx,dy);

        if(checkforCollision(newOffset,null)){
            return false;
        }
        currentOffset= newOffset;
        return true;
    }

    public Point getGhostBrickPosition(){
       Point ghost= new Point(currentOffset);
       while(!checkforCollision(new Point(ghost.x,ghost.y+1),null)){
           ghost.translate(0,1);
       }
       return ghost;
    }


    @Override
    public int[][] getHeldBrickShape(){
        if (heldBrick == null) return null;

        return heldBrick.getRotation(heldRotation);
    }

    @Override
    public boolean moveBrickDown() {
        return moveIfNoCollision(0,1);
    }

    @Override
    public boolean moveBrickLeft() {
        return moveIfNoCollision(-1,0);
    }

    @Override
    public boolean moveBrickRight() {
        return moveIfNoCollision(1,0);
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
    public boolean rotateLeftBrick() {

        NextShapeInfo nextShape = brickRotator.getNextShape();

        if (checkforCollision(currentOffset,nextShape.getShape())) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }
    private final int GAME_OVER_ROW=3;
    @Override
    public boolean createNewBrick() {
        this.currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 3);

        canHold=true;
        boolean collision= checkforCollision(currentOffset,null);
        if(collision && currentOffset.getY()<=GAME_OVER_ROW){
            return true;
        }
        return false;
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    private int calcGhostY(){
        int ghostY= (int) currentOffset.getY();
        Point ghostPoint=new Point(currentOffset);

        while(!checkforCollision(ghostPoint,null)
        ){
            ghostY++;
            ghostPoint.y=ghostY;
        }

        return ghostY;
    }

    @Override
    public ViewData getViewData() {
        int ghostY= calcGhostY();

        return new ViewData(
                brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().getFirst(),ghostY);
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

            heldBrick= currentBrick;
            heldRotation= brickRotator.getCurrentIndex();
            createNewBrick();
        }else{
            Brick tempBrick= currentBrick;
            currentBrick=heldBrick;
            heldBrick=tempBrick;

            int tempRotation=heldRotation;
            heldRotation=brickRotator.getCurrentIndex();
            brickRotator.setBrick(currentBrick);
            brickRotator.setCurrentShape(tempRotation);
        }

        currentOffset= new Point(4,10);

        canHold= false;
        return getViewData();
    }


}
