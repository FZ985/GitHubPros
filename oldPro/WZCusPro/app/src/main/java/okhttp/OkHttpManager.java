package okhttp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp.callback.OkRequestCallback;
import okhttp.certificate.OkTrustAllCerts;
import okhttp.certificate.OkTrustAllHostnameVerifier;
import okhttp.file.ProgressHelper;
import okhttp.file.upload.UIProgressRequestListener;
import okhttp.loadding.HttpLoad;
import okhttp.utils.OkUtil;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by JFZ .
 * on 2018/1/15.
 */

public class OkHttpManager {
    static final String TAG = OkHttpManager.class.getSimpleName();
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static String DEF_HTTP = "https://www.baidu.com";
    private static final String PREFIX_HTTP = "http://";
    private static final String PREFIX_HTTPS = "https://";
    private static final long DEF_TIMEOUT = 5;
    private static final long cacheSize = 20 * 1024 * 1024;//缓存大小为20M
    private static OkHttpManager INSTANCE;
    private OkHttpClient mOkHttpClient;
    private CacheControl cacheControl;
    private Handler mDelivery;
    private boolean isUseHttpPrefix = false;
    private Gson mGson;

    public static OkHttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (OkHttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpManager();
                }
            }
        }
        return INSTANCE;
    }
//        params.setHeader(Constants.APP_URL_HEADER_KEY,Constants.APP_URL_HEDTER);

    private OkHttpManager() {
        OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEF_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory())// 信任所有证书~
//                .sslSocketFactory(createSSLSocketFactory(), new OkTrustAllCerts()) // 信任所有证书~
                .hostnameVerifier(new OkTrustAllHostnameVerifier())/* .cache(cache) */
