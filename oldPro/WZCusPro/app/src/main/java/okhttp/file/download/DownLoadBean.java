package okhttp.file.download;

import java.io.Serializable;

/**
 * Created by JFZ .
 * on 2018/1/17.
 */

public class DownLoadBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String SAVE_PATH = "download";
    public String status;
    public String fileName;
    public String downloadUrl;
    public int fileSize;
    public int currentSize;
    public String path;
    public String tempPath;
    public String error;
    public boolean done = false;

    public DownLoadBean(String fileName, String path, String downloadUrl, int fileSize, int currentSize) {
        this.fileName = fileName;
        this.path = path + fileName;
        this.downloadUrl = downloadUrl;
        this.fileSize = fileSize;
        this.currentSize = currentSize;
        this.tempPath = path + fileName.replace(replace(fileName), "") + ".tmp";
    }

    private String replace(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public synchronized void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public String getPath() {
        return path;
    }

    public String getTempPath() {
        return tempPath;
    }

    @Override
    public String toString() {
        return "DownLoadBean{" +
                "status='" + status + '\'' +
                ", fileName='" + fileName + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", fileSize=" + fileSize +
                ", currentSize=" + currentSize +
                ", path='" + path + '\'' +
                ", tempPath='" + tempPath + '\'' +
                ", error='" + error + '\'' +
                ", done=" + done +
                '}';
    }
}
