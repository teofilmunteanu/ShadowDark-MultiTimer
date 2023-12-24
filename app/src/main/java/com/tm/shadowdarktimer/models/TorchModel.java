package com.tm.shadowdarktimer.models;

import java.sql.Time;
import java.time.LocalTime;

public class TorchModel {
    LocalTime torchTime;
    private boolean paused;

    public TorchModel(int hours, int minutes, int seconds){
        this.torchTime = LocalTime.of(hours,minutes,seconds);
        this.paused = true;
    }

    public LocalTime getTime(){
        return torchTime;
    }

    public boolean isPaused(){
        return paused;
    }

    public void pauseUnpause(){
        paused = !paused;
    }
}
