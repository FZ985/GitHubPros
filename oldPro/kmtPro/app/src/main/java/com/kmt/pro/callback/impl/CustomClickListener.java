package com.kmt.pro.callback.impl;

import android.view.View;

/**
 * Create by JFZ
 * date: 2020-04-14 19:29
 **/
public abstract class CustomClickListener implements View.OnClickListener {
    private long mLastClickTime;
    private long timeInterval = 1000L;

    public CustomClickListener() {

    }

    public CustomClickListener(long interval) {
        this.timeInterval = interval;
    }

    @Override
    public void onClick(View v) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastClickTime > timeInterval) {
            // 单次点击事件
            onSingleClick(v);
            mLastClickTime = nowTime;
        } else {
            // 快速点击事件
            onFastClick(v);
        }
    }

    protected abstract void onSingleClick(View v);

    protected void onFastClick(View v) {
    }
}
