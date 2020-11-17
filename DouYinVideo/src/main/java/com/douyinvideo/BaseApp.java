package com.douyinvideo;

import android.app.Application;
import android.content.Context;

import videocache.HttpProxyCacheServer;

public class BaseApp extends Application {
    private static BaseApp app;
    private HttpProxyCacheServer proxy;

    public static BaseApp getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        BaseApp app = (BaseApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
