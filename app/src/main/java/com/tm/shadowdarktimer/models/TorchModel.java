package com.tm.shadowdarktimer.models;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public void resetTime(){
        torchTime = LocalTime.of(0,0,0);
    }

    public String getTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return torchTime.format(formatter);
    }

    public boolean isPaused(){
        return paused;
    }

    public void pauseUnpause(){
        paused = !paused;
    }
}
