package com.wzcuspro.app.utils;

import android.text.TextUtils;
import android.util.Log;


/**
 * Created by JFZ on 2017/11/3.
 * <p>
 * 日志打印类
 */

public class Logger {
    public static final String TAG = "##看门狗##";
    private static final String SUFFIX = ".java";
    //    public static final boolean DEBUG = BuildConfig.DEBUG;
    public static final boolean DEBUG = true;
    public static boolean enabled = false;

    public static void d(String value) {
        if (DEBUG) {
            Log.d(TAG, value);
        }
    }

    public static void e(String value) {
        if (DEBUG) {
        Log.e(TAG, value);
        }
    }

    public static void e(Exception value) {
        if (DEBUG && value != null) {
            Log.e(TAG, value.getMessage());
        }
    }

    public static void i(String value) {
        if (DEBUG) {
            Log.i(TAG, value);
        }
    }

    public static void w(String value) {
        if (DEBUG) {
            Log.w(TAG, value);
        }
    }

    public static void e(String msg, Throwable e) {
        if (!DEBUG) {
            return;
        }
        Log.e(TAG, msg, e);
    }

    public static void e(String msg, String e) {
        if (!DEBUG) {
//			return;
        }
        Log.e("看门狗", msg + ":" + e);
    }

    public static String logDetail() {
        String result = "\n";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result += thisMethodStack.getClassName() + "."; // 当前的类名（全名）
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")  \n";
        Log.e(TAG, result);
        return result;
    }

    /**
     * 堆栈跟踪log,自定义tag
     */
    public static void log(String tag, String msg) {
        if (!DEBUG) {
            // return;
        }
        String result = "\n";
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

//        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        StackTraceElement thisMethodStack = stackTrace[5];

        String className = thisMethodStack.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }
        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }
        result += thisMethodStack.getClassName() + "."; // 当前的类名（全名）
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")  \n";

//        if (DEBUG) {
//            Log.i("狗狗", result + "\n" + msg);
//        }
        if (TextUtils.isEmpty(tag)) {
            Log.e(TAG, "***start*************************************************************\n");
            Log.e(TAG, result + "\n" + msg);
            Log.e(TAG, "***end*************************************************************\n");
        } else {
            Log.e("看门狗", "***start*************************************************************\n");
            Log.e("看门狗", tag + ":" + result + "\n" + msg);
            Log.e("看门狗", "***end*************************************************************\n");
        }

    }


    /**
     * 堆栈跟踪log,自定义tag
     */
    public static void tst(String tag, String msg) {
        String result = "\n";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[1];
        result += thisMethodStack.getClassName() + "."; // 当前的类名（全名）
        result += thisMethodStack.getMethodName();
        result += "(" + thisMethodStack.getFileName();
        result += ":" + thisMethodStack.getLineNumber() + ")  \n";
        Log.e(tag, result + "\n" + msg);
        Log.e("dog", result + "\n" + msg);
    }

    /**
     * 堆栈跟踪log,自定义tag
     */
    public static void tst(String msg) {
        tst("", msg);
    }

    /**
     * 堆栈跟踪log
     */
    public static void log(String msg) {
        log("jfz", msg);
    }
}
