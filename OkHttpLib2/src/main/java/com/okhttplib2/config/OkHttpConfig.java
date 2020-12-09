package com.okhttplib2.config;


import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.Http;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by JFZ
 * date: 2020-09-15 14:00
 * 全局配置文件
 **/
public class OkHttpConfig {

    private static OkHttpConfig config;
    private boolean isProxy = true;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mBuilder;
    private CookieJar cookieJar;
    private List<Interceptor> interceptors;
    private List<Interceptor> netInterceptors;
    private String cachePath;
    private CacheControl cacheControl;
    private Http.RequestInterceptor requestInterceptor;
    private Http.ResponseInterceptor responseInterceptor;
    private ConcurrentHashMap<Object,Object> tags;
    public static OkHttpConfig getInstance() {
        if (config == null) {
            synchronized (OkHttpConfig.class) {
                if (config == null) {
                    config = new OkHttpConfig();
                }
            }
        }
        return config;
    }

    private OkHttpConfig() {
        check();
    }

    public boolean isProxy() {
        return isProxy;
    }

    public OkHttpConfig setProxy(boolean proxy) {
        isProxy = proxy;
        return this;
    }

    public OkHttpClient getHttpClient() {
        return mOkHttpClient;
    }

    public OkHttpConfig setHttpClient(OkHttpClient mOkHttpClient) {
        this.mOkHttpClient = mOkHttpClient;
        return this;
    }

    public OkHttpClient.Builder getBuilder() {
        return mBuilder;
    }

    public OkHttpConfig setBuilder(OkHttpClient.Builder mBuilder) {
        this.mBuilder = mBuilder;
        return this;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public OkHttpConfig setCookieJar(CookieJar cookieJar) {
        this.cookieJar = cookieJar;
        return this;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public OkHttpConfig addInterceptor(Interceptor interceptor) {
        check();
        if (interceptors != null) {
            this.interceptors.add(interceptor);
        }
        return this;
    }

    public OkHttpConfig addInterceptors(List<Interceptor> interceptors) {
        check();
        if (interceptors != null) {
            this.interceptors.addAll(interceptors);
        }
        return this;
    }

    public List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    public OkHttpConfig addNetInterceptor(Interceptor netInterceptor) {
        check();
        if (netInterceptors != null) {
            this.netInterceptors.add(netInterceptor);
        }
        return this;
    }

    public OkHttpConfig addNetInterceptors(List<Interceptor> netInterceptors) {
        check();
        if (netInterceptors != null) {
            this.netInterceptors.addAll(netInterceptors);
        }
        return this;
    }

    private void check() {
        if (this.netInterceptors == null) {
            this.netInterceptors = new ArrayList<>();
        }
        if (this.interceptors == null) {
            this.interceptors = new ArrayList<>();
        }
    }

    public String getCachePath() {
        return cachePath;
    }

    public OkHttpConfig setCachePath(String cachePath) {
        this.cachePath = cachePath;
        return this;
    }

    public OkHttpConfig setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public CacheControl cacheControl() {
        if (cacheControl == null) {
            cacheControl = new CacheControl.Builder()
                    .maxAge(10, TimeUnit.HOURS)
                    .noStore()
                    .build();
        }
        return cacheControl;
    }

    public Http.RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    public OkHttpConfig setRequestInterceptor(Http.RequestInterceptor requestInterceptor) {
        this.requestInterceptor = requestInterceptor;
        return this;
    }

    public Http.ResponseInterceptor getResponseInterceptor() {
        return responseInterceptor;
    }

    public OkHttpConfig setResponseInterceptor(Http.ResponseInterceptor responseInterceptor) {
        this.responseInterceptor = responseInterceptor;
        return this;
    }

    public ConcurrentHashMap<Object, Object> getTags() {
        if (tags == null){
            tags = new ConcurrentHashMap<>();
        }
        return tags;
    }

    public void init() {
        OkHttpFactory.getInstance().newBuild();
    }
}
