package com.okhttpapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.Http;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.config.OkHttpConfig;

/**
 * Create by JFZ
 * date: 2020-09-15 18:29
 **/
public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpConfig.getInstance()
                .setRequestInterceptor(builder -> {
                    if (!isConnection(BaseApp.this)) {
                        OkHttpFactory.getInstance().obtainHandler().post(() -> Toast.makeText(BaseApp.this, "拦截请求,未联网", Toast.LENGTH_SHORT).show());
                        return false;
                    } else return true;
                }).setResponseInterceptor(new Http.ResponseInterceptor() {
            @Override
            public boolean interceptorResponse(Http.GETBuilder builder, Http.UICall uiCall, RequestCallback<?> callback, int code, String result) {
                if (!isConnection(BaseApp.this)) {
                    OkHttpFactory.getInstance().obtainHandler().post(() -> Toast.makeText(BaseApp.this, "拦截成功响应，不往下执行", Toast.LENGTH_SHORT).show());
                    return false;
                } else return true;
            }

            @Override
            public boolean interceptorResponseErr(Http.GETBuilder builder, Http.UICall uiCall, RequestCallback<?> callback, Exception e) {
                if (!isConnection(BaseApp.this)) {
                    OkHttpFactory.getInstance().obtainHandler().post(() -> Toast.makeText(BaseApp.this, "拦截失败响应,不往下执行", Toast.LENGTH_SHORT).show());
                    return false;
                } else return true;
            }
        });
    }

    public static boolean isConnection(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable;
        if (networkInfo != null) {
            isAvailable = networkInfo.isAvailable();
        } else {
            isAvailable = false;
        }
        Log.i("ConnectionVerdict", isAvailable + "");
        return isAvailable;
    }

    /**
     * 获取联网的Manager
     *
     * @param context
     * @return
     */
    private static ConnectivityManager getConnectivityManager(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return null;
        }
        return mConnectivityManager;
    }
}
