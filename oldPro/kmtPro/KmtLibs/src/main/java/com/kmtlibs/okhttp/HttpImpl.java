package com.kmtlibs.okhttp;


import com.kmtlibs.okhttp.builder.DownLoadBuilder;
import com.kmtlibs.okhttp.builder.DownLoadBuilder2;
import com.kmtlibs.okhttp.builder.DownLoadBuilder3;
import com.kmtlibs.okhttp.builder.MethodBuilder;
import com.kmtlibs.okhttp.callback.Http;
import com.kmtlibs.okhttp.file.download.DownLoadInfo;
import com.kmtlibs.okhttp.file.download.DownLoadInfo2;
import com.kmtlibs.okhttp.file.download.DownLoadListenerAdapter;
import com.kmtlibs.okhttp.tool.OkhttpUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Interceptor;

public class HttpImpl {

    public static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(20);

    private static List<Interceptor> interceptorss = new ArrayList<>();

    public static MethodBuilder get(String url) {
        return new MethodBuilder(url, Http.GET, Http.NO);
    }

    public static MethodBuilder postJson(String url) {
        return new MethodBuilder(url, Http.POST, Http.JSON);
    }

    public static MethodBuilder postParams(String url) {
        return new MethodBuilder(url, Http.POST, Http.PARAMS);
    }

    public static MethodBuilder postForm(String url) {
        return new MethodBuilder(url, Http.POST, Http.FORM);
    }

    public static MethodBuilder putJson(String url) {
        return new MethodBuilder(url, Http.PUT, Http.JSON);
    }

    public static MethodBuilder putParams(String url) {
        return new MethodBuilder(Http.PUT, Http.PARAMS);
    }

    public static MethodBuilder putForm(String url) {
        return new MethodBuilder(url, Http.PUT, Http.FORM);
    }

    public static MethodBuilder delete(String url) {
        return new MethodBuilder(url, Http.DELETE, Http.NO);
    }

    public static MethodBuilder deleteJson(String url) {
        return new MethodBuilder(url, Http.DELETE, Http.JSON);
    }

    public static MethodBuilder deleteParams(String url) {
        return new MethodBuilder(url, Http.DELETE, Http.PARAMS);
    }

    public static MethodBuilder deleteForm(String url) {
        return new MethodBuilder(url, Http.DELETE, Http.FORM);
    }

    public static MethodBuilder headParams(String url) {
        return new MethodBuilder(url, Http.HEAD, Http.PARAMS);
    }

    public static MethodBuilder headForm(String url) {
        return new MethodBuilder(url, Http.HEAD, Http.FORM);
    }

    public static MethodBuilder patchJson(String url) {
        return new MethodBuilder(url, Http.PATCH, Http.JSON);
    }

    public static MethodBuilder patchParams(String url) {
        return new MethodBuilder(url, Http.PATCH, Http.PARAMS);
    }

    public static MethodBuilder patchForm(String url) {
        return new MethodBuilder(url, Http.PATCH, Http.FORM);
    }

    public static void download(String url, String path, String fileName, DownLoadListenerAdapter call) {
        DownLoadBuilder.getInstance().download(new DownLoadInfo(url, path, fileName), call);
    }

    //单文件下载，支持断点下载
    @Deprecated
    public static void download2(String url, String path, String fileName, DownLoadListenerAdapter call) {
        DownLoadBuilder2.getInstance().download(new DownLoadInfo2(url, path, fileName), call);
    }

    //多文件文件下载，支持断点下载 DownLoadBuilder2的优化
    public static void download3(String url, String path, String fileName, DownLoadListenerAdapter call) {
        DownLoadBuilder3.getInstance().download(new DownLoadInfo2(url, path, fileName), call);
    }

    public static void enable(boolean logDebug) {
        OkhttpUtil.LogDebug(logDebug);
    }

    /***==过时函数，但仍可以使用=====================================================***/
    @Deprecated
    public static MethodBuilder get() {
        return new MethodBuilder(Http.GET, Http.NO);
    }

    @Deprecated
    public static MethodBuilder postJson() {
        return new MethodBuilder(Http.POST, Http.JSON);
    }

    @Deprecated
    public static MethodBuilder postParams() {
        return new MethodBuilder(Http.POST, Http.PARAMS);
    }

    @Deprecated
    public static MethodBuilder postForm() {
        return new MethodBuilder(Http.POST, Http.FORM);
    }

    @Deprecated
    public static MethodBuilder putJson() {
        return new MethodBuilder(Http.PUT, Http.JSON);
    }

    @Deprecated
    public static MethodBuilder putParams() {
        return new MethodBuilder(Http.PUT, Http.PARAMS);
    }

    @Deprecated
    public static MethodBuilder putForm() {
        return new MethodBuilder(Http.PUT, Http.FORM);
    }

    @Deprecated
    public static MethodBuilder delete() {
        return new MethodBuilder(Http.DELETE, Http.NO);
    }

    @Deprecated
    public static MethodBuilder deleteJson() {
        return new MethodBuilder(Http.DELETE, Http.JSON);
    }

    @Deprecated
    public static MethodBuilder deleteParams() {
        return new MethodBuilder(Http.DELETE, Http.PARAMS);
    }

    @Deprecated
    public static MethodBuilder headJson() {
        return new MethodBuilder(Http.HEAD, Http.JSON);
    }

    @Deprecated
    public static MethodBuilder deleteForm() {
        return new MethodBuilder(Http.DELETE, Http.FORM);
    }

    @Deprecated
    public static MethodBuilder headParams() {
        return new MethodBuilder(Http.HEAD, Http.PARAMS);
    }

    @Deprecated
    public static MethodBuilder headForm() {
        return new MethodBuilder(Http.HEAD, Http.FORM);
    }

    @Deprecated
    public static MethodBuilder patchJson() {
        return new MethodBuilder(Http.PATCH, Http.JSON);
    }

    @Deprecated
    public static MethodBuilder patchParams() {
        return new MethodBuilder(Http.PATCH, Http.PARAMS);
    }

    @Deprecated
    public static MethodBuilder patchForm() {
        return new MethodBuilder(Http.PATCH, Http.FORM);
    }
}
