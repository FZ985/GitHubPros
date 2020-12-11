package expand.download2;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import com.okhttplib2.callback.Http;
import com.okhttplib2.utils.OkhttpUtil;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class Download implements Http.Config {
    private Handler mDelivery;
    private OkHttpClient mOkHttpClient;
    private OkHttpClient.Builder mBuilder;

    public synchronized static Download getInstance() {
        return DownloadHolder.download;
    }

    private static class DownloadHolder {
        public static volatile Download download = new Download();
    }

    @Override
    public Handler obtainHandler() {
        return mDelivery;
    }

    @Override
    public OkHttpClient client() {
        return mOkHttpClient;
    }

    @Override
    public OkHttpClient.Builder bulid() {
        return mBuilder;
    }

    @Override
    public void cancel(Object tag) {

    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void newBuild() {

    }

    @Override
    public void cancelHttp(Object tag) {

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

    private Download() {
        mDelivery = new Handler(Looper.getMainLooper());
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(), new OkTrustAllCerts()) // 信任所有证书~
                .hostnameVerifier(new OkTrustAllHostnameVerifier());
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mBuilder.addInterceptor(httpLoggingInterceptor);

        mOkHttpClient = mBuilder.build();
    }

    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    public void download(String url) {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(url).get().header("Range", "bytes=" + 0 + "-").build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log("失败:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response != null) {
                            if (response.code() >= 300) {
//                        notifyError(mTaskId, response.code(), DownloadError.ERROR_NETWORK, DownloadError.NAMES.get(DownloadError.ERROR_NETWORK));
                                if (response.body() != null) {
                                    response.body().close();
                                }
                                return;
                            }
                            long contentLength = Math.max(0, 0 + response.body().contentLength());
                            log("contentLength===:" + contentLength);
//                    long count = mRangeOffset;
//                    InputStream inStream = response.body().byteStream();
//                    FileOutputStream buffer = null;
//                    try {
//                        // start
//                        notifyStart(mTaskId, count, contentLength);
//
//                        File targetFile = new File(mLocalPath);
//                        if (!targetFile.exists()) {
//                            FileUtils.createFile(targetFile.getAbsolutePath());
//                        }
//                        buffer = new FileOutputStream(targetFile, mOkDownload.isAppend());
//                        if (inStream != null) {
//                            byte[] buf = new byte[OkDownload.DOWNLOAD_BUFFER_SIZE];
//                            int l;
//                            while (!Thread.currentThread().isInterrupted()) {
//                                if (mIsCanceled) {
//                                    mOkDownload.cancel();
//                                    break;
//                                }
//                                l = inStream.read(buf);
//                                if (l == -1) {// 写入结束
//                                    break;
//                                }
//
//                                count += l;
//                                buffer.write(buf, 0, l);
//
//                                mTotalBytes = contentLength <= 0 ? count : contentLength;
//                                mDownloadBytes = count;
//                                mRangeOffset = count;
//
//                                if ((System.currentTimeMillis() - mProgressStart) >= INTERVAL_PROGRESS) {
//                                    notifyProgress(mTaskId, count, mTotalBytes);
//                                    mProgressStart = System.currentTimeMillis();
//                                }
//                            }
//                            buffer.flush();
//                        }
//                    } catch (Exception e) {
//                        notifyError(mTaskId, response.code(), DownloadError.ERROR_IO, e.getMessage());
//                        e.printStackTrace();
//                    } finally {
//                        try {
//                            if (response.body() != null) {
//                                response.body().close();
//                            }
//                            if (buffer != null) {
//                                buffer.close();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        notifyFinished(mTaskId, count, mTotalBytes);
//                    }
                        }
                    }
                });
            }
        });

    }

    private void log(String m) {
        OkhttpUtil.log("Download", m);
    }
}