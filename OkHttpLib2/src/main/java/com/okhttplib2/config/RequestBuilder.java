package com.okhttplib2.config;

import android.app.Activity;
import android.text.TextUtils;

import com.okhttplib2.callback.Http;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 16:25
 * 请求创建者
 **/
public class RequestBuilder implements Http.Builder {
    private int requestWay, requestType;
    private String requestUrl;
    private long startReqTime = System.currentTimeMillis();//开始请求时间
    private JRequest request;
    private Loadding loadding;
    private Lifecycle mLifecycleRegistry;
    private RequestImpl requestImpl;

    public RequestBuilder(int requestWay, int requestType) {
        this.requestWay = requestWay;
        this.requestType = requestType;
    }

    public RequestBuilder(String requestUrl, int requestWay, int requestType) {
        this.requestWay = requestWay;
        this.requestType = requestType;
        this.requestUrl = requestUrl;
    }

    @Override
    public RequestBuilder url(String url) {
        this.requestUrl = url;
        return this;
    }

    @Override
    public RequestBuilder request(JRequest request) {
        this.request = request;
        return this;
    }

    private class Builder implements Http.GETBuilder {
        @Override
        public int requestWay() {
            return requestWay;
        }

        @Override
        public int requestType() {
            return requestType;
        }

        @Override
        public String url() {
            return requestUrl;
        }

        @Override
        public Loadding load() {
            return loadding;
        }

        @Override
        public JRequest request() {
            return request;
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return mLifecycleRegistry;
        }

        @Override
        public long requestTime() {
            return startReqTime;
        }
    }

    @Override
    public RequestBuilder load(Loadding loadding) {
        this.loadding = loadding;
        return this;
    }

    @Override
    public RequestBuilder bind(Activity activity) {
        if (activity != null && activity instanceof AppCompatActivity) {
            return bind(((AppCompatActivity) new WeakReference<>(activity).get()).getLifecycle());
        }
        return this;
    }

    @Override
    public RequestBuilder bind(Fragment fragment) {
        if (fragment != null) {
            return bind(fragment.getLifecycle());
        }
        return this;
    }

    @Override
    public RequestBuilder bind(Lifecycle lifecycle) {
        this.mLifecycleRegistry = lifecycle;
        return this;
    }

    private void create() {
        startReqTime = System.currentTimeMillis();
        requestImpl = new RequestImpl(new Builder());
    }

    @Override
    public Response execute() throws Exception {
        create();
        if (TextUtils.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("The url is null!");
        }
        return requestImpl.execute();
    }

    @Override
    public Object executeObject() throws Exception {
        create();
        if (TextUtils.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("The url is null!");
        }
        return requestImpl.executeObject();
    }

    @Override
    public Response executeUploadFile(File[] fils, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener) throws Exception {
        create();
        if (TextUtils.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("The url is null!");
        }
        if (fils == null || fils.length == 0 || fileKeys == null || fileKeys.length == 0) {
            throw new IllegalArgumentException("Please check files or filekeys!");
        }
        return requestImpl.executeUploadFile(fils, fileKeys, uiProgressRequestListener);
    }

    @Override
    public void enqueue(RequestCallback<?> callback) {
        create();
        if (TextUtils.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("The url is null!");
        }
        requestImpl.enqueue(callback);
    }

    @Override
    public void enqueueUploadFile(File[] fils, String[] fileKeys, RequestCallback<?> callback, UIProgressRequestListener uiProgressRequestListener) {
        create();
        if (TextUtils.isEmpty(requestUrl)) {
            throw new IllegalArgumentException("The url is null!");
        }
        if (fils == null || fils.length == 0 || fileKeys == null || fileKeys.length == 0) {
            throw new IllegalArgumentException("Please check files or filekeys!");
        }
        requestImpl.enqueueUploadFile(fils, fileKeys, callback, uiProgressRequestListener);
    }
}
