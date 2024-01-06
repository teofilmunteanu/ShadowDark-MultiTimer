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
    private LocalTime timeChange;
    private final TorchTimer timer;

    private static final DateTimeFormatter torchTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter timeChangeFormatter = DateTimeFormatter.ofPattern("mm:ss");


   /* private final LocalTime minTime = LocalTime.of(0,0,0);
    private final LocalTime maxTime = LocalTime.of(99,59,59);*/

    public TorchModel(int hours, int minutes, int seconds){
        this.torchTime = LocalTime.of(hours,minutes,seconds);
        timeChange = LocalTime.of(0,10,0);
        timer = TimerManager.getInstance().createTimer(this, torchTime);
    }

    @Bindable
    public boolean getPaused(){
        return timer.isPaused();
    }


    //torch time
    public void resetTime(){
        torchTime = LocalTime.of(0,0,0);
    }

    @Bindable
    public String getTimeString(){
        return torchTime.format(torchTimeFormatter);
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
            return LocalTime.parse(timeString, torchTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean isValidTimeString(String timeString){
        return parseTimeString(timeString) != null;
    }


    //time change
    @Bindable
    public String getTimeChangeString(){

        return timeChange.format(timeChangeFormatter);
    }

    public void setTimeChangeString(String changeString){
        timeChange = parseTimeChangeString(changeString);

        //only update the bound value if it's valid
        if (timeChange != null){
            notifyPropertyChanged(BR.timeChangeString);
        }
    }

    public static LocalTime parseTimeChangeString(String timeString){
        try {
            return LocalTime.parse(timeString, timeChangeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
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

    /*public void fastForward(){
        int torchTimeSeconds = torchTime.toSecondOfDay();
        int changeTimeSeconds = changeTime.toSecondOfDay();
        if (torchTimeSeconds - changeTimeSeconds < 0){
            torchTime = minTime;
        }
        else{
            torchTime.minusMinutes(changeTime.getMinute());
            torchTime.minusSeconds(changeTime.getSecond());
        }

        timer.updateMillisRemaining(torchTime);

        notifyPropertyChanged(BR.timeString);
    }

    public void fastBackward(){
        int torchTimeSeconds = torchTime.toSecondOfDay();
        int changeTimeSeconds = changeTime.toSecondOfDay();

        if (torchTimeSeconds + changeTimeSeconds > maxTime.toSecondOfDay()){
            torchTime = maxTime;
        }
        else{
            torchTime.plusMinutes(changeTime.getMinute());
            torchTime.plusSeconds(changeTime.getSecond());
        }

        timer.updateMillisRemaining(torchTime);

        notifyPropertyChanged(BR.timeString);
    }*/
}
