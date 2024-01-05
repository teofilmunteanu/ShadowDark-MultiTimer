package com.tm.shadowdarktimer.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tm.shadowdarktimer.models.TorchModel;

import java.time.LocalTime;
import java.util.ArrayList;

public class TimerManager extends Service {
    private static TimerManager instance;

    private ArrayList<TorchTimer> torchTimers = new ArrayList<>();

    public static TimerManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public TorchTimer createTimer(TorchModel torchModel, LocalTime time) {
        TorchTimer torchTimer =  new TorchTimer(torchModel, time);
        torchTimers.add(torchTimer);

        return torchTimer;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //global operations on timers
}
