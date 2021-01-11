package com.okhttplib2.callback;

import android.app.Activity;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.okhttplib2.config.JRequest;
import com.okhttplib2.config.Loadding;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 14:12
 **/
public interface Http {
    int GET = 1;
    int POST = 2;
    int PUT = 3;
    int DELETE = 4;
    int HEAD = 5;
    int PATCH = 6;

    int NO = -1;
    int JSON = 1;
    int PARAMS = 2;
    int FORM = 3;
    int BODY = 4;

    interface Config {
        //获取handler
        Handler obtainHandler();

        OkHttpClient client();

        OkHttpClient.Builder bulid();

        //取消指定请求
        void cancel(Object tag);

        //取消全部请求
        void cancelAll();

        void newBuild();

        void cancelHttp(Object tag);
    }

    interface Call {
        Response execute() throws Exception;

        Object executeObject() throws Exception;

        Response executeUploadFile(File[] fils, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener) throws Exception;

        void enqueue(RequestCallback<?> callback);

        void enqueueUploadFile(File[] fils, String[] fileKeys, RequestCallback<?> callback, UIProgressRequestListener uiProgressRequestListener);

    }

    interface UICall {
        void showLoadding();

        void dismissLoadding();

        void sendSuccessCall(final RequestCallback callback, final Object obj);

        void sendFailedCall(final RequestCallback callback, final int code, final Exception e);
    }

    interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    interface Builder extends Http.Call {

        Http.Builder url(String url);

        Http.Builder request(JRequest request);

        Http.Builder load(Loadding loadding);

        Http.Builder upRequestBody(RequestBody body);

        Http.Builder bind(Activity activity);

        Http.Builder bind(Fragment fragment);

        Http.Builder bind(Lifecycle lifecycle);

    }

    interface GETBuilder {

        int requestWay();

        int requestType();

        long requestTime();

        String url();

        Loadding load();

        JRequest request();

        RequestBody requestBody();

        @NonNull
        Lifecycle getLifecycle();
    }

    //全局拦截 请求和响应
    interface RequestInterceptor {
        boolean interceptorRequest(GETBuilder builder);
    }

    interface ResponseInterceptor {
        boolean interceptorResponse(GETBuilder builder, UICall uiCall, RequestCallback<?> callback, int code, String result);

        boolean interceptorResponseErr(GETBuilder builder, UICall uiCall, RequestCallback<?> callback, Exception e);
    }
}
