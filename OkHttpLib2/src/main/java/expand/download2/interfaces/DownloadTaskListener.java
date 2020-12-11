package expand.download2.interfaces;

import expand.download2.DownloadInfo;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 * description：下载任务监听器
 */
public interface DownloadTaskListener {
    void onStart(DownloadInfo taskId, long downloadBytes, long totalBytes);

    void onProgress(DownloadInfo taskId, long downloadBytes, long totalBytes);

    void onCanceled(DownloadInfo taskId, long downloadBytes, long totalBytes);

    void onError(DownloadInfo taskId, int httpStatus, int errorCode, String msg);

    void onFinished(DownloadInfo taskId, long downloadBytes, long totalBytes);
}
