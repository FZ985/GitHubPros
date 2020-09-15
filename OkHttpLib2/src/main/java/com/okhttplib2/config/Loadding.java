package com.okhttplib2.config;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 * Create by JFZ
 * date: 2020-09-15 13:58
 * 全局加载进度
 **/
public abstract class Loadding {
    public abstract void show();

    public abstract void dismiss();

    public abstract boolean isShowing();

    protected Activity scanForActivity(Context cont) {
        if (cont == null)
            return null;
        else if (cont instanceof Activity)
            return (Activity) cont;
        else if (cont instanceof ContextWrapper)
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        return null;
    }
}
