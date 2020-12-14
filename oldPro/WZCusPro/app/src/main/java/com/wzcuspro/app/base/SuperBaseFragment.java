package com.wzcuspro.app.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;

import com.wzcuspro.app.callback.BaseInitCallback;
import com.wzcuspro.app.utils.Logger;

import weiying.customlib.app.handler.HandlerHelperAdapter;
import weiying.customlib.app.receive.Actions;
import weiying.customlib.app.receive.IBroadcastRecvHandler;
import weiying.customlib.app.receive.RecvCallBack;


/**
 * Created by JFZ
 * on 2018/5/20.
 */

public abstract class SuperBaseFragment extends Fragment implements BaseInitCallback, View.OnClickListener, Handler.Callback, RecvCallBack, Actions {

    protected String TAG = getClass().getSimpleName();
    protected View mRootView;
    protected AppCompatActivity mAct;
    protected Context mContext;

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
    protected HandlerHelperAdapter<AppCompatActivity> mHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mAct = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new HandlerHelperAdapter<AppCompatActivity>(mAct, this);
    }

    @Override
    public View createView() {
        return new View(mContext);
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
        LocalBroadcastManager.getInstance(BaseApp.getInstance()).registerReceiver(
                mBroadcastReceiv, mReceivFilter);
    }

    @Override
    public void onResume() {
//        Logger.e(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
//        Logger.e(TAG, "onStart");
    }

    @Override
    public void onPause() {
//        Logger.e(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        Logger.e(TAG, "onStop");
    }

    @Override
    public void onDetach() {
        try {
            if (mBroadcastReceiv != null) {
                LocalBroadcastManager.getInstance(BaseApp.getInstance()).unregisterReceiver(
                        mBroadcastReceiv);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mContext = null;
        Logger.e(TAG, "#视图销毁#");
        super.onDetach();
    }

    @Override
    public void onDestroy() {
//        Logger.e(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 广播处理器
     */
    @Override
    public void onReceive(Context context, Intent intent) {
//        Logger.e(TAG, "##onReceive##");
        if (intent != null) {
            String act = intent.getAction();
            onSafeReceiv(context, intent, act);
        }
    }

    /**
     * 广播处理器
     */
    protected void onSafeReceiv(Context context, Intent intent, String action) {
//        Logger.e(TAG, "##receiv##");
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return true;
    }

    public <T extends View> T findViewById(@IdRes int id) {
        SparseArray<View> hashMap = (SparseArray<View>) mRootView.getTag();
        if (hashMap == null) {
            hashMap = new SparseArray<View>();
            mRootView.setTag(hashMap);
        }
        View childView = hashMap.get(id);
        if (childView == null) {
            childView = mRootView.findViewById(id);
            hashMap.put(id, childView);
        }
        return (T) childView;
    }
}
