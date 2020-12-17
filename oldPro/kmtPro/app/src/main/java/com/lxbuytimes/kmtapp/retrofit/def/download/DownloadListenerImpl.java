package com.lxbuytimes.kmtapp.retrofit.def.download;

import java.io.File;

import okhttp3.Response;

/**
 * Create by JFZ
 * date: 2019-10-22 11:14
 **/
public abstract class DownloadListenerImpl implements DownloadListener {
    @Override
    public void cancel() {

    }

    @Override
    public void newResponse(Response response, File file) {

    }
}
