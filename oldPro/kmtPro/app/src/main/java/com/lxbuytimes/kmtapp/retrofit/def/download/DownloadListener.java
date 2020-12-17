package com.lxbuytimes.kmtapp.retrofit.def.download;

import java.io.File;

import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2019-10-22 11:08
 **/
public interface DownloadListener {

    void update(long progress, int percent, long contentLength, boolean done);

    void complete(File file);

    void error(Throwable e);

    void cancel();

    void newResponse(Response response, File file);
}
