package com.lxbuytimes.kmtapp.retrofit.def;


import android.os.AsyncTask;

import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.BaseRequestParams;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestListener;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 默认的同步请求，在AsyncTask中请求, 返回Response<ResponseBody>
 * task.executeOnExecutor(Executors.newCachedThreadPool(), req);
 */
public class DefExecuteHttpAsyncTask extends AsyncTask<Void, Void, Response<ResponseBody>> {
    private int type;
    private String domain;
    private String url;
    private BaseRequestParams reqParams;
    private Map<String, Object> params;
    private Loadding loadding;
    private ProgressRequestListener progressRequestListener;

    public DefExecuteHttpAsyncTask(int type, String domain, String url, BaseRequestParams reqParams, ProgressRequestListener progressRequestListener, Loadding loadding) {
        this.type = type;
        this.domain = domain;
        this.url = url;
        this.reqParams = reqParams;
        this.loadding = loadding;
        if (reqParams == null) {
            this.reqParams = new BaseRequestParams();
            this.params = new HashMap<>();
        } else {
            this.params = (reqParams.requestParams == null ? new HashMap<>() : reqParams.requestParams);
        }
    }

    @Override
    protected Response<ResponseBody> doInBackground(Void... voids) {
        DefaultApi api = RetrofitManager.getInstance().getRetrofit(domain).create(DefaultApi.class);
        if (type == HttpUtils.GET) {
            try {
                return (params.size() > 0 ? api.executeGet(url, params,reqParams.getHeaders()).execute() : api.executeGet(url,reqParams.getHeaders()).execute());
            } catch (Exception e) {
                HttpUtils.log("同步get请求出错:" + e.getMessage());
            }
        } else if (type == HttpUtils.POST) {
            try {
                return (params.size() > 0 ? api.executePost(url, params,reqParams.getHeaders()).execute() : api.executePost(url,reqParams.getHeaders()).execute());
            } catch (Exception e) {
                HttpUtils.log("同步post请求出错:" + e.getMessage());
            }
        } else if (type == HttpUtils.JSON) {
            try {
                String json = (reqParams != null && reqParams.requestBean != null) ? HttpUtils.reqParams(reqParams.requestBean) : "";
                return api.executePostJson(url, RequestBody.create(HttpUtils.MediaTypes.APPLICATION_JSON_TYPE, json),reqParams.getHeaders()).execute();
            } catch (Exception e) {
                HttpUtils.log("同步postJson请求出错:" + e.getMessage());
            }
        }
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (loadding != null && loadding.isShowing()) loadding.dismiss();
        loadding = null;
    }
}
