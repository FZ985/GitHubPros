package com.lxbuytimes.kmtapp.retrofit.def;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.google.gson.JsonParseException;
import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.BaseRequestParams;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestListener;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Create by JFZ
 * date: 2019-10-21 18:31
 * 默认的异步网络请求管理
 **/
public class DefEnqueueHttp {

    public static void enqueue(int type, String url, BaseRequestParams params, DefaultCallback<?> callback, ProgressRequestListener progressListener, Loadding loadding) {
        if (callback == null) return;
        if (TextUtils.isEmpty(url)) {
            dismissLoadding(loadding);
            callback.onCallError(-1,new NullPointerException("the url is null!"));
            return;
        }
        if (!URLUtil.isNetworkUrl(url)) {
            dismissLoadding(loadding);
            callback.onCallError(-1,new Exception("the url is not http:// or https://"));
            return;
        }
        Uri uri = Uri.parse(url);
        String domain = uri.getScheme() + "://" + uri.getAuthority() + "/";
        String path = TextUtils.isEmpty(uri.getPath()) ? "/" : uri.getPath();
        DefaultApi api = RetrofitManager.getInstance().getRetrofit(domain).create(DefaultApi.class);
        Observable<ResponseBody> observable = null;
        switch (type) {
            case HttpUtils.GET:
                if (params == null) observable = api.enqueueGet(path,params.getHeaders());
                else
                    observable = api.enqueueGet(path, (params.requestParams == null ? new HashMap<>() : params.requestParams),params.getHeaders());
                break;
            case HttpUtils.POST:
                if (params == null) observable = api.enqueuePost(path,params.getHeaders());
                else
                    observable = api.enqueuePost(path, (params.requestParams == null ? new HashMap<>() : params.requestParams),params.getHeaders());
                break;
            case HttpUtils.JSON:
                String json = (params != null && params.requestBean != null) ? HttpUtils.reqParams(params.requestBean) : "";
                HttpUtils.log(url + "<<<请求json:" + json);
                observable = api.enqueuePostJson(path, RequestBody.create(HttpUtils.MediaTypes.APPLICATION_JSON_TYPE, json),params.getHeaders());
                break;
            case HttpUtils.FORM:
                RequestBody body = HttpUtils.Upload.createRequestBody(params);
                observable = api.postForm(path, progressListener == null ? body : HttpUtils.Upload.addProgressRequestListener(body, progressListener),params.getHeaders());
                break;
        }
        if (observable != null) {
            observable.compose(RxHelper.observableIO2Main())
                    .subscribe(new ICall<ResponseBody>() {
                        @Override
                        public void onResponse(ResponseBody data) {
                            dismissLoadding(loadding);
                            try {
                                if (callback.mType != null && callback.mType == ResponseBody.class) {
                                    sendSuccessCall(callback, data);
                                    return;
                                }

                                String str = data.string();
                                String info = "";
                                HttpUtils.log(url + "<<返回数据:" + str);
                                info = str;
                                if (callback.mType != null && !TextUtils.isEmpty(info)) {
                                    if (callback.mType == String.class) {
                                        sendSuccessCall(callback, info);
                                    } else {
                                        Object o = RetrofitManager.GSON.fromJson(info,
                                                callback.mType);
                                        if (o == null) {
                                            sendFailedCall(callback,1, new Exception("Httppi:mGson.fromJson(finalStr,callback.mType) return null!"));
                                        } else sendSuccessCall(callback, o);
                                    }
                                } else
                                    sendFailedCall(callback,1, new Exception(":回调 or 返回数据 or 解析类型为空"));
                            } catch (JsonParseException e) {
                                HttpUtils.log("json解析失败:" + e.getMessage());
                                sendFailedCall(callback, 1,e);
                            } catch (Exception e) {
                                HttpUtils.log("请求失败：" + e.getMessage());
                                sendFailedCall(callback,1, e);
                            }
                        }

                        @Override
                        public void onCallError(int code,Throwable e) {
                            dismissLoadding(loadding);
                            callback.onCallError(-1,e);
                        }
                    });
        }
    }

    private static void sendFailedCall(final DefaultCallback callback,int code, final Exception e) {
        if (callback != null) callback.onCallError(code,e);
    }

    private static void sendSuccessCall(final DefaultCallback callback, final Object obj) {
        if (callback != null) callback.onResponse(obj);
    }

    private static void dismissLoadding(Loadding loadding) {
        if (loadding != null && loadding.isShowing()) loadding.dismiss();
        loadding = null;
    }
}
