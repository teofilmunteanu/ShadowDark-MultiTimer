package com.tm.shadowdarktimer.services;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.tm.shadowdarktimer.models.TorchModel;

import java.time.LocalTime;

public class TorchTimer{
    private final TorchModel torchModel;

    private final Handler handler;
    private Runnable runnable;

    private final long intervalMillis = 1000;
    private boolean paused;
    private long millisRemaining;

    public TorchTimer(TorchModel torchModel, LocalTime startTime) {
        this.torchModel = torchModel;
        handler = new Handler();
        paused = true;
        millisRemaining = localTimeToMilis(startTime);
    }

    public int localTimeToMilis(LocalTime time){
        return time.getHour() * 3600000 + time.getMinute()*60000 + time.getSecond()*1000;
    }

    public LocalTime millisToLocalTime(long millis){
        long hours = millis / 3600000;
        millis %= 3600000;
        long minutes = millis / 60000;
        millis %= 60000;
        long seconds = millis / 1000;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    public void updateMillisRemaining(LocalTime time){
        millisRemaining = localTimeToMilis(time);
    }

    public void runTimer(){
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                if (!paused) {
                    millisRemaining -= intervalMillis;

                    torchModel.setTorchTime(millisToLocalTime(millisRemaining));

                    if (millisRemaining > 0) {
                        handler.postDelayed(this, intervalMillis);
                    }
                }
            }
        };

        handler.postDelayed(runnable, intervalMillis);
    }

    //TIMER OPERATIONS
    public boolean isPaused(){
        return paused;
    }

    public void resumeTimer() {
        paused = false;
        runTimer();
    }

    public void pauseTimer() {
        paused = true;
        handler.removeCallbacks(runnable);
    }

    public void fastForward(long time){

    }

    public void fastBackward(long time){

    }
}
