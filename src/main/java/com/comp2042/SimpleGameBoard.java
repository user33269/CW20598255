package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 *This class is implemented from GameBoard interface to create a TetrisGame
 * It manages simple brick movements, collision checking, board state, new bricks creation and line clearing
 *
 */
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

    /**
     *This method constructs a SimpleGameBoard with specific measurements
     * @param width this is the number of columns
     * @param height this is the number of rows
     */
    public SimpleGameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     *Returns current position of active brick. A new point is given to prevent external modification.
     * @return current brick offset on the board
     */
    public Point getCurrentOffset(){
        return new Point(currentOffset);
    }

    /**
     * Returns height of game board
     * @return board height in rows
     */
    public int getBoardHeight(){
        return currentGameMatrix[0].length;
    }

    /**
     * Retrieves rotation index of current brick from BrickRotator
     * @return current rotation index of brick
     */
    public int getRotationIndex(){
        return brickRotator.getCurrentIndex();
    }

    /**
     * checks if there's collision of brick shape at particular offset with the board
     * @param currentOffset is the position to test
     * @param shape is the shape of matrix to test
     * @return true if collision happens, false if doesn't
     */
    public boolean checkforCollision(Point currentOffset, int[][] shape){
        return  MatrixOperations.intersect(currentGameMatrix,brickRotator.getCurrentShape(),(int)currentOffset.getX(),
                (int)currentOffset.getY());
    }

    /**
     * Moves brick by (dx, dy) if theres no collision detected
     * @param dx specifies the horizontal movement
     * @param dy specifies vertical movement
     * @return true if theres movement, false if otherwise
     */
    public boolean moveIfNoCollision(int dx, int dy){
        Point newOffset= new Point(currentOffset);
        newOffset.translate(dx,dy);

        if(checkforCollision(newOffset,null)){
            return false;
        }
        currentOffset= newOffset;
        return true;
    }

    /**
     * calculate landing position of ghost brick piece
     * @return the lowest non-colliding ghost brick position
     */
    public Point getGhostBrickPosition(){
       Point ghost= new Point(currentOffset);
       while(!checkforCollision(new Point(ghost.x,ghost.y+1),null)){
           ghost.translate(0,1);
       }
       return ghost;
    }

    /**
     * Returns shape matrix of held brick.
     * @return shape of heldBrick, return null if no brick is held
     */
    @Override
    public int[][] getHeldBrickShape(){
        if (heldBrick == null) return null;

        return heldBrick.getRotation(heldRotation);
    }

    /**
     * Attempts to move brick one row downwards.
     * @return true if brick is moved down
     */
    @Override
    public boolean moveBrickDown() {
        return moveIfNoCollision(0,1);
    }

    /**
     *Attempts to move brick one column to the left.
     * @return if brick is moved left
     */
    @Override
    public boolean moveBrickLeft() {
        return moveIfNoCollision(-1,0);
    }

    /**
     *Attempts to move brick one column to the right.
     * @return if brick is moved right
     */
    @Override
    public boolean moveBrickRight() {
        return moveIfNoCollision(1,0);
    }

    /**
     * move brick down
     * If brick cannot move, it merges into the board.
     * Completed rows will be cleared and scoreBonus is added.
     * New brick is created if gameOver status is false.
     * @return DownData that includes rows cleared, view data and gameover status
     */
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

    /**
     * Drops current brick downwards immediately.
     * Completed rows will be cleared and scoreBonus is added.
     * New brick is created if gameOver status is false.
     * @return QuickDropData that includes rows cleared, view data, gameover status and drop distance
     */

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

        return  new QuickDropData(clearRow,getViewData(),gameOver,dropDistance);
    }

    /**
     * Rotates current brick to the left if no collision detected
     * @return true if rotation happens, false if it collides
     */
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

    /**
     * Creates new brick
     * Game ends if new brick generated collides with preset gameOver region
     * @return true if gameover occur
     */
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

    /**
     *Returns current state of game board as a matrix.
     * @return current gameBoard matrix containing all placed matrix.
     */
    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * To compute the y-coordinate of ghost brick piece
     * @return y-coordinate of ghost piece
     */
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

    /**
     * Produces ViewData of current game state
     * @return a ViewData consisting current shape, coordinates, next brick and ghostY
     */
    @Override
    public ViewData getViewData() {
        int ghostY= calcGhostY();

        return new ViewData(
                brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().getFirst(),ghostY);
    }

    /**
     * merges brick to gameBoard's background matrix
     */
    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Check and clear any filled rows from board
     *
     * @return ClearRow containing removed lines and updated matrix
     */
    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    /**
     * Return player's current score.
     * Score object tracks points earned from line clears and other scoreBonus.
     * @return player score
     */
    @Override
    public Score getScore() {
        return score;
    }

    /**
     * resets board and score, creates new brick to start game
     */
    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    /**
     * Handles hold brick mechanic
     * At first hold, brick would be stored. Otherwise, held brick would be swapped with current brick
     * @return updated ViewData afer holding
     */
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
