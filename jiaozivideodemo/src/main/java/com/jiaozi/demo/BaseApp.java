package com.jiaozi.demo;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

public class BaseApp extends Application {
    private static BaseApp app;
    private HttpProxyCacheServer proxy;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static BaseApp getInstance() {
        return app;
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        BaseApp app = (BaseApp) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}
