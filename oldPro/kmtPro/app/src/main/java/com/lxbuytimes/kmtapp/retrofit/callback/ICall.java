package com.lxbuytimes.kmtapp.retrofit.callback;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Create by JFZ
 * date: 2019-10-16 13:58
 **/
public abstract class ICall<T> implements Observer<T> {

    private Context mContext;
    private Loadding loadding;
    private Disposable d;

    public ICall(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    public ICall(Context mContext, Loadding loadding) {
        this.mContext = mContext.getApplicationContext();
        this.loadding = loadding;
    }

    public ICall(Loadding loadding) {
        if (loadding !=null){
            this.loadding = loadding;
        }
    }

    public ICall() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.d = d;
        if (loadding != null && !loadding.isShowing()) {
            loadding.show();
        }
        if (mContext != null && !isConnected(mContext)) {
            HttpUtils.log("未连接网络,取消订阅");
            if (!d.isDisposed()) {
                d.dispose();
            }
            dismissLoad();
            notNetWork();
        }
    }

    @Override
    public void onNext(T t) {
//        HttpUtils.log("onNext:" + t);
        if (d != null && !d.isDisposed()) d.dispose();
        dismissLoad();
        onResponse(t);
    }

    @Override
    public void onError(Throwable e) {
        HttpUtils.log("onError:" + e.getMessage());
        if (d != null && !d.isDisposed()) d.dispose();
        dismissLoad();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            if (code == 502 || code == 404) {
                msg = "网络拥堵，请稍后再试";
            }
            onCallError(code, e);
        } else {
            onCallError(-1,e);
        }
        onFinish();
    }

    public void onFinish() {
    }

    private void dismissLoad() {
        if (loadding != null && loadding.isShowing()) {
            loadding.dismiss();
        }
    }

    public abstract void onResponse(T data);

    public abstract void onCallError(int code, Throwable e);

    /**
     * 开始请求时，无网络调用此方法
     * 没有联网
     */
    public void notNetWork() {
    }

    @Override
    public void onComplete() {
        HttpUtils.log("onComplete");
        onFinish();
    }

    private boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isAvailable();
    }
}
