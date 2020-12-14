package com.wzcuspro.app.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.wzcuspro.app.callback.BaseInitCallback;

import weiying.customlib.app.ActivityManager;
import weiying.customlib.app.HomeKeyWatcher;
import weiying.customlib.app.handler.HandlerHelperAdapter;
import weiying.customlib.app.receive.Actions;
import weiying.customlib.app.receive.IBroadcastRecvHandler;
import weiying.customlib.app.receive.RecvCallBack;
import weiying.customlib.barlib.ImmersionBar;
import weiying.customlib.slide.SlideBackActivity;

public abstract class SuperInitActivity extends SlideBackActivity implements LayoutInflaterFactory, BaseInitCallback, Actions, Handler.Callback, RecvCallBack, HomeKeyWatcher.OnHomePressedListener {
    protected String TAG = getClass().getSimpleName();
    protected Activity mContext;
    private boolean pressedHome;
    private HomeKeyWatcher mHomeKeyWatcher;
    protected ImmersionBar immersionBar;
    /**
     * 广播
     */
    protected IBroadcastRecvHandler mBroadcastReceiv;
    /**
     * 广播过滤
     */
    protected IntentFilter mReceivFilter;

    /**
     * 安全的handler
     */
    protected HandlerHelperAdapter<Activity> mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        onCreateBefore(savedInstanceState);
        super.onCreate(savedInstanceState);
        onCreateAfter(savedInstanceState);
        mContext = this;
        mHandler = new HandlerHelperAdapter<Activity>(mContext, this);
        ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈
        if (mHomeKeyWatcher == null) {
            mHomeKeyWatcher = new HomeKeyWatcher(this);
            mHomeKeyWatcher.setOnHomePressedListener(this);
            pressedHome = false;
            if (!mHomeKeyWatcher.isRegRecevier()) {
                mHomeKeyWatcher.startWatch();
            }
        }
        setSlideable(isSlideable());
        printActivityLife("onCreate");
    }

    //左滑退出界面必须重写 setContentView
    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        try {
            super.setContentView(view);
        } catch (Exception e) {
        }
    }

    //onCreate初始化之前
    private void onCreateBefore(Bundle savedInstanceState) {
    }

    //onCreate初始化之后
    private void onCreateAfter(Bundle savedInstanceState) {
    }

    @Override
    public View createView() {
        return new View(this);
    }

    public void styleBar(Activity activity) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onRestart() {
        mHomeKeyWatcher.startWatch();
        pressedHome = false;
        super.onRestart();
        printActivityLife("onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        printActivityLife("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        printActivityLife("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHomeKeyWatcher.stopWatch();
        printActivityLife("onStop");
    }


    @Override
    protected void onPause() {
        printActivityLife("onPause");
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        printActivityLife("onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        printActivityLife("onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        printActivityLife("onDestroy");
        if (immersionBar != null) {
            printActivityLife("清除状态栏样式");
            immersionBar.destroy();
            immersionBar = null;
        }
        ActivityManager.getAppInstance().removeActivity(this);//将当前activity移除管理栈
        if (mBroadcastReceiv != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
                    mBroadcastReceiv);
        }
        if (mHomeKeyWatcher != null && mHomeKeyWatcher.isRegRecevier()) {
            mHomeKeyWatcher.stopWatch();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
        mContext = null;
    }

    @Override
    public void onHomePressed() {
        pressedHome = true;
    }

    /**
     * 广播回调.空实现
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        printActivityLife(TAG + ":onReceive=======================");
        if (intent != null) {
            String action = intent.getAction();
            onSafeReceive(intent, action);
        }
    }

    /**
     * 广播回调.空实现
     */
    public void onSafeReceive(Intent intent, String action) {
        printActivityLife(TAG + ":onSafeReceive=====广播==================");
    }


    @Override
    public boolean handleMessage(Message msg) {
        System.out.println(TAG + "<消息========拦截========");
        return true; // return true callback 拦截消息,自己处理
    }

    /**
     * 注册广播
     */
    public void regBroadcastRecv(String... actions) {
        if (mBroadcastReceiv == null || mReceivFilter == null) {
            mBroadcastReceiv = new IBroadcastRecvHandler(this);
            mReceivFilter = new IntentFilter();
        }
        if (actions != null) {
            for (String act : actions) {
                mReceivFilter.addAction(act);
            }
        }
        LocalBroadcastManager.getInstance(mContext).registerReceiver(
                mBroadcastReceiv, mReceivFilter);
    }

    @Override
    public boolean isSlideable() {
        return false;
    }

    protected void printActivityLife(String method) {
        Log.i(TAG, String.format("######Activity:%s #####Method:%s", TAG, method));
    }

}
