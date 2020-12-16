package com.app.utils;

import android.app.Application;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-16 17:15
 */
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