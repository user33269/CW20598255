package com.comp2042;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeManagerTest {

    static  class MockTimeListener implements  TimeManager.TimeListener{
        int lastTime=-1;
        boolean ended= false;

        @Override
        public void updateTime(int timeLeft){
            lastTime= timeLeft;
        }

        @Override
        public void endTime(){
            ended= true;
        }
    }

    @Test
    void start() {
        MockTimeListener listener= new MockTimeListener();
        TimeManager timemanager= new TimeManager(3, listener);

        timemanager.tick();
        assertEquals(2, listener.lastTime);

    }


    @Test
    void pause() {
        MockTimeListener listener= new MockTimeListener();
        TimeManager timemanager= new TimeManager(3, listener);

        timemanager.start();
        timemanager.pause();

        //test if timer is stopped
        assertFalse(timemanager.isRunning());

    }

    @Test
    void resume() {
        MockTimeListener listener= new MockTimeListener();
        TimeManager timemanager= new TimeManager(3, listener);

        timemanager.tick();
        int pausedTime= timemanager.getTimeLeft();

        timemanager.tick();
        //test if timer resume
        assertEquals(pausedTime-1, timemanager.getTimeLeft());
    }

    @Test
    void stop() {
        MockTimeListener listener= new MockTimeListener();
        TimeManager timemanager= new TimeManager(3, listener);

        timemanager.start();
        timemanager.stop();

        //timer should stop running
        assertFalse(timemanager.isRunning());
    }

    @Test
    void restart() {
        MockTimeListener listener= new MockTimeListener();
        TimeManager timemanager= new TimeManager(3, listener);

        timemanager.tick();
        timemanager.restart(3);

        assertEquals(3,timemanager.getTimeLeft());


    }
}