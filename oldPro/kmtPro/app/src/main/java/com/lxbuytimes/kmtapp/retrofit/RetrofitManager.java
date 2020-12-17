package com.lxbuytimes.kmtapp.retrofit;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.http.interceptor.CommonParamsInterceptor;
import com.kmt.pro.http.interceptor.HttpLoggingInterceptor;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by JFZ
 * date: 2019-10-16 13:56
 **/
public class RetrofitManager {
    public static ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(BaseApp.getInstance()));
    private static final String defaultDomain = "https://www.baidu.com";
    private static final String defaultBaseUrl = defaultDomain + "/";
    private static RetrofitManager INSTANCE;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mBuilder;
    private Handler mDelivery;
    private static final long cacheSize = 50 * 1024 * 1024;//缓存大小为50M
    public static HashMap<String, String> baseUrlCache = new HashMap<>();
    private HashMap<String, Retrofit> retrofitCache = new HashMap<>();
    public static Gson GSON = new Gson();

    public Handler obtainHandler() {
        return mDelivery;
    }

    public OkHttpClient client() {
        return mOkHttpClient;
    }

    public Retrofit getRetrofit(String domainUrl) {
        if (retrofitCache.containsKey(domainUrl))
            return retrofitCache.get(domainUrl);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(HttpLoggingInterceptor.Level.BODY);
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .cache(new Cache(BaseApp.getInstance().getApplicationContext().getCacheDir(), cacheSize))
                .sslSocketFactory(createSSLSocketFactory())// 信任所有证书~
//                .sslSocketFactory(createSSLSocketFactory(), new OkTrustAllCerts()) // 信任所有证书~
                .hostnameVerifier(new OkTrustAllHostnameVerifier())
                .addInterceptor(new CommonParamsInterceptor())
                .cookieJar(cookieJar)
                .addInterceptor(logging);
        mBuilder.proxy(Proxy.NO_PROXY);
        mOkHttpClient = mBuilder.build();
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(domainUrl)
                .client(mOkHttpClient)
                //添加Rxjava支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                //添加GSON解析:返回数据转换成GSON类型
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitCache.put(domainUrl, mRetrofit);
        return mRetrofit;
    }

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitManager();
                }
            }
        }
        return INSTANCE;
    }

    private RetrofitManager() {
        HttpUtils.log(" RetrofitManager init..");
        getRetrofit(defaultBaseUrl);
        mDelivery = new Handler(Looper.getMainLooper());
    }

    @SuppressLint("TrulyRandom")
    private SSLSocketFactory createSSLSocketFactory() {
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

    private class OkTrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private class OkTrustAllHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}
