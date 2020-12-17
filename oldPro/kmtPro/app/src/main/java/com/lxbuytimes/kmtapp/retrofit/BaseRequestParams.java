package com.lxbuytimes.kmtapp.retrofit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Create by JFZ
 * date: 2019-10-21 16:42
 **/
public class BaseRequestParams {
    public Class<?> cls;//请求成功时接收的bean对象
    public Map<String, Object> requestParams;//请求参数集合
    public Object requestBean;//请求参数对象
    public Map<String, File> files;//上传的文件集
    private Map<String, String> headers;

    public BaseRequestParams() {
    }

    public BaseRequestParams(Class<?> cls) {
        this.cls = cls;
    }

    public BaseRequestParams(Map<String, Object> requestParams, Class<?> cls) {
        this.cls = cls;
        this.requestParams = requestParams;
        this.requestBean = requestParams;
    }

    public BaseRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
        this.requestBean = requestParams;
    }

    public BaseRequestParams(Map<String, Object> requestParams, Map<String, File> files) {
        this.requestParams = requestParams;
        this.files = files;
    }

    public BaseRequestParams(Object requestBean) {
        this.requestBean = requestBean;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new HashMap<>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
