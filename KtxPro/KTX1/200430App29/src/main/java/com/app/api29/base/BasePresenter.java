package com.app.api29.base;

import android.app.Activity;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Create by JFZ
 * date: 2019-06-06 17:47
 **/
public abstract class BasePresenter<V extends BaseView> implements IPresenter<V> {
    //定义view的软引用避免内存泄漏
    private Reference<V> mViewRef;
    //定义activity的软引用
    private Reference<Activity> mActivity;
    @Override
    public void attachView(V view, Activity activity) {
        if (view != null) {
            mViewRef = new WeakReference<V>(view);
        }
        if (activity != null) {
            mActivity = new WeakReference<Activity>(activity);
        }
        if (activity instanceof AppCompatActivity){
            ((AppCompatActivity) activity).getLifecycle();
        }
    }

    protected V getView() {
        return mViewRef.get();
    }

    protected Activity getActivity() {
        return mActivity.get();
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void dettachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mActivity != null) {
            mActivity.clear();
            mActivity = null;
        }
    }
}
