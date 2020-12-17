package com.lxbuytimes.kmtapp.retrofit.def.download;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Create by JFZ
 * date: 2019-10-22 11:22
 **/
public class DownloadInfo implements Serializable {

    public String url;
    private String path;
    private String fileName;
    public long currentLength;//下载文件当前文件大小
    public long totalLength;//下载文件总大小
    public File tmpFile;//临时文件
    public File finalFile;//最终文件
    private boolean isBreakPoint;//是否支持断点续传
    private String tmp = ".tmp";//临时文件后缀

    public DownloadInfo(String url, String path, String fileName) {
        this(url, path, fileName, false);
    }

    public DownloadInfo(String url, String path, String fileName, boolean isBreakPoint) {
        this.url = url;
        this.path = path;
        this.fileName = fileName;
        this.isBreakPoint = isBreakPoint;
        this.currentLength = getCurrentLength();
    }

    private long getCurrentLength() {
        File dir = new File(this.path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.finalFile = new File(dir, this.fileName);
        this.tmpFile = new File(dir, this.fileName + tmp);
        if (!finalFile.exists()) {
            try {
                if (!tmpFile.exists()) {
                    tmpFile.createNewFile();
                } else {
                    if (!isBreakPoint() && tmpFile.delete()) {
                        tmpFile.createNewFile();
                    }
                }
                return tmpFile.length();
            } catch (IOException e) {
                return 0;
            }
        } else {
            totalLength = finalFile.length();
            return finalFile.length();
        }
    }

    public boolean isBreakPoint() {
        return isBreakPoint;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", fileName='" + fileName + '\'' +
                ", currentLength=" + currentLength +
                ", totalLength=" + totalLength +
                ", finalFile=" + finalFile +
                '}';
    }
}
