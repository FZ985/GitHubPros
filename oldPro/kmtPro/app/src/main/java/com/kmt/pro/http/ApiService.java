package com.kmt.pro.http;


/**
 * Create by JFZ
 * date: 2020-02-24 18:36
 **/
public class ApiService {

    private static ApiService api;
    private ApiService() {
    }

    public static ApiService get() {
        if (api == null) {
            synchronized (ApiService.class) {
                if (api == null) {
                    api = new ApiService();
                }
            }
        }
        return api;
    }




//    //post 加密请求，加密返回
//    public void postJsonEncrypt(String url, final OkRequestCallback<?> callback, Object request, Loadding loadding) {
//        HttpImpl.postJson()
//                .url(url)
//                .load(loadding)
//                .request(new OkHttpBaseRequest(AesApi.encryptObj(request)))
//                .build()
//                .enqueue(getEncryptCallback(url, callback));
//    }
//
//    public void postJson(String url, OkRequestCallback<?> callback, Object request, Loadding loadding) {
//        postJson(true, url, callback, request, loadding);
//    }
//
//    public void postJson(boolean encrypt, String url, OkRequestCallback<?> callback, Object request, Loadding loadding) {
//        if (encrypt) {
//            Logger.e("加密前:" + new Gson().toJson(request));
//            postJsonEncrypt(url, callback, request, loadding);
//        } else {
//            HttpImpl.postJson()
//                    .url(url)
//                    .load(loadding)
//                    .request(new OkHttpBaseRequest(request))
//                    .build()
//                    .enqueue(callback);
//        }
//    }
//
//    public void postParams(String url, OkRequestCallback<?> callback, HashMap<String, Object> params, Loadding loadding) {
//        HttpImpl.postParams()
//                .url(url)
//                .request(new OkHttpBaseRequest(params))
//                .load(loadding)
//                .build()
//                .enqueue(callback);
//    }
//
//    public void postParamsEncrypt(String url, OkRequestCallback<?> callback, HashMap<String, Object> params, Loadding loadding) {
//        HttpImpl.postParams()
//                .url(url)
//                .request(new OkHttpBaseRequest(params))
//                .load(loadding)
//                .build()
//                .enqueue(getEncryptCallback(url, callback));
//    }
//
//    private OkRequestCallback<?> getEncryptCallback(final String url, final OkRequestCallback<?> callback) {
//        return new OkRequestCallback<String>() {
//            @Override
//            public void onResponse(String data) {
//                try {
//                    if (callback != null && callback.mType != null && !TextUtils.isEmpty(data)) {
//                        String finalStr = AesApi.decrypt(data);
//                        Logger.e(url + "<<解密数据:" + finalStr);
//                        if (callback.mType == String.class) {
//                            sendSuccessCall(callback, finalStr);
//                        } else {
//                            Object o = OkhttpUtil.GSON.fromJson(finalStr, callback.mType);
//                            if (o == null) {
//                                sendFailedCall(callback, -1, new Exception("Httppi:mGson.fromJson(finalStr,callback.mType) return null!"));
//                            } else sendSuccessCall(callback, o);
//                        }
//                    } else
//                        sendFailedCall(callback, -1, new Exception(":回调 or 返回数据 or 解析类型为空"));
//                } catch (JsonSyntaxException e) {
//                    Logger.e("json解析err:" + e.getMessage());
//                    sendFailedCall(callback, -1, e);
//                }
//            }
//
//            @Override
//            public void onError(int code, Exception e) {
//                if (callback != null) callback.onError(code, e);
//            }
//        };
//    }
//
//    private void sendFailedCall(final OkRequestCallback callback, final int code, final Exception e) {
//        OkHttpConf.getInstance().obtainHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (callback != null) callback.onError(code, e);
//            }
//        });
//    }
//
//    private void sendSuccessCall(final OkRequestCallback callback, final Object obj) {
//        OkHttpConf.getInstance().obtainHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                if (callback != null) callback.onResponse(obj);
//            }
//        });
//    }
//
//    public void getAsync(String url, OkRequestCallback<?> callback) {
//        HttpImpl.get()
//                .url(url)
//                .build()
//                .enqueue(callback);
//    }
//    public void postAsync(String url, OkRequestCallback<?> callback) {
//        HttpImpl.postParams()
//                .url(url)
//                .build()
//                .enqueue(callback);
//    }
}
