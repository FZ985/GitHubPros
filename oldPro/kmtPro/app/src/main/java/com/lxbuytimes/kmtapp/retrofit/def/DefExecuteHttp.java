package com.lxbuytimes.kmtapp.retrofit.def;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.BaseRequestParams;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestListener;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Create by JFZ
 * date: 2019-10-17 16:03
 * 默认的同步请求管理
 **/
public class DefExecuteHttp {

    public static Response<ResponseBody> execute(int type, String url, BaseRequestParams params, ProgressRequestListener progressRequestListener, Loadding loadding) throws Exception {
        if (TextUtils.isEmpty(url)) {
            dismissLoadding(loadding);
            throw new NullPointerException("the url is null!");
        }
        if (!URLUtil.isNetworkUrl(url)) {
            dismissLoadding(loadding);
            throw new Exception("the url is not http:// or https://");
        }
        Uri uri = Uri.parse(url);
        String domain = uri.getScheme() + "://" + uri.getAuthority() + "/";
        String path = TextUtils.isEmpty(uri.getPath()) ? "/" : uri.getPath();
        DefExecuteHttpAsyncTask task = new DefExecuteHttpAsyncTask(type, domain, path, params, progressRequestListener, loadding);
        task.executeOnExecutor(Executors.newCachedThreadPool());
        Response<ResponseBody> response = task.get();
        if (response != null) {
            task.cancel(true);
        }
        return response;
    }

    public static Object executeObj(int type, String url, BaseRequestParams params, ProgressRequestListener progressRequestListener, Loadding loadding) throws Exception {
        Response<ResponseBody> response = execute(type, url, params, progressRequestListener, loadding);
        if (response == null) return null;
        String finalObj = response.body().string();
        boolean success = response.isSuccessful();
        int code = response.code();
        HttpUtils.log("##success:" + success + ",code:" + code + ",解析前数据:" + finalObj);
        if (success && !TextUtils.isEmpty(finalObj)) {
            if (params != null && params.cls != null && params.cls != String.class) {
                return RetrofitManager.GSON.fromJson(finalObj, params.cls);
            } else
                return finalObj;
        }
        return null;
    }

    private static void dismissLoadding(Loadding loadding) {
        if (loadding != null && loadding.isShowing()) loadding.dismiss();
        loadding = null;
    }
}
