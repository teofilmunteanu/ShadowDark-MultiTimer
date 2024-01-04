package com.tm.shadowdarktimer.models;

import android.os.CountDownTimer;

import com.tm.shadowdarktimer.services.TimerService;
import com.tm.shadowdarktimer.services.TimerUpdateListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TorchModel {
    private final int id;
    private LocalTime torchTime;
    private boolean paused;
    //!!!!! do I need an instance of the timer? 2 options: instance of the timer, or the id, and I search it every time in the service
    private CountDownTimer timer;
    private final TimerService timerService;
    private TimerUpdateListener torchTimeChangeListener;

    public TorchModel(int id, int hours, int minutes, int seconds){
        this.id = id;
        this.torchTime = LocalTime.of(hours,minutes,seconds);
        this.paused = true;
        timerService = TimerService.getInstance();
    }

    public void resetTime(){
        torchTime = LocalTime.of(0,0,0);
    }

    public static String getTimeString(LocalTime torchTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return torchTime.format(formatter);
    }
    public String getTimeString(){
        return getTimeString(torchTime);
    }

    public boolean isPaused(){
        return paused;
    }

    public void pauseUnpause(){
        paused = !paused;
    }

    public void startTimer() {
        timer = timerService.createTimer(this, torchTime);
    }

    public void updateTorchTime(LocalTime newTime) {
        this.torchTime = newTime;
        notifyTorchTimeChanged(newTime);
    }

    public void stopTimer() {
        timerService.stopTimer(timer);
    }

    public void setTorchTimeChangeListener(TimerUpdateListener listener) {
        this.torchTimeChangeListener = listener;
    }

    private void notifyTorchTimeChanged(LocalTime newTime) {
        if (torchTimeChangeListener != null) {
            torchTimeChangeListener.onTorchTimeChanged(newTime);
        }
    }
}
