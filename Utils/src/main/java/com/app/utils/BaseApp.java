package com.app.utils;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

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
        boolean b = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        Toast.makeText(app, "bbb:" + b, Toast.LENGTH_SHORT).show();
    }

    public static BaseApp getInstance() {
        return app;
    }
}