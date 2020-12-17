package com.lxbuytimes.kmtapp.retrofit.def.download;

import android.net.Uri;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;
import com.lxbuytimes.kmtapp.retrofit.def.DefaultApi;
import com.lxbuytimes.kmtapp.retrofit.tool.HttpUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2019-10-22 10:25
 **/
public class Download {

    private boolean isCancel;
    private Disposable disposable;
    private DownloadListener listener;

    public static Download obtain() {
        return new Download();
    }

    //不支持断点续传
    public void download(String url, String fileDir, String fileName, DownloadListener listener) {
        download(new DownloadInfo(url, fileDir, fileName, false), listener);
    }

    //可支持断点续传,由用户控制
    public void download(String url, String fileDir, String fileName, boolean isBreakPoint, DownloadListener listener) {
        download(new DownloadInfo(url, fileDir, fileName, isBreakPoint), listener);
    }

    public void download(DownloadInfo info, DownloadListener listener) {
        if (TextUtils.isEmpty(info.url)) return;
        if (!URLUtil.isNetworkUrl(info.url)) return;
        if (listener == null) return;
        this.isCancel = false;
        this.listener = listener;
        Uri uri = Uri.parse(info.url);
        String domain = uri.getScheme() + "://" + uri.getAuthority() + "/";
        String path = TextUtils.isEmpty(uri.getPath()) ? "/" : uri.getPath();

        if (info.finalFile.exists() && info.totalLength == info.currentLength) {
            listener.complete(info.finalFile);
            return;
        }
        DefaultApi api = RetrofitManager.getInstance()
                .getRetrofit(domain)
                .newBuilder()
                .client(RetrofitManager.getInstance().client().newBuilder().addInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    listener.newResponse(originalResponse, info.finalFile);
                    return originalResponse.newBuilder()
                            .body(new DownloadResponseBody(originalResponse, info, listener))
                            .build();
                }).build())
                .build()
                .create(DefaultApi.class);

        api.download("bytes=" + info.currentLength + "-", path)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> {
                    long length = responseBody.contentLength();
                    System.out.println("download:" + length);
                    // 保存文件到本地
                    InputStream is = null;
                    RandomAccessFile randomAccessFile = null;
                    BufferedInputStream bis = null;

                    byte[] buff = new byte[2048];
                    int len;
                    try {
                        is = responseBody.byteStream();
                        bis = new BufferedInputStream(is);

                        File file = info.tmpFile;
                        // 随机访问文件，可以指定断点续传的起始位置
                        randomAccessFile = new RandomAccessFile(file, "rwd");
                        randomAccessFile.seek(info.currentLength);
                        while ((len = bis.read(buff)) != -1) {
                            randomAccessFile.write(buff, 0, len);
                        }

                        // 下载完成
                        info.tmpFile.renameTo(info.finalFile);
                        info.currentLength = info.finalFile.length();
                        listener.complete(info.finalFile);

                    } catch (Exception e) {
                        System.out.println("down_exce1:" + e.getMessage());
                        if (!isCancel())
                            listener.error(new Exception("下载失败"));
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (bis != null) {
                                bis.close();
                            }
                            if (randomAccessFile != null) {
                                randomAccessFile.close();
                            }
                        } catch (Exception e) {
                            System.out.println("down_exce:" + e.getMessage());
                        }
                    }
                    return info;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DownloadInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Download.this.disposable = d;
                    }

                    @Override
                    public void onNext(DownloadInfo downloadInfo) {
                        HttpUtils.log("下载onNext:" + downloadInfo.toString());
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpUtils.log("下载onError:" + e.getMessage());
                        if (listener!= null)listener.error(e);
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onComplete() {
                        HttpUtils.log("下载onComplete");
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                    }
                });
    }

    public void cancel() {
        isCancel = true;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
        if (listener != null) listener.cancel();
    }

    public boolean isCancel() {
        return isCancel;
    }
}
