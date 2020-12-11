package expand.download2.interfaces;

/**
 * 定义状态码
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public interface DownloadStatus {

    /**
     * 未知状态
     */
    public static final int STATUS_UNKNOWN = -1;

    /**
     * 等待
     */
    public static final int STATUS_PENDED = 1;

    /**
     * 下载开始
     */
    public static final int STATUS_STARTED = 2;

    /**
     * 下载暂停
     */
    public static final int STATUS_PAUSED = 3;

    /**
     * 下载错误
     */
    public static final int STATUS_ERROR = 4;

    /**
     * 下载完成
     */
    public static final int STATUS_DOWNLOAD_COMPLETED = 5;

}
