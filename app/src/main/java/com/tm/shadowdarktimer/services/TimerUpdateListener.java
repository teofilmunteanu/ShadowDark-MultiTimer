package com.tm.shadowdarktimer.services;

import java.time.LocalTime;

public interface TimerUpdateListener {
    void onTorchTimeChanged(LocalTime newTime);
}
