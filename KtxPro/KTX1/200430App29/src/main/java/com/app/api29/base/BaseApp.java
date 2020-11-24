package com.app.api29.base;

import androidx.multidex.MultiDex;

/**
 * Create by JFZ
 * date: 2020-04-30 18:12
 **/
public class BaseApp extends androidx.multidex.MultiDexApplication {
    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        app = this;
    }

    public static BaseApp getInstance() {
        return app;
    }
}
