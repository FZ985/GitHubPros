package com.daynightjava1;

import android.app.Application;

import nightmode.DayNightMode;

public class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DayNightMode.setDefaultNightMode(this, DayNightMode.getDefaultNightMode(this));
    }
}
