package com.okhttplib2.callback;

import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.config.OkHttpConfig;
import com.okhttplib2.utils.OkhttpUtil;

import java.io.IOException;

import androidx.lifecycle.Lifecycle;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2020-09-15 17:03
 **/
public class BaseCallback implements Callback {
    private Http.GETBuilder builder;
    private Http.UICall uiCall;
    private RequestCallback callback;

    public BaseCallback(Http.GETBuilder builder, Http.UICall uiCall, RequestCallback callback) {
        this.builder = builder;
        this.uiCall = uiCall;
        this.callback = callback;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        final long responseTime = System.currentTimeMillis();
        log(builder.url() + "<<error响应时间start:" + builder.requestTime() + ",end:" + responseTime + ",total:" + ((responseTime - builder.requestTime())) + "ms");
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                uiCall.dismissLoadding();
                if (call.isCanceled()) return;
                if (builder.getLifecycle() != null && builder.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                    return;
                }
                if (checkResponseInterceptorErr(e))
                    uiCall.sendFailedCall(callback, -1, e);
            }
        });
    }

    @Override
    public void onResponse(final okhttp3.Call call, Response response) throws IOException {
        OkHttpFactory.getInstance().obtainHandler().post(new Runnable() {
            @Override
            public void run() {
                uiCall.dismissLoadding();
                if (call.isCanceled()) return;
            }
        });
        if (response == null) {
            log("响应对象response 为空");
            return;
        }
        if (callback == null) return;

        if (builder.getLifecycle() != null && builder.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        if (callback.mType != null && callback.mType == Response.class) {
            uiCall.sendSuccessCall(callback, response);
            return;
        }
        try {
            String str = response.body().string();
            String info = "";
            final int code = response.code();
            final boolean success = response.isSuccessful();
            final long responseTime = System.currentTimeMillis();
            log(builder.url() + "<<success响应时间start:" + builder.requestTime() + ",end:" + responseTime + ",总计:" + (responseTime - builder.requestTime()) + "ms");
            log(success + "," + code + "<<" + builder.url() + "<<返回数据:" + str);
            info = str;
            if (success) {
                if (callback.mType != null && !TextUtils.isEmpty(info)) {
                    if (checkResponseInterceptor(code, info)) {
                        if (callback.mType == String.class) {
                            uiCall.sendSuccessCall(callback, info);
                        } else {
                            Object o = OkhttpUtil.GSON.fromJson(info,
                                    callback.mType);
                            if (o == null) {
                                uiCall.sendFailedCall(callback, code, new Exception("Httppi:mGson.fromJson(finalStr,callback.mType) return null!"));
                            } else uiCall.sendSuccessCall(callback, o);
                        }
                    }
                } else
                    uiCall.sendFailedCall(callback, code, new Exception(":返回数据 or 解析类型为空"));
            } else
                uiCall.sendFailedCall(callback, code, new Exception(":response.isSuccessful() is not be true!"));
        } catch (JsonParseException e) {
            log("json解析失败:" + e.getMessage());
            uiCall.sendFailedCall(callback, -1, e);
        } catch (Exception e) {
            log("请求失败：" + e.getMessage());
            uiCall.sendFailedCall(callback, -1, e);
        }
    }

    private boolean checkResponseInterceptor(int code, String result) {
        if (OkHttpConfig.getInstance().getResponseInterceptor() != null) {
            return OkHttpConfig.getInstance().getResponseInterceptor().interceptorResponse(builder, uiCall, callback, code, result);
        }
        return true;
    }

    private boolean checkResponseInterceptorErr(Exception e) {
        if (OkHttpConfig.getInstance().getResponseInterceptor() != null) {
            return OkHttpConfig.getInstance().getResponseInterceptor().interceptorResponseErr(builder, uiCall, callback, e);
        }
        return true;
    }

    private void log(String m) {
        OkhttpUtil.log("BaseCallback", m);
    }
}
