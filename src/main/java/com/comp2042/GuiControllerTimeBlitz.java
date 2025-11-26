package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

public class GuiControllerTimeBlitz implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @FXML
    private void handleRestart(){
        newGame();
        gameControllerTimeBlitz.restartTimer();
    }

    @FXML
    private void handleExit(){
        javafx.application.Platform.exit();
    }

    @FXML
    private Label scoreLabel;

    @FXML
    private Label timeLabel;

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
        gameControllerTimeBlitz.stopTimer();
        SceneLoader.load("/home.fxml");
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)),true);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)),true);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)),true);
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if(keyEvent.getCode()== KeyCode.SPACE){
                        performQuickDrop();
                        keyEvent.consume();
                    }
                    if(keyEvent.getCode()== KeyCode.C){
                        performHold();
                        keyEvent.consume();
                    }

                }
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame();
                }
                if(keyEvent.getCode()== KeyCode.P){
                    pauseGame();
                    keyEvent.consume();
                }
            }
        });
        gameOverPanel.setVisible(false);

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }


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
        brickPanel.setLayoutX(323+gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-120 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);


        timeLine = new Timeline(new KeyFrame(
                Duration.millis(700),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }
    
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }


    private void drawGhostBrick(int[][] shape, int x, int y){
        ghostPane.getChildren().clear();

        int blockSize= BRICK_SIZE;

        double hGap= brickPanel.getHgap();
        double vGap= brickPanel.getVgap();

        for (int i=0; i< shape.length; i++){
            for(int j=0;j<shape[i].length; j++){
                if (shape[i][j] !=0){
                    Rectangle r = new Rectangle(blockSize, blockSize);
                    r.setFill(Color.GRAY);
                    r.setOpacity(0.3);
                    r.setTranslateX(gamePanel.getLayoutX() +(x+j)*blockSize + (x+j)*hGap);
                    r.setTranslateY(-58+gamePanel.getLayoutY()+(y+i)*blockSize + (y+i)*vGap);
                    ghostPane.getChildren().add(r);}
                }
            }
    }

    private void refreshBrick(ViewData brick, boolean updateNext) {

        if (isPause.getValue() == Boolean.FALSE) {

            Point ghostPos= eventListener.getGhostBrickPosition();
            if (ghostPos != null){
                drawGhostBrick(brick.getBrickData(), brick.getxPosition(),brick.getGhostY());
            }

            brickPanel.setLayoutX(323+brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-120 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }

        }
        if (updateNext) {
            updateNextBrick(brick.getNextBrickData());}
    }

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

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);


            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());


            }
            refreshBrick(downData.getViewData(),false);
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        scoreLabel.textProperty().bind(integerProperty.asString("Score:%d"));
    }

    public void updateHighestScore(int hs){
        if (highestScoreLabel !=null){
            highestScoreLabel.setText("Highest Score:"+ hs);
        }
    }

    public void updateTime(int timeLeft){
        timeLabel.setText((timeLeft+" s"));
    }


    public void updateNextBrick(int[][] shape){
        nextBrickPane.getChildren().clear();

        if (shape ==null) return;


        int blocksize=20;

        for (int y=0; y< shape.length; y++){
            for(int x= 0; x<shape[y].length; x++){
                if (shape[y][x] !=0){
                    Rectangle rect = new Rectangle(blocksize,blocksize);
                    rect.setFill(getFillColor(shape[y][x]));
                    rect.setStroke(Color.BLACK);
                    rect.setStrokeWidth(1);

                    rect.setTranslateX(x*blocksize);
                    rect.setTranslateY(y*blocksize);

                    nextBrickPane.getChildren().add(rect);
                }
            }
        }
    }
    private void performHold(){
        if(isPause.get()== Boolean.FALSE &&isGameOver.getValue()==Boolean.FALSE){

            ViewData newBrick = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD,EventSource.USER));
            if (newBrick!= null) {

                refreshBrick(newBrick, false);
                int[][] heldShape = eventListener.getHeldBrickShape();

                updateHeldBrick(heldShape);
            }
        }
    }

    public void updateHeldBrick(int[][] shape){
        holdPane.getChildren().clear();

        if (shape ==null) return;


        int blocksize=20;

        for (int y=0; y< shape.length; y++){
            for(int x= 0; x<shape[y].length; x++){
                if (shape[y][x] !=0){
                    Rectangle rectangle = new Rectangle(blocksize,blocksize);
                    rectangle.setFill(getFillColor(shape[y][x]));
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(1);

                    rectangle.setTranslateX(x*blocksize);
                    rectangle.setTranslateY(y*blocksize);

                    holdPane.getChildren().add(rectangle);
                }
            }
        }
    }



    private void performQuickDrop(){
        if (isPause.getValue()== Boolean.FALSE && isGameOver.getValue()== Boolean.FALSE){
            QuickDropData quickDrop= eventListener.onQuickDropEvent(new MoveEvent(EventType.QUICK_DROP,EventSource.USER));

            refreshBrick(quickDrop.getViewData(),true);

            if (quickDrop.getClearRow() != null && quickDrop.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + quickDrop.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());}

            gamePanel.requestFocus();
        }
    }

    private GameControllerTimeBlitz gameControllerTimeBlitz;
    public void setGameControllerTimeBlitz(GameControllerTimeBlitz gc){
        this.gameControllerTimeBlitz=gc;
    }

    public void gameOver() {
        timeLabel.setText("Time's UP!");
        timeLine.stop();
        gameControllerTimeBlitz.stopTimer();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }


    public void newGame() {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    public void pauseGame() {


        if (isPause.get()){
            pauseOverlay.setOpacity(0);
            pauseOverlay.setVisible(false);
            timeLine.play();
            gameControllerTimeBlitz.resumeTimer();
            isPause.set(false);
            pauseButton.setText("Pause");


        } else{
            pauseOverlay.setOpacity(0.5);
            pauseOverlay.setVisible(true);
            pauseOverlay.toFront();

            timeLine.pause();
            gameControllerTimeBlitz.pauseTimer();
            isPause.set(true);
            pauseButton.setText("Resume");


        }
        gamePanel.requestFocus();
    }
}
