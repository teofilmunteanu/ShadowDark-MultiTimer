package com.tm.shadowdarktimer.services;

import android.os.CountDownTimer;
import android.util.Log;

import com.tm.shadowdarktimer.models.TorchModel;

import java.time.LocalTime;

public class TorchTimer extends CountDownTimer {
    private TorchModel torchModel;

    public TorchTimer(TorchModel torchModel, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.torchModel = torchModel;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.d("time", millisUntilFinished+"");
        torchModel.updateTorchTime(millisToLocalTime(millisUntilFinished));
    }

    @Override
    public void onFinish() {

    }

    public LocalTime millisToLocalTime(long millis){
        long hours = millis / 3600000;
        millis %= 3600000;
        long minutes = millis / 60000;
        millis %= 60000;
        long seconds = millis / 1000;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }
}
