package com.comp2042;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimeManager {

    private int timeLeft;
    private Timeline timer;
    private TimeListener listener;


    public interface TimeListener{
        void updateTime( int timeLeft);
        void endTime();
    }


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
    public boolean isRunning(){
        return timer != null&& timer.getStatus() == Timeline.Status.RUNNING;
    }
    public int getTimeLeft(){
        return timeLeft;
    }

    //game time controls
    public void start(){
        timer.play();
    }
    public void pause(){
        timer.pause();
    }
    public void resume(){
        timer.play();
    }
    public void stop(){
        timer.stop();
    }
    public void restart(int startingTime){
        timer.stop();
        this.timeLeft= startingTime;
        timer.play();
    }


}
