package com.lxbuytimes.kmtapp.retrofit.tool;

import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpLoggerInterceptor implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        System.out.println("OkHttpLoggerInterceptor:" + message);
    }
}
