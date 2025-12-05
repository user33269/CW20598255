package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class TimeManager {

    private int timeLeft;
    private Timeline timer;

    public interface TimeListener{
        void updateTime( int timeLeft);
        void endTime();
    }

    public TimeManager(int startingTime, TimeListener listener){
        this.timeLeft= startingTime;

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
