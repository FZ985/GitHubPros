package com.kmtlibs.okhttp.callback;

/**
 * Created by JFZ .
 * on 2018/1/16.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}
