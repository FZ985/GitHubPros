package com.kmtlibs.banner.util;

import android.util.Log;

import com.kmtlibs.BuildConfig;


public class LogUtils {
    public static final String TAG = "banner_log";

    private static final boolean DEBUG = BuildConfig.DEBUG;

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }

    public static void i(String msg) {
        if (DEBUG) {
            Log.i(TAG, msg);
        }
    }
}
