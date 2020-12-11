package expand.download2.interfaces;

import android.util.SparseArray;

/**
 * 定义错误码
 *
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public class DownloadError {
    public static final int ERROR_SUCCESS = 0;// 成功
    public static final int ERROR_UNKNOWN = 1;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_IO = 3;
    public static final int ERROR_TIMEOUT = 4;
    public static final int ERROR_DUPLICATE_DOWNLOAD = 5;
    public static final int ERROR_FILE_EXIST = 6;
    public static final int ERROR_SDCARD_INVALID = 7;
    public static final int ERROR_SDCARD_FULL = 8;
    public static final int ERROR_INVALID_URL = 9;
    public static final int ERROR_MD5_NOT_MATCH = 10;

    public static final SparseArray<String> NAMES = new SparseArray<>();

    static {
        NAMES.append(ERROR_SUCCESS, "ERROR_SUCCESS");
        NAMES.append(ERROR_UNKNOWN, "ERROR_UNKNOWN");
        NAMES.append(ERROR_NETWORK, "ERROR_NETWORK");
        NAMES.append(ERROR_IO, "ERROR_IO");
        NAMES.append(ERROR_TIMEOUT, "ERROR_TIMEOUT");
        NAMES.append(ERROR_DUPLICATE_DOWNLOAD, "ERROR_DUPLICATE_DOWNLOAD");
        NAMES.append(ERROR_FILE_EXIST, "ERROR_FILE_EXIST");
        NAMES.append(ERROR_SDCARD_INVALID, "ERROR_SDCARD_INVALID");
        NAMES.append(ERROR_SDCARD_FULL, "ERROR_SDCARD_FULL");
        NAMES.append(ERROR_INVALID_URL, "ERROR_INVALID_URL");
        NAMES.append(ERROR_MD5_NOT_MATCH, "ERROR_MD5_NOT_MATCH");
    }

}
