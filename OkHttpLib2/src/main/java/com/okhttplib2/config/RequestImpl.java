package com.okhttplib2.config;

import android.text.TextUtils;

import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.BaseCallback;
import com.okhttplib2.callback.Http;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.upload.UIProgressRequestListener;
import com.okhttplib2.utils.OkHttpAsyncTask;
import com.okhttplib2.utils.OkResponseStringTask;
import com.okhttplib2.utils.OkhttpUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 14:10
 * 实现网络请求
 **/
public class RequestImpl implements Http.Call, Http.UICall {
    private Http.GETBuilder builder;
    private OkHttpAsyncTask task;

    public RequestImpl(Http.GETBuilder builder) {
        this.builder = builder;
    }

    @Override
    public Response execute() throws Exception {
        return executeUploadFile(null, null, null);
    }

    @Override
    public Object executeObject() throws Exception {
        if (!checkRequestInterceptor()) return null;
        try {
            Response response = execute();
            if (response == null) return null;
            String finalObj = new OkResponseStringTask().executeOnExecutor(Executors.newCachedThreadPool(), response).get();
            boolean success = response.isSuccessful();
            int code = response.code();
            log("##success:" + success + ",code:" + code + ",解析前数据:" + finalObj);
            if (success && !TextUtils.isEmpty(finalObj)) {
                if (builder.request() != null && builder.request().cls != null && builder.request().cls != String.class) {
                    return OkhttpUtil.GSON.fromJson(finalObj, builder.request().cls);
                } else
                    return finalObj;
            }
        } catch (Exception e) {
            log("###" + OkhttpUtil.typeTag(builder) + "同步请求出错:" + e.getMessage());
        }
        return null;
    }

    @Override
    public Response executeUploadFile(File[] fils, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener) throws Exception {
        if (TextUtils.isEmpty(builder.url())) {
            throw new NullPointerException("url can not be null!");
        }
        if (!(builder.url().startsWith("http") || builder.url().startsWith("https"))) {
            throw new IllegalArgumentException("The url prefix is not http or https!");
        }
        if (!checkRequestInterceptor()) return null;
        showLoadding();
        return getTask(OkhttpUtil.getRequest(builder, fils, fileKeys, uiProgressRequestListener));
    }

    @Override
    public void enqueue(RequestCallback<?> callback) {
        enqueueUploadFile(null, null, callback, null);
    }

    @Override
    public void enqueueUploadFile(File[] files, String[] fileKeys, RequestCallback<?> callback, UIProgressRequestListener uiProgressRequestListener) {
        if (!checkRequestInterceptor()) {
            log("内部拦截请求====");
            return;
        }
        if (TextUtils.isEmpty(builder.url())) {
            sendFailedCall(callback, -1, new NullPointerException("url can not be null!"));
            return;
        }
        if (!(builder.url().startsWith("http") || builder.url().startsWith("https"))) {
            sendFailedCall(callback, -1, new IllegalArgumentException("The url prefix is not http or https!"));
            return;
        }
        Request request = OkhttpUtil.getRequest(builder, files, fileKeys, uiProgressRequestListener);
        if (request == null) return;
        showLoadding();
        log(builder.url() + "<<开始请求时间:" + builder.requestTime());
        asyncCall(request, callback);
    }

    private void asyncCall(final Request request, final RequestCallback<?> callback) {
        OkHttpFactory.getInstance().client().newCall(request).enqueue(new BaseCallback(builder, this, callback) {
            @Override
            public void onFailure(Call call, IOException e) {
                super.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                super.onResponse(call, response);
            }
        });
    }

    @Override
    public void showLoadding() {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (builder.load() != null && !builder.load().isShowing())
                    builder.load().show();
            }
        });
    }

    @Override
    public void dismissLoadding() {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (builder.load() != null && builder.load().isShowing())
                    builder.load().dismiss();
            }
        });
    }

    @Override
    public void sendSuccessCall(RequestCallback callback, Object obj) {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onResponse(obj);
            }
        });
    }

    @Override
    public void sendFailedCall(RequestCallback callback, int code, Exception e) {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) callback.onError(code, e);
            }
        });
    }

    private Response getTask(Request req) throws Exception {
        if (task != null) task.cancel(true);
        task = null;
        task = new OkHttpAsyncTask(builder.load());
        task.executeOnExecutor(Executors.newCachedThreadPool(), req);
        Response response = task.get();
        if (response != null) {
            task.cancel(true);
            task = null;
        }
        return response;
    }

    private boolean checkRequestInterceptor() {
        if (OkHttpConfig.getInstance().getRequestInterceptor() != null) {
            return OkHttpConfig.getInstance().getRequestInterceptor().interceptorRequest(builder);
        }
        return true;
    }

    public void log(String m) {
        OkhttpUtil.log("RequestImpl", m);
    }
}
