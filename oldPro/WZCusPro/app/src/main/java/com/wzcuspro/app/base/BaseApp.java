package com.wzcuspro.app.base;


import android.support.multidex.MultiDex;


public class BaseApp extends android.support.multidex.MultiDexApplication {
    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        app = this;

//        BackgroundAppManager backgroundAppManager = BackgroundAppManager.init(this);
//        backgroundAppManager.addListener(new BackgroundAppManager.Listener() {
//            @Override
//            public void onBecameForeground(Activity activity) {
////                前台
//                Logger.e("切换到前台");
//                }
//
//            @Override
//            public void onBecameBackground(Activity activity)
////              后台
//                Logger.e("切换到后台");
//            }
//        });
//        registerActivityLifecycleCallbacks(backgroundAppManager);
    }

    public static BaseApp getInstance() {
        return app;
    }

}
