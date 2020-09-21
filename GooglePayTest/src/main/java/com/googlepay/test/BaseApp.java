package com.googlepay.test;

import android.app.Application;

/**
 * Create by JFZ
 * date: 2020-09-18 14:00
 **/
public class BaseApp extends Application {
    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static BaseApp getInstance() {
        return app;
    }
}
