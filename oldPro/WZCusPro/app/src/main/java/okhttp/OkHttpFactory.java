package okhttp;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp.callback.OkRequestCallback;
import okhttp.file.download.DownLoadBean;
import okhttp.file.download.DownLoadManager;
import okhttp.file.download.DownLoadStatusCallback;
import okhttp.file.upload.UIProgressRequestListener;
import okhttp.loadding.HttpLoad;
import okhttp.utils.OkUtil;


/**
 * Created by JFZ .
 * on 2018/1/16.
 */

public class OkHttpFactory {

    public static final ExecutorService THREAD_POOL = Executors
            .newFixedThreadPool(5);

    public static final String KEY = "data";

    // TODO: 2018/1/16 异步get请求 带参数 
    public static <T> void get(String url, OkRequestCallback<?> callback, HashMap<String, Object> map, HttpLoad loadding) {
        exec(0, url, callback, map, null, null, null, null, loadding);
    }

    // TODO: 2018/1/16 异步get请求 不带传参数 
    public static <T> void get(String url, OkRequestCallback<?> callback, HttpLoad loadding) {
        exec(0, url, callback, null, null, null, null, null, loadding);
    }

    // TODO: 2018/1/16 异步post json请求 
    public static <T> void postJson(String url, OkRequestCallback<?> callback, Object obj, HttpLoad loadding) {
        exec(1, url, callback, null, obj, null, null, null, loadding);
    }

    // TODO: 2018/1/16 异步post 参数请求 
    public static <T> void postParams(String url, OkRequestCallback<?> callback, HashMap<String, Object> map, HttpLoad loadding) {
        exec(2, url, callback, map, null, null, null, null, loadding);
    }

    // TODO: 2018/1/16 异步表单请求 带参数 
    public static <T> void postForm(String url, OkRequestCallback<?> callback, HashMap<String, Object> map, HttpLoad loadding) {
        exec(3, url, callback, map, null, null, null, null, loadding);
    }

    // TODO: 2018/1/16 单文件上传 带参数
    public static <T> void upload(String url, OkRequestCallback<?> callback, File file, String fileKey, HashMap<String, Object> map, HttpLoad loadding, UIProgressRequestListener progressListener) {
        exec(4, url, callback, map, null, new File[]{file}, new String[]{fileKey}, progressListener, loadding);
    }

    // TODO: 2018/1/16 单文件上传 不带参数
    public static <T> void upload(String url, OkRequestCallback<?> callback, File file, String fileKey, HttpLoad loadding, UIProgressRequestListener progressListener) {
        exec(4, url, callback, null, null, new File[]{file}, new String[]{fileKey}, progressListener, loadding);
    }

    // TODO: 2018/1/16 多文件上传 带参数 
    public static <T> void upload(String url, OkRequestCallback<?> callback, File[] file, String[] fileKey, HashMap<String, Object> map, HttpLoad loadding, UIProgressRequestListener progressListener) {
        exec(4, url, callback, map, null, file, fileKey, progressListener, loadding);
    }

    // TODO: 2018/1/16 多文件上传 不带参数
    public static <T> void upload(String url, OkRequestCallback<?> callback, File[] file, String[] fileKey, HttpLoad loadding, UIProgressRequestListener progressListener) {
        exec(4, url, callback, null, null, file, fileKey, progressListener, loadding);
    }

    // TODO: 2018/1/18 文件下载
    public static void download(DownLoadBean bean, DownLoadStatusCallback<DownLoadBean> callback) {
        DownLoadManager.getInstance().downLoad(bean, callback);
    }

    // TODO: 2018/1/18 文件下载
    public static void download(String downUrl, String fileName, String downPath, DownLoadStatusCallback<DownLoadBean> callback) {
        DownLoadManager.getInstance().downLoad(new DownLoadBean(fileName, downPath, downUrl, 0, 0), callback);
    }

    // TODO: 2018/1/16 post 表单 key: data ,value: json
    public static <T> void postFormJson(String url, OkRequestCallback<?> callback, Object obj, HttpLoad loadding) {
        exec(5, url, callback, null, obj, null, null, null, loadding);
    }


    // TODO: 2018/1/16 执行网络请求  0:get请求; 1:post json 请求；2:post 带参数请求; 3:post 表单请求，但参数; 4 :文件上传; 5 :表单 key: data ,value: json
    private static <T> void exec(int requestType, String url, OkRequestCallback<?> callback, HashMap<String, Object> map, Object src, File[] file, String[] fileKey, UIProgressRequestListener progress, HttpLoad loadding) {

        if (loadding != null && !loadding.isLoaddingShowing())
            loadding.showLoadding();
        switch (requestType) {
            case 0:
                OkHttpManager.getAsync(url, callback, map, loadding);
                break;
            case 1:
                String json = OkUtil.reqParams(src);
                OkHttpManager.postAsyncToJson(url, callback, json, loadding);
                break;
            case 2:
                OkHttpManager.postAsyncToParams(url, callback, map, loadding);
                break;
            case 3:
                try {
                    OkHttpManager.postForm(url, callback, map, loadding);
                } catch (IOException e) {
                    sendErrorCall(callback, e, loadding);
                }
                break;
            case 4:
                try {
                    OkHttpManager.postUpFile(url, callback, file, fileKey, map, loadding, progress);
                } catch (IOException e) {
                    sendErrorCall(callback, e, loadding);
                }
                break;
            case 5:
                String jsonData = OkUtil.reqParams(src);
                try {
                    OkHttpManager.postForm(url, callback, new Params[]{new Params(KEY, jsonData)}, loadding);
                } catch (IOException e) {
                    sendErrorCall(callback, e, loadding);
                }
                break;
        }

    }

    private static void sendErrorCall(OkRequestCallback<?> callback, Exception e, HttpLoad loadding) {
        if (loadding != null && loadding.isLoaddingShowing()) {
            loadding.dismissLoadding();
        }
        if (callback != null) {
            callback.onError(e);
        }
    }
}
