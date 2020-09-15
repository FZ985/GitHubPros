package com.okhttplib2;

import com.okhttplib2.callback.Http;
import com.okhttplib2.config.RequestBuilder;

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
}
