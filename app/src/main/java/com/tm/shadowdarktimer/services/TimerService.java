package com.tm.shadowdarktimer.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tm.shadowdarktimer.MainActivity;
import com.tm.shadowdarktimer.models.TorchModel;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Timer;

public class TimerService extends Service {
    private static TimerService instance;

    private ArrayList<TorchTimer> torchTimers = new ArrayList<>();
    private int totalTimerMiliseconds;

    public static TimerService getInstance() {
        return instance;
    }

    public int localTimeToMilis(LocalTime time){
        return time.getHour() * 3600000 + time.getMinute()*60000 + time.getSecond()*1000;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public TorchTimer createTimer(TorchModel torchModel, LocalTime time) {
        long totalMillis = localTimeToMilis(time);

        TorchTimer torchTimer =  new TorchTimer(torchModel, totalMillis, 1000);
        torchTimers.add(torchTimer);

        torchTimer.start();

        return torchTimer;
    }

    public void stopTimer(CountDownTimer timer) {
        //CountDownTimer timer = null; // get timer with that id from the timers list (if it gets the id as parameter)
        if (timer != null) {
            timer.cancel();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
