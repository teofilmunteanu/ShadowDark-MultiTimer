package com.tm.shadowdarktimer.models;

import android.os.CountDownTimer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.tm.shadowdarktimer.BR;
import com.tm.shadowdarktimer.services.TimerManager;
import com.tm.shadowdarktimer.services.TimerUpdateListener;
import com.tm.shadowdarktimer.services.TorchTimer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TorchModel extends BaseObservable{
    private LocalTime torchTime;
    private final TorchTimer timer;

    public TorchModel(int hours, int minutes, int seconds){
        this.torchTime = LocalTime.of(hours,minutes,seconds);
        timer = TimerManager.getInstance().createTimer(this, torchTime);
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
            timer.updateMillisRemaining(torchTime);
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
        return timer.isPaused();
    }

    //TIMER OPERATIONS
    public void pauseUnpause(){
        if (timer.isPaused()){
            timer.resumeTimer();
        }
        else{
            timer.pauseTimer();
        }

        notifyPropertyChanged(BR.paused);
    }
}
