package com.comp2042;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * This class manages a countdown timer for Tetris game Time Blitz mode.
 * It provides features to start, resume, pause, stop and restart timer.
 */
public class TimeManager {

    private int timeLeft;
    private Timeline timer;
    private TimeListener listener;

    /**
     * Listener interface to receive time updates and endTime event.
     */
    public interface TimeListener{
        /**
         * Called every second to update remaining time
         * @param timeLeft the current remaining time in seconds.
         */
        void updateTime( int timeLeft);

        /**
         * called when timer reaches zero.
         */
        void endTime();
    }

    /**
     * constructs a new TimeManager with specified starting time and listener for Tetris game.
     * @param startingTime the starting time in seconds
     * @param listener the listener to receive updates and endTime event.
     */
    public TimeManager(int startingTime, TimeListener listener){
        this.timeLeft= startingTime;
        this.listener= listener;

        timer= new Timeline(new KeyFrame(
                Duration.seconds(1),
                e->{
                    timeLeft--;
                    listener.updateTime(timeLeft);

                    if(timeLeft<=0){
                        timer.stop();
                        listener.endTime();
                    }}

        ));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Decrements the timer by one second and updates listener.
     * This is used solely for testing purposes.
     */
    //for testing
    public void tick(){
        if(timeLeft>0){
            timeLeft--;
            listener.updateTime(timeLeft);

            if(timeLeft==0){
                listener.endTime();
            }
        }
    }

    /**
     * Check if timer is currently running. Used for testing purposes.
     * @return true if timer is running, false otherwise.
     */
    public boolean isRunning(){
        return timer != null&& timer.getStatus() == Timeline.Status.RUNNING;
    }

    /**
     * Returns current remaining time.
     * Used for testing purposes.
     * @return remaining time in seconds.
     */
    public int getTimeLeft(){
        return timeLeft;
    }

    //game time controls

    /**
     * Starts timer
     */
    public void start(){
        timer.play();
    }
    /**
     * Pause timer
     */
    public void pause(){
        timer.pause();
    }
    /**
     * Resume timer
     */
    public void resume(){
        timer.play();
    }
    /**
     * Stops timer
     */
    public void stop(){
        timer.stop();
    }
    /**
     * Restarts timer with new starting time in seconds.
     */
    public void restart(int startingTime){
        timer.stop();
        this.timeLeft= startingTime;
        timer.play();
    }


}
