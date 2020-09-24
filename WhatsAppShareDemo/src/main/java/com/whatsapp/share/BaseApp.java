package com.whatsapp.share;

import android.app.Application;

import com.umeng.commonsdk.UMConfigure;

public class BaseApp extends Application {
    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        UMConfigure.init(this, "5e2e5c140cafb29fe800007a", "guanfang", UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    public static BaseApp getInstance() {
        return app;
    }
}
