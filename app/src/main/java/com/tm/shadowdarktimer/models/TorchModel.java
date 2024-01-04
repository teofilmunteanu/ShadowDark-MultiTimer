package com.tm.shadowdarktimer.models;

import android.os.CountDownTimer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.tm.shadowdarktimer.BR;
import com.tm.shadowdarktimer.services.TimerManager;
import com.tm.shadowdarktimer.services.TimerUpdateListener;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TorchModel extends BaseObservable{
    private final int id;
    private LocalTime torchTime;
    private boolean paused;
    //!!!!! do I need an instance of the timer? 2 options: instance of the timer, or the id, and I search it every time in the service
    private CountDownTimer timer;
    private final TimerManager timerManager;
    private TimerUpdateListener torchTimeChangeListener;

    public TorchModel(int id, int hours, int minutes, int seconds){
        this.id = id;
        this.torchTime = LocalTime.of(hours,minutes,seconds);
        this.paused = true;
        timerManager = TimerManager.getInstance();
    }

    public void resetTime(){
        torchTime = LocalTime.of(0,0,0);
    }

    @Bindable
    public String getTimeString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return torchTime.format(formatter);
    }

    public void setTimeString(String timeString){
        torchTime = parseTimeString(timeString);

        //only update the bound value if it's valid
        if (torchTime != null){
            notifyPropertyChanged(BR.timeString);
        }
    }

    public void setTorchTime(LocalTime newTime){
        if (!torchTime.equals(newTime)){
            torchTime = newTime;
            notifyPropertyChanged(BR.timeString);
        }
    }

    public static LocalTime parseTimeString(String timeString){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return LocalTime.parse(timeString, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean isValidTimeString(String timeString){
        return parseTimeString(timeString) != null;
    }

    @Bindable
    public boolean getPaused(){
        return paused;
    }

    public void setPaused(boolean paused) {
        if (this.paused != paused) {
            this.paused = paused;
            notifyPropertyChanged(BR.paused);
        }
    }

    public void pauseUnpause(){
        setPaused(!paused);
        notifyPropertyChanged(BR.paused);
    }

    public void startTimer() {
        timer = timerManager.createTimer(this, torchTime);
    }

    public void stopTimer() {
        timerManager.stopTimer(timer);
    }
}
