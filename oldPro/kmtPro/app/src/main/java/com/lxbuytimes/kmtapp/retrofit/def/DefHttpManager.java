package com.lxbuytimes.kmtapp.retrofit.def;


import com.kmtlibs.okhttp.callback.Loadding;
import com.lxbuytimes.kmtapp.retrofit.BaseRequestParams;
import com.lxbuytimes.kmtapp.retrofit.def.download.Download;
import com.lxbuytimes.kmtapp.retrofit.def.download.DownloadInfo;
import com.lxbuytimes.kmtapp.retrofit.def.download.DownloadListener;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestListener;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Create by JFZ
 * date: 2019-10-21 15:16
 * 默认的retrofit网络请求管理，只针对get、post、postJson、postForm做处理
 **/
public class DefHttpManager {
    private Loadding loadding;
    private int type = -1;

    private DefHttpManager(int type) {
        this.type = type;
    }

    public static DefHttpManager get() {
        return new DefHttpManager(HttpUtils.GET);
    }

    public static DefHttpManager post() {
        return new DefHttpManager(HttpUtils.POST);
    }

    public static DefHttpManager postJson() {
        return new DefHttpManager(HttpUtils.JSON);
    }

    public static DefHttpManager postForm() {
        return new DefHttpManager(HttpUtils.FORM);
    }

    public static DefHttpManager download() {
        return new DefHttpManager(-1);
    }

    public static DefHttpManager upload() {
        return new DefHttpManager(HttpUtils.FORM);
    }

    public DefHttpManager loadding(Loadding loadding) {
        this.loadding = loadding;
        return this;
    }

    public Response<ResponseBody> execute(String url) throws Exception {
        return execute(url, null);
    }

    public Response<ResponseBody> execute(String url, BaseRequestParams requestParams) throws Exception {
        if (type == -1) throw new Exception("请求方式不明");
        if (type == HttpUtils.FORM) throw new Exception("不支持的请求方式");
        showLoadding();
        return DefExecuteHttp.execute(type, url, requestParams, null, loadding);
    }

    public Object executeObj(String url) throws Exception {
        return executeObj(url, null);
    }

    public Object executeObj(String url, BaseRequestParams requestParams) throws Exception {
        if (type == -1) throw new Exception("请求方式不明");
        if (type == HttpUtils.FORM) throw new Exception("不支持的请求方式");
        showLoadding();
        return DefExecuteHttp.executeObj(type, url, requestParams, null, loadding);
    }

    public void enqueue(String url, DefaultCallback<?> callback) {
        enqueue(url, callback, null);
    }

    public void enqueue(String url, DefaultCallback<?> callback, BaseRequestParams requestParams) {
        DefEnqueueHttp.enqueue(type, url, requestParams, callback, null, loadding);
    }

    //不支持断点续传
    public Download download(String url, String fileDir, String fileName, DownloadListener listener) {
        return download(new DownloadInfo(url, fileDir, fileName, false), listener);
    }

    //可支持断点续传,由用户控制
    public Download download(String url, String fileDir, String fileName, boolean isBreakPoint, DownloadListener listener) {
        return download(new DownloadInfo(url, fileDir, fileName, isBreakPoint), listener);
    }

    //下载
    public Download download(DownloadInfo info, DownloadListener listener) {
        if (type == HttpUtils.FORM) return null;
        Download download = Download.obtain();
        download.download(info, listener);
        return download;
    }

    //异步上传文件
    public void enqueueUpload(String url, DefaultCallback<?> callback, BaseRequestParams requestParams, ProgressRequestListener listener) {
        DefEnqueueHttp.enqueue(type, url, requestParams, callback, listener, loadding);
    }

    private void showLoadding() {
        if (this.loadding != null && !loadding.isShowing()) {
            loadding.show();
        }
    }
}
