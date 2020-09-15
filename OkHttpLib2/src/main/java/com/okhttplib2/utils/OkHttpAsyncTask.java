package com.okhttplib2.utils;

import android.os.AsyncTask;

import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.config.Loadding;
import com.okhttplib2.config.OkHttpConfig;

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
            return OkHttpFactory.getInstance().client().newCall(requests[0]).execute();
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
