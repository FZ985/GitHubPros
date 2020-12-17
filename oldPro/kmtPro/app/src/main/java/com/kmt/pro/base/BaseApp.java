package com.kmt.pro.base;

import com.kmtlibs.app.utils.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.smtt.sdk.QbSdk;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        app = this;
        init();
    }

    private void init() {
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                Logger.e("x5内核加载:" + b);
            }
        });
    }

    public static BaseApp getInstance() {
        return app;
    }
}
