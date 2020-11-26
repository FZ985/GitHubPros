package com.okhttplib2;

import com.okhttplib2.callback.Http;
import com.okhttplib2.config.RequestBuilder;

import expand.download.DownLoadBuilder3;
import expand.download.DownLoadInfo2;
import expand.download.DownLoadListenerAdapter;

/**
 * Create by JFZ
 * date: 2020-09-15 13:47
 **/
public class HttpImpl {

    public static Http.Builder get(String url) {
        return new RequestBuilder(url, Http.GET, Http.NO);
    }

    public static Http.Builder postJson(String url) {
        return new RequestBuilder(url, Http.POST, Http.JSON);
    }

    public static Http.Builder postParams(String url) {
        return new RequestBuilder(url, Http.POST, Http.PARAMS);
    }

    public static Http.Builder postForm(String url) {
        return new RequestBuilder(url, Http.POST, Http.FORM);
    }

    public static Http.Builder putJson(String url) {
        return new RequestBuilder(url, Http.PUT, Http.JSON);
    }

    public static RequestBuilder putParams(String url) {
        return new RequestBuilder(Http.PUT, Http.PARAMS);
    }

    public static Http.Builder putForm(String url) {
        return new RequestBuilder(url, Http.PUT, Http.FORM);
    }

    public static Http.Builder delete(String url) {
        return new RequestBuilder(url, Http.DELETE, Http.NO);
    }

    public static Http.Builder deleteJson(String url) {
        return new RequestBuilder(url, Http.DELETE, Http.JSON);
    }

    public static Http.Builder deleteParams(String url) {
        return new RequestBuilder(url, Http.DELETE, Http.PARAMS);
    }

    public static Http.Builder deleteForm(String url) {
        return new RequestBuilder(url, Http.DELETE, Http.FORM);
    }

    public static Http.Builder headParams(String url) {
        return new RequestBuilder(url, Http.HEAD, Http.PARAMS);
    }

    public static Http.Builder headForm(String url) {
        return new RequestBuilder(url, Http.HEAD, Http.FORM);
    }

    public static Http.Builder patchJson(String url) {
        return new RequestBuilder(url, Http.PATCH, Http.JSON);
    }

    public static Http.Builder patchParams(String url) {
        return new RequestBuilder(url, Http.PATCH, Http.PARAMS);
    }

    public static Http.Builder patchForm(String url) {
        return new RequestBuilder(url, Http.PATCH, Http.FORM);
    }

    //多文件文件下载，支持断点下载
    public static void download3(String url, String path, String fileName, DownLoadListenerAdapter call) {
        DownLoadBuilder3.getInstance().download(new DownLoadInfo2(url, path, fileName), call);
    }
}
