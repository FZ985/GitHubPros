package com.okhttpapp.upload;

public interface UploadListener<Bean> {

    void onCall(Bean t,UploadInfo uploadInfo);
}