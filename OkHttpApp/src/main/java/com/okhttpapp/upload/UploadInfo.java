package com.okhttpapp.upload;

import java.io.File;
import java.io.Serializable;

/**
 * Description:
 * Author: jfz
 * Date: 2020-12-11 10:47
 */
public class UploadInfo implements Serializable {

    private long mId;
    private File file;
    private String fileKey;

    private long bytesWrite;
    private long contentLength;
    private boolean done;
    private int status;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UploadInfo(long mId, File file, String fileKey) {
        this.mId = mId;
        this.file = file;
        this.fileKey = fileKey;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public long getBytesWrite() {
        return bytesWrite;
    }

    public void setBytesWrite(long bytesWrite) {
        this.bytesWrite = bytesWrite;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}