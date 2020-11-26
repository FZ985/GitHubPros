package com.okhttplib2.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-09-15 14:03
 * 请求体
 **/
public class JRequest {

    private List<JRequestHeaders> headers = null;//请求头
    public Class<?> cls;//请求成功时接收的bean对象
    public HashMap<String, Object> requestParams;//请求参数集合
    public Object requestBean;//请求参数对象
    public Object tag;//请求标识

    public JRequest() {
    }

    public JRequest(Object obj) {
        this.requestBean = obj;
    }

    public JRequest(HashMap<String, Object> requestParams) {
        this.requestParams = requestParams;
        this.requestBean = requestParams;
    }

    public JRequest(Object obj, List<JRequestHeaders> headers) {
        this.requestBean = obj;
        this.headers = headers;
    }

    public JRequest(Class<?> cls, Object requestBean) {
        this.cls = cls;
        this.requestBean = requestBean;
    }

    public JRequest(Class<?> cls, HashMap<String, Object> requestParams) {
        this.cls = cls;
        this.requestParams = requestParams;
        this.requestBean = requestParams;
    }

    public JRequest(Class<?> cls) {
        this.cls = cls;
    }


    public void addHeader(String key, String value) {
        if (headers == null) headers = new ArrayList<>();
        headers.add(new JRequestHeaders(key, value));
    }

    public void addHeaders(List<JRequestHeaders> hs) {
        if (headers == null) headers = new ArrayList<>();
        headers.addAll(hs);
    }

    public List<JRequestHeaders> getHeaders() {
        if (headers == null) headers = new ArrayList<>();
        return headers;
    }

    public static class JRequestHeaders {
        public String key, value;

        public JRequestHeaders(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
