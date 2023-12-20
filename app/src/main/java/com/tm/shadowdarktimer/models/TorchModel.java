package com.tm.shadowdarktimer.models;

public class TorchModel {
    private String timeString;
    private boolean paused;

   /* public final String pauseSymbol = " ";
    public final String playSymbol = ;*/


    public TorchModel(String timeString){
        this.timeString = timeString;
        this.paused = true;
    }

    public String getTime(){
        return timeString;
    }

    public boolean isPaused(){
        return paused;
    }

    public void pauseUnpause(){
        paused = !paused;
    }
}
