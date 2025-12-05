package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;


import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the GUI controller for Zen mode of Tetris.
 * It manages user input, highest score display and game-over status.
 * It also handles rendering of gameboard, ghost bricks, current and next bricks and held bricks.
 */
public class GuiControllerZen implements Initializable {

    private static final int BRICK_SIZE = 20;
    private static final int BRICK_OFFSETX = 323;
    private static final int BRICK_OFFSETY = -120;
    private static final int GHOST_OFFSETY = -58;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @FXML
    private GridPane gamePanel;
    @FXML
    private Group groupNotification;
    @FXML
    private GridPane brickPanel;
    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private void handleRestart(){
        newGame();
    }

    @FXML
    private void handleExit(){
        javafx.application.Platform.exit();
    }

    @FXML
    private Label scoreLabel;
    @FXML
    private Label highestScoreLabel;
    @FXML
    private StackPane holdPane;
    @FXML
    private StackPane nextBrickPane;
    @FXML
    private Button pauseButton;
    @FXML
    private Pane pauseOverlay;
    @FXML
    private Pane ghostPane;

    @FXML
    private void returnHome(){
        SceneLoader.load("/homeLayout.fxml");
    }

    /**
     * Initializes controller, load fonts and set up all key bindings.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(keyEvent ->{

            if (isPause.getValue() || isGameOver.getValue()) return;

            switch(keyEvent.getCode()) {
                case LEFT->
                    refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventSource.USER)),true);

                case RIGHT->
                    refreshBrick(eventListener.onRightEvent(new MoveEvent(EventSource.USER)),true);

                case UP ->
                    refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventSource.USER)),true);

                case DOWN->
                    moveDown(new MoveEvent(EventSource.USER));

                case SPACE-> performQuickDrop();

                case C-> performHold();

                case N-> newGame();

                case P -> pauseGame();

            }
            keyEvent.consume();
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    /**
     * Initialises game board view and current brick panel.
     * @param boardMatrix current game board state
     * @param brick current brick and its position
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(BRICK_OFFSETX +gamePanel.getLayoutX() + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
        brickPanel.setLayoutY(BRICK_OFFSETY + gamePanel.getLayoutY() + brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE));


        timeLine = new Timeline(new KeyFrame(
                Duration.millis(600),
                ae -> moveDown(new MoveEvent(EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    private Paint getFillColor(int i) {

        return switch (i) {
            case 0 ->Color.TRANSPARENT;
            case 1 ->Color.AQUA;
            case 2 ->Color.BLUEVIOLET;
            case 3 ->Color.DARKGREEN;
            case 4 ->Color.YELLOW;
            case 5->Color.RED;
            case 6-> Color.BEIGE;
            case 7->Color.BURLYWOOD;
            default -> Color.WHITE;
        };
    }


    private void drawGhostBrick(int[][] shape, int x, int y){
        ghostPane.getChildren().clear();

        double hGap= brickPanel.getHgap();
        double vGap= brickPanel.getVgap();

        for (int i=0; i< shape.length; i++){
            for(int j=0;j<shape[i].length; j++){
                if (shape[i][j] !=0){
                    Rectangle r = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    r.setFill(Color.GRAY);
                    r.setOpacity(0.3);
                    r.setTranslateX(gamePanel.getLayoutX() +(x+j)*(BRICK_SIZE + hGap));
                    r.setTranslateY(GHOST_OFFSETY+gamePanel.getLayoutY()+(y+i)*(BRICK_SIZE + vGap));
                    ghostPane.getChildren().add(r);}
                }
            }
    }

    private void updateGhostBrick(ViewData brick){
        Point ghostPos= eventListener.getGhostBrickPosition();
        if (ghostPos != null){
            drawGhostBrick(brick.getBrickData(), brick.getxPosition(),brick.getGhostY());
        }
    }

    private void refreshBrick(ViewData brick, boolean updateNext) {

        if (isPause.getValue() == Boolean.FALSE) {
            updateGhostBrick(brick);

            brickPanel.setLayoutX(BRICK_OFFSETX + brick.getxPosition() * (brickPanel.getVgap() + BRICK_SIZE));
            brickPanel.setLayoutY(BRICK_OFFSETY + gamePanel.getLayoutY()+ brick.getyPosition() * (brickPanel.getHgap() + BRICK_SIZE));
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

        }
        if (updateNext) {
            updateNextBrick(brick.getNextBrickData());}
    }

    /**
     * Updates game board background to current state of board matrix
     * @param board the 2D matrix representing current game board state
     */
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }
    private void clearedRowsNotif(ClearRow clearRow){
        if (clearRow!= null && clearRow.getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + clearRow.getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());

        }
    }
    private void moveDown(MoveEvent event) {
        if (isPause.getValue()) return;

        DownData downData = eventListener.onDownEvent(event);
        clearedRowsNotif(downData.getClearRow());
        refreshBrick(downData.getViewData(),false);
        gamePanel.requestFocus();
    }

    /**
     * Sets event listener that handles game ations
     * @param eventListener the instance to set
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * bind score to an IntergerProperty
     * @param integerProperty represents current score
     */
    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score:%d"));
    }

    /**
     * Updates the displayed highest score/
     * @param hs the highest score to display
     */
    public void updateHighestScore(int hs){
        if (highestScoreLabel !=null){
            highestScoreLabel.setText("Highest Score:"+ hs);
        }
    }
    private void drawRectanglesToPane(int[][] shape, Pane pane){
        if (shape ==null) return;
        pane.getChildren().clear();
        for (int y=0; y< shape.length; y++){
            for(int x= 0; x<shape[y].length; x++){
                if (shape[y][x] !=0){
                    Rectangle rect = new Rectangle(BRICK_SIZE,BRICK_SIZE);
                    rect.setFill(getFillColor(shape[y][x]));
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(1);

                    rect.setTranslateX(x* BRICK_SIZE);
                    rect.setTranslateY(y*BRICK_SIZE);

                    pane.getChildren().add(rect);
                }
            }
        }
    }
    /**
     * Updates hold brick panel with currently held brick shape
     * @param shape the 2D matrix representing the held brick shape.
     */
    public void updateHeldBrick(int[][] shape){
        drawRectanglesToPane(shape,holdPane);
    }
    /**
     * Updates next brick panel with upcoming brick shape
     * @param shape the 2D matrix representing the held brick shape.
     */
    public void updateNextBrick(int[][] shape){
        drawRectanglesToPane(shape,nextBrickPane);
    }

    private void performHold(){
        if(!isPause.get() &&isGameOver.getValue()==Boolean.FALSE){

            ViewData newBrick = eventListener.onHoldEvent(new MoveEvent(EventSource.USER));
            if (newBrick!= null) {

                refreshBrick(newBrick, false);
                int[][] heldShape = eventListener.getHeldBrickShape();

                updateHeldBrick(heldShape);
            }
        }
    }

    private void performQuickDrop(){
        if (isPause.getValue()== Boolean.FALSE && isGameOver.getValue()== Boolean.FALSE){
            QuickDropData quickDrop= eventListener.onQuickDropEvent(new MoveEvent(EventSource.USER));

            refreshBrick(quickDrop.getViewData(),true);

            clearedRowsNotif(quickDrop.getClearRow());

            gamePanel.requestFocus();
        }
    }
    /**
     * display Game Over panel.
     */
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    /**
     * Starts a new game.
     * Resets UI elements such as held brick and next brick.
     */
    public void newGame() {
        timeLine.stop();
        gameOverPanel.setVisible(false);

        holdPane.getChildren().clear();
        nextBrickPane.getChildren().clear();
        pauseOverlay.setVisible(false);
        pauseOverlay.setOpacity(0);

        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    /**
     * Pause or resume game based on current pause state
     */
    public void pauseGame() {

        if (isPause.get()){
            resumeGame();
        } else{
           pauseGameUI();
        }
        gamePanel.requestFocus();
    }

    private void resumeGame(){
        pauseOverlay.setOpacity(0);
        pauseOverlay.setVisible(false);
        timeLine.play();
        isPause.set(false);
        pauseButton.setText("Pause");
    }

    private void pauseGameUI(){
        pauseOverlay.setOpacity(0.5);
        pauseOverlay.setVisible(true);
        pauseOverlay.toFront();

        timeLine.pause();
        isPause.set(true);
        pauseButton.setText("Resume");
    }
}
