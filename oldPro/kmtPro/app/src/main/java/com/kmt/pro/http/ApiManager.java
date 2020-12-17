package com.kmt.pro.http;


import com.kmt.pro.helper.KConstant;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;

/**
 * Create by JFZ
 * date: 2019-10-17 14:29
 * 管理多个interface接口
 **/
public class ApiManager {

    private static ApiManager apiManager;

    private ApiManager() {
    }

    public static ApiManager get() {
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager();
                }
            }
        }
        return apiManager;
    }

    public KmtApi kmtApi() {
        return RetrofitManager.getInstance().getRetrofit(KConstant.base_url).create(KmtApi.class);
    }

    public LoginApi loginApi() {
        return RetrofitManager.getInstance().getRetrofit(KConstant.base_url).create(LoginApi.class);
    }
}