//                .cache(new Cache(new File(context.getExternalCacheDir(), "okhttpcache"), cacheSize))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addHeader("Accept-Encoding", "gzip, deflate")
                                .addHeader("Connection", "keep-alive")
                                .build();
                        return chain.proceed(request);
                    }
                });

        mOkHttpClient = mBuilder.build();
        mDelivery = new Handler(Looper.getMainLooper());
        mGson = new Gson();
        //设置缓存时间为60秒
        cacheControl = new CacheControl.Builder()
                .maxAge(60, TimeUnit.SECONDS)
                .build();
    }

    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new OkTrustAllCerts()},
                    new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }
        return ssfFactory;
    }

    // TODO: 2018/1/15 异步 get 请求
    private void _getAsync(String url, OkRequestCallback<?> callback, HttpLoad load, Params... params) {
        if (!OkUtil.checkUrl(url)) {
            OkUtil.log("***get***请求url:" + url);
            Request request = null;
            if (params == null) {
                request = new Request.Builder().url(url).cacheControl(cacheControl).build();
            } else {
                Request.Builder builder = new Request.Builder();
                url = url + OkUtil.splicingParams(params);
                OkUtil.log(">>>异get请求地址：" + url);
                request = builder.url(chunkUrl(url).trim()).cacheControl(cacheControl).build();
            }
            requestCall(request, callback, load);
        } else {
            sendFailureCallBack(callback, new NullPointerException("The request url is null ! please check !"));
        }
    }

    // TODO: 2018/1/16 异步 post请求 直接传json
    private void _postAsyncJson(String url, OkRequestCallback<?> callback, String json, HttpLoad load) {
        if (!OkUtil.checkUrl(url)) {
            OkUtil.log(">>>异>post请求地址：" + url);
            OkUtil.log(">>>异>post请求json：" + json);
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(chunkUrl(url).trim()).cacheControl(cacheControl).post(body).build();
            requestCall(request, callback, load);
        } else {
            sendFailureCallBack(callback, new NullPointerException("The request url is null ! Please check!"));
        }
    }

    // TODO: 2018/1/16  异步 post请求
    private void _postAsyncParams(String url, OkRequestCallback<?> callback, Params[] params, HttpLoad load) {
        if (!OkUtil.checkUrl(url)) {
            OkUtil.log(">>>异>post请求地址：" + url);
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (Params param : params) {
                    builder.add(param.key, param.value);
                }
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder()
                    .url(chunkUrl(url).trim())
                    .cacheControl(cacheControl)
                    .post(body)
                    .build();
            requestCall(request, callback, load);
        } else {
            sendFailureCallBack(callback, new NullPointerException("The request url is null ! Please check!"));
        }
    }

    // TODO: 2018/1/16 post form 表单请求 含文件上传 含携带参数
    private void _postForm(String url, OkRequestCallback<?> callback, File[] file,
                           String[] fileKey, HttpLoad loading, UIProgressRequestListener progress, Params... params) throws IOException {
        if (!OkUtil.checkUrl(url)) {
            OkUtil.log(">>>异>post请求地址：" + url);
            Request request = buildMultipartFormRequest(chunkUrl(url).trim(), file, fileKey, progress, params);
            requestCall(request, callback, loading);
        } else {
            sendFailureCallBack(callback, new NullPointerException("The request url is null ! Please check!"));
        }
    }

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, UIProgressRequestListener uiProgressRequestListener, Params[] params) {
        params = OkUtil.validateParam(params);
        MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x");
        builder.setType(MultipartBody.FORM);

        for (Params param : params) {
            OkUtil.log("##请求数据##" + param.value);
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; name=\""
                            + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(
                        MediaType.parse(OkUtil.guessMimeType(fileName)), file);
                // TODO 根据文件名设置contentType
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\""
                                + fileKeys[i] + "\"; filename=\"" + fileName
                                + "\""), fileBody);
            }
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder().url(chunkUrl(url).trim()).post(uiProgressRequestListener == null ? requestBody : ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener)).build();
    }


    // TODO: 2018/1/16 统一异步的请求
    private void requestCall(final Request request, final OkRequestCallback callback, final HttpLoad load) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (load != null && load.isLoaddingShowing()) {
                    load.dismissLoadding();
                }
                sendFailureCallBack(callback, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (load != null && load.isLoaddingShowing()) {
                    load.dismissLoadding();
                }
                OkUtil.log(response.code() + "<<<response.isSuccessful():" + response.isSuccessful());
                String info = "";
                try {
                    if (response.code() != 504) {
                        // 资源已经缓存了，可以直接使用
                        if (response.isSuccessful()) {
                            String string = response.body().string();
//                            Log.i(TAG, "testCache: response cache :" + response.cacheResponse());
//                            Log.i(TAG, "testCache: response network :" + response.networkResponse());

                            OkUtil.log(request.url().toString() + "<解析前请求数据:\n" + string);
//                            String finalStr = "";
                            info = string;
                            if (callback != null && callback.mType != null) {
                                if (callback.mType == String.class) {
                                    if (TextUtils.isEmpty(info)) {
//                                        if(isEncrypt){
//                                            Exception e = new Exception("jfz_Exception:AesEncryptionUtil.decrypt(string) return null!");
//                                            sendFailureCallBack(callback, call, e);
//                                        }else{
                                        Exception e = new Exception("okhttp_Exception:the response return null!");
                                        sendFailureCallBack(callback, e);
//                                        }
                                    } else {
                                        sendSuccessCallBack(info, callback);
                                    }
                                } else {
//                                OkUtil.log("++++++callback.mType+++++" + callback.mType );
                                    Object o = mGson.fromJson(info,
                                            callback.mType);
                                    if (o == null) {
                                        Exception e = new Exception("okhttp_Exception:mGson.fromJson(finalStr,callback.mType) return null!");
                                        sendFailureCallBack(callback, e);
                                    } else {
                                        sendSuccessCallBack(o, callback);
                                    }
                                }
                            } else {
                                Exception e = new Exception("okhttp_Exception:callback.mType is Null");
                                sendFailureCallBack(callback, e);
                            }

                        } else {
                            Exception e = new Exception(
                                    response == null ? "okhttp_Exception:response is null~"
                                            : "okhttp_Exception:" + response.toString() + "," + response.body().string());
                            sendFailureCallBack(callback, e);
                        }
                    } else {
                        // 资源没有缓存，或者是缓存不符合条件了。
                        sendFailureCallBack(callback, new Exception("504,资源没有缓存，或者是缓存不符合条件了"));
                    }

                } catch (JsonSyntaxException e) {
                    sendFailureCallBack(callback, new Exception("json 解析错误:" + e.getMessage()));
                } catch (IOException e) {
                    sendFailureCallBack(callback, e);
                }
            }
        });
    }

    /**
     * 校验url,无前缀添加前缀
     */
    private String chunkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return DEF_HTTP;
        }
        if (url.startsWith(PREFIX_HTTP)) {

        } else if (url.startsWith(PREFIX_HTTPS)) {

        } else {
            if (isUseHttpPrefix) {
                url = PREFIX_HTTP + url;
            } else {
                url = PREFIX_HTTPS + url;
            }
        }
        OkUtil.log("#http#" + url);
        return url;
    }

    /**
     * 成功回调
     *
     * @param obj
     * @param callBack
     */
    private void sendSuccessCallBack(final Object obj, final OkRequestCallback callBack) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    if (obj != null) {
                        callBack.onResponse(obj);
                    }
                }
            }
        });
    }

    /**
     * 失败回调
     *
     * @param callBack
     * @param e
     */
    private void sendFailureCallBack(final OkRequestCallback callBack, final Exception e) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onError(e);
                }
            }
        });
    }

    public Handler obtainHandler() {
        return mDelivery;
    }

    public OkHttpClient client() {
        return mOkHttpClient;
    }

    //-----------------------------------------------------------------------------------------
    // TODO: 2018/1/16 外部调用get请求
    public static <T> void getAsync(String url, OkRequestCallback<?> callback, HashMap<String, Object> params, HttpLoad load) {
        getInstance()._getAsync(url, callback, load, OkUtil.appendParams(params));
    }

    // TODO: 2018/1/16  外部调用post json请求
    public static <T> void postAsyncToJson(String url, OkRequestCallback<?> callback, String json, HttpLoad load) {
        getInstance()._postAsyncJson(url, callback, json, load);
    }

    // TODO: 2018/1/16 外部调用post 请求
    public static <T> void postAsyncToParams(String url, OkRequestCallback<?> callback, HashMap<String, Object> params, HttpLoad load) {
        getInstance()._postAsyncParams(url, callback, OkUtil.appendParams(params), load);
    }

    // TODO: 2018/1/16 外部调用post form 请求
    public static <T> void postForm(String url, OkRequestCallback<?> callback, HashMap<String, Object> params, HttpLoad load) throws IOException {
        getInstance()._postForm(url, callback, null, null, load, null, OkUtil.appendParams(params));
    }

    // TODO: 2018/1/16 外部调用post form 请求
    public static <T> void postForm(String url, OkRequestCallback<?> callback, Params[] params, HttpLoad load) throws IOException {
        getInstance()._postForm(url, callback, null, null, load, null, params);
    }

    // TODO: 2018/1/16 外部调用post form 单文件上传请求  并携带参数
    public static <T> void postUpFile(String url, OkRequestCallback<?> callback, File file, String fileKey, HashMap<String, Object> params, HttpLoad load, UIProgressRequestListener progress) throws IOException {
        getInstance()._postForm(url, callback, new File[]{file}, new String[]{fileKey}, load, progress, OkUtil.appendParams(params));
    }

    // TODO: 2018/1/16 外部调用post form 单文件上传请求  并携带参数
    public static <T> void postUpFile(String url, OkRequestCallback<?> callback, File file, String fileKey, Params[] params, HttpLoad load, UIProgressRequestListener progress) throws IOException {
        getInstance()._postForm(url, callback, new File[]{file}, new String[]{fileKey}, load, progress, params);
    }

    // TODO: 2018/1/16 外部调用post form 多文件上传请求  并携带参数
    public static <T> void postUpFile(String url, OkRequestCallback<?> callback, File[] file, String[] fileKey, HashMap<String, Object> params, HttpLoad load, UIProgressRequestListener progress) throws IOException {
        getInstance()._postForm(url, callback, file, fileKey, load, progress, OkUtil.appendParams(params));
    }

    // TODO: 2018/1/16 外部调用post form 多文件上传请求  并携带参数
    public static <T> void postUpFile(String url, OkRequestCallback<?> callback, File[] file, String[] fileKey, Params[] params, HttpLoad load, UIProgressRequestListener progress) throws IOException {
        getInstance()._postForm(url, callback, file, fileKey, load, progress, params);
    }

}
