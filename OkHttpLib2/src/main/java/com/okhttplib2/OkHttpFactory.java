package com.okhttplib2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.okhttplib2.callback.Http;
import com.okhttplib2.config.OkHttpConfig;

import java.io.File;
import java.net.Proxy;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Create by JFZ
 * date: 2020-09-15 14:44
 **/
public class OkHttpFactory implements Http.Config {
    private static Http.Config factory;
    private static final long cacheSize = 50 * 1024 * 1024;//缓存大小为50M
    private Handler mDelivery;
    public static Http.Config getInstance() {
        if (factory == null) {
            synchronized (OkHttpFactory.class) {
                if (factory == null) {
                    factory = new OkHttpFactory();
                }
            }
        }
        return factory;
    }

    private OkHttpFactory() {
        Log.e("OkHttpFactory", " OkHttpFactory init..");
        newBuild();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    @Override
    public void newBuild() {
        if (OkHttpConfig.getInstance().getHttpClient() == null || OkHttpConfig.getInstance().getBuilder() == null) {
            OkHttpClient.Builder mBuilder = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .sslSocketFactory(createSSLSocketFactory())// 信任所有证书~
//                .sslSocketFactory(createSSLSocketFactory(), new OkTrustAllCerts()) // 信任所有证书~
                    .hostnameVerifier(new OkTrustAllHostnameVerifier());
            OkHttpConfig.getInstance().setBuilder(mBuilder);
        }
        init();
        OkHttpConfig.getInstance().setHttpClient(OkHttpConfig.getInstance().getBuilder().build());
    }

    private void init() {
        if (!TextUtils.isEmpty(OkHttpConfig.getInstance().getCachePath())) {
            OkHttpConfig.getInstance().getBuilder().cache(new Cache(new File(OkHttpConfig.getInstance().getCachePath()), cacheSize));
        }
        if (OkHttpConfig.getInstance().isProxy()) {
            OkHttpConfig.getInstance().getBuilder().proxy(Proxy.NO_PROXY);
        }
        if (OkHttpConfig.getInstance().getCookieJar() != null) {
            OkHttpConfig.getInstance().getBuilder().cookieJar(OkHttpConfig.getInstance().getCookieJar());
        }
        if (OkHttpConfig.getInstance().getInterceptors() != null && OkHttpConfig.getInstance().getInterceptors().size() > 0) {
            for (Interceptor interceptor : OkHttpConfig.getInstance().getInterceptors()) {
                OkHttpConfig.getInstance().getBuilder().addInterceptor(interceptor);
            }
        }
        if (OkHttpConfig.getInstance().getNetInterceptors() != null && OkHttpConfig.getInstance().getNetInterceptors().size() > 0) {
            for (Interceptor interceptor : OkHttpConfig.getInstance().getNetInterceptors()) {
                OkHttpConfig.getInstance().getBuilder().addNetworkInterceptor(interceptor);
            }
        }
    }

    @Override
    public Handler obtainHandler() {
        return mDelivery;
    }

    @Override
    public OkHttpClient client() {
        return OkHttpConfig.getInstance().getHttpClient();
    }

    @Override
    public OkHttpClient.Builder bulid() {
        return OkHttpConfig.getInstance().getBuilder();
    }

    @Override
    public void cancel(Object tag) {
        cancel(false, tag);
    }

    @Override
    public void cancelAll() {
        cancel(true, null);
    }

    @Override
    public void cancelHttp(Object tag) {
        OkHttpConfig.getInstance().getTags().put(tag,tag);
    }

    private void cancel(boolean isCancelAll, Object tag) {
        if (OkHttpConfig.getInstance().getHttpClient() == null) return;
        for (okhttp3.Call call : OkHttpConfig.getInstance().getHttpClient().dispatcher().queuedCalls()) {
            if (!isCancelAll) {
                if (tag != null && tag.equals(call.request().tag())) {
                    call.cancel();
                }
            } else call.cancel();
        }
        for (okhttp3.Call call : OkHttpConfig.getInstance().getHttpClient().dispatcher().runningCalls()) {
            if (!isCancelAll) {
                if (tag != null && tag.equals(call.request().tag())) {
                    call.cancel();
                }
            } else call.cancel();
        }
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
