package com.kmt.pro.widget.countdownview;

/**
 * Created by silence on 16-4-4.
 */
public interface CountdownListener {

    void onStartTick();

    void onStopTick();

    void onTick(CountdownTextView CountdownTextView, long startTime, long stopTime);
}
