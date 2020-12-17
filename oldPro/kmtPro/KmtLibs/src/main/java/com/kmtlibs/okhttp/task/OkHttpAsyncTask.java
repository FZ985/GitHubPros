package com.kmtlibs.okhttp.task;

import android.os.AsyncTask;

import com.kmtlibs.okhttp.base.OkHttpConfig;
import com.kmtlibs.okhttp.callback.Loadding;
import com.kmtlibs.okhttp.tool.OkhttpUtil;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 同步请求 返回Response
 */
public class OkHttpAsyncTask extends AsyncTask<Request, Void, Response> {
    private Loadding loadding;

    public OkHttpAsyncTask() {
    }

    public OkHttpAsyncTask(Loadding loadding) {
        this.loadding = loadding;
    }

    @Override
    protected Response doInBackground(Request... requests) {
        try {
            return OkHttpConfig.getInstance().client().newCall(requests[0]).execute();
        } catch (IOException e) {
            OkhttpUtil.log("OkHttpAsyncTask", "#####doInBackground####:" + e.getMessage());
            return null;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        OkhttpUtil.log("OkHttpAsyncTask", "#####onCancelled####:");
        if (loadding != null && loadding.isShowing()) loadding.dismiss();
        loadding = null;
    }
}
