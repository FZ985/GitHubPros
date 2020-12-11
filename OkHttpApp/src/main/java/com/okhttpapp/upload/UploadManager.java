package com.okhttpapp.upload;

import android.text.TextUtils;

import com.okhttplib2.OkHttpFactory;
import com.okhttplib2.callback.Http;
import com.okhttplib2.callback.RequestCallback;
import com.okhttplib2.upload.UIProgressRequestListener;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Description:
 * Author: jfz
 * Date: 2020-12-11 10:40
 */
public class UploadManager {
    private static UploadManager manager;
    private boolean isCanceled = false;

    private UploadManager() {
    }

    private Queue<UploadInfo> mActiveTasks = new ConcurrentLinkedQueue<>();

    public synchronized static UploadManager instance() {
        if (manager == null) {
            manager = new UploadManager();
        }
        return manager;
    }

    public <Bean> void add(Http.Builder builder, UploadListener<Bean> listener, UploadInfo... uploadInfos) {
        if (uploadInfos == null || uploadInfos.length == 0) return;
        for (UploadInfo info : uploadInfos) {
            if (!isActiveTask(info.getId())) {
                isCanceled = false;
                if (info.getFile() == null || TextUtils.isEmpty(info.getFileKey())) {
                    //文件和 文件key 为空跳到下一个循环
                    continue;
                }
                info.setStatus(UploadErr.START);
                if (listener != null){
                    listener.onCall(null,info);
                }
                mActiveTasks.add(info);
                builder.enqueueUploadFile(new File[]{info.getFile()}, new String[]{info.getFileKey()}, new RequestCallback<Bean>() {
                    @Override
                    public void onResponse(Bean data) {
                        if (isCanceled) {
                            info.setStatus(UploadErr.CANCEL);
                        } else {
                            info.setStatus(UploadErr.SUCCESS);
                        }
                        if (listener != null) {
                            listener.onCall(data, info);
                        }
                    }

                    @Override
                    public void onError(int code, Exception e) {
                        info.setStatus(UploadErr.ERROR);
                        info.setMsg(e.getMessage());
                        if (listener != null) {
                            listener.onCall(null, info);
                        }
                    }
                }, new UIProgressRequestListener() {
                    @Override
                    public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                        info.setBytesWrite(bytesWrite);
                        info.setContentLength(contentLength);
                        info.setDone(done);
                        if (isCanceled) {
                            info.setStatus(UploadErr.CANCEL);
                        } else {
                            info.setStatus(UploadErr.RUNNING);
                        }
                        if (listener != null) {
                            listener.onCall(null, info);
                        }
                    }
                });
            }
        }
    }

    public void cancelAll(Object tag) {
        isCanceled = true;
        mActiveTasks.clear();
        OkHttpFactory.getInstance().cancel(tag);
    }

    private boolean isActiveTask(long taskId) {
        return findActiveTask(taskId) != null;
    }

    public UploadInfo findActiveTask(long taskId) {
        for (UploadInfo task : mActiveTasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

}