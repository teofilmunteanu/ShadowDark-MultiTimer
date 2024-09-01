package com.tm.shadowdarktimer.models;

import android.os.CountDownTimer;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.tm.shadowdarktimer.BR;
import com.tm.shadowdarktimer.adapters.TorchObserver;
import com.tm.shadowdarktimer.services.TimerManager;
import com.tm.shadowdarktimer.services.TimerUpdateListener;
import com.tm.shadowdarktimer.services.TorchTimer;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class TorchModel extends BaseObservable implements Serializable {
    private LocalTime torchTime;
    private LocalTime timeChange;
    private final TorchTimer timer;

    private static final DateTimeFormatter torchTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter timeChangeFormatter = DateTimeFormatter.ofPattern("mm:ss");


    private static final LocalTime minTime = LocalTime.of(0,0,0);
    private static final LocalTime maxTime = LocalTime.of(23,59,59);

    private List<TorchObserver> observers = new ArrayList<>();


    public TorchModel(){
        this.torchTime = LocalTime.of(1,0,0);
        timeChange = LocalTime.of(0,10,0);
        timer = TimerManager.getInstance().createTimer(this, torchTime);
    }

    @Bindable
    public boolean getPaused(){
        return timer.isPaused();
    }


    public void addPauseObserver(TorchObserver observer) {
        observers.add(observer);
    }

    public void removePauseObserver(TorchObserver observer) {
        observers.remove(observer);
    }

    private void notifyPauseObservers(){
        for (TorchObserver observer : observers){
            observer.onPausedChanged(getPaused());
        }
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
    public void resetTimeChange(){
        timeChange = LocalTime.of(0,10,0);
    }
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
            LocalTime parseHHmm = LocalTime.parse("00:"+timeString, torchTimeFormatter);
            return LocalTime.of(0,parseHHmm.getMinute(),parseHHmm.getSecond());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean isValidTimeChangeString(String timeString){
        return parseTimeChangeString(timeString) != null;
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

        notifyPauseObservers();
    }

    public void fastForward(LocalTime timeChange){
        int torchTimeSeconds = torchTime.toSecondOfDay();
        int changeTimeSeconds = timeChange.toSecondOfDay();

        if (torchTimeSeconds - changeTimeSeconds < 0){
            setTorchTime(minTime);
        }
        else{
            torchTime = torchTime.minusMinutes(timeChange.getMinute());
            torchTime = torchTime.minusSeconds(timeChange.getSecond());
        }

        timer.updateMillisRemaining(torchTime);

        notifyPropertyChanged(BR.timeString);
    }

    public void fastForward(String timeChangeString){
        LocalTime timeChange = parseTimeChangeString(timeChangeString);
        if (timeChange!=null){
            fastForward(timeChange);
        }
    }

    public void fastForward(){
        fastForward(timeChange);
    }

    public void fastBackward(LocalTime timeChange){
        int torchTimeSeconds = torchTime.toSecondOfDay();
        int changeTimeSeconds = timeChange.toSecondOfDay();

        if (torchTimeSeconds + changeTimeSeconds > maxTime.toSecondOfDay()){
            setTorchTime(maxTime);
        }
        else{
            torchTime = torchTime.plusMinutes(timeChange.getMinute());
            torchTime = torchTime.plusSeconds(timeChange.getSecond());
        }

        timer.updateMillisRemaining(torchTime);

        notifyPropertyChanged(BR.timeString);
    }

    public void fastBackward(String timeChangeString){
        LocalTime timeChange = parseTimeChangeString(timeChangeString);
        if (timeChange!=null){
            fastBackward(timeChange);
        }
    }

    public void fastBackward(){
        fastBackward(timeChange);
    }

}
