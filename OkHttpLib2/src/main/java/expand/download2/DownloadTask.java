package expand.download2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import expand.download2.interfaces.DownloadError;
import expand.download2.interfaces.DownloadTaskListener;
import expand.download2.utils.FileUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 * description：下载任务，一个DownloadTask对应一个下载任务
 */
class DownloadTask {

    private static final String TAG = "DownloadTask";

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 下载进度调用间隔，防止频繁的UI刷新操作
     */
    public static final long INTERVAL_PROGRESS = 100;

    private long mProgressStart = 0;

    private long mdTaskId; // 任务id， 唯一
    private DownloadInfo mTaskId; // 任务id， 唯一
    private String mUrl; // 下载地址
    private String mLocalPath; // 本地保存路径
    private long mRangeOffset; // 下载偏移，用于断点续传
    /**
     * 文件读写缓冲大小
     */
    public static final int DOWNLOAD_BUFFER_SIZE = 4096;
    private DownloadTaskListener mDownloadTaskListener;
    private long mDownloadBytes; // 已下载
    private long mTotalBytes; // 总大小
    private volatile boolean mIsCanceled = false;

    public DownloadTask(DownloadInfo taskId, DownloadTaskListener listener) {
        Log.d(TAG, "taskId:" + taskId.getId() + "  url:" + taskId.getUrl() + "  localPath:" + taskId.getPath() + "  rangeOffset:" + taskId.getDownloadedBytes());
        mTaskId = taskId;
        mUrl = taskId.getUrl();
        mLocalPath = taskId.getPath();
        mRangeOffset = taskId.getDownloadedBytes();
        mDownloadTaskListener = listener;

        // create file
        FileUtils.createFile(mLocalPath);

    }

    public void setDownloadTaskListener(DownloadTaskListener listener) {
        mDownloadTaskListener = listener;
    }

    /**
     * Execute task download.
     */
    public void execute() {
        Request request = new Request.Builder().url(mUrl).get().header("Range", "bytes=" + mRangeOffset + "-").build();
        OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();
        OkHttpClient mOkHttpClient = mOkHttpClientBuilder.build();
        mOkHttpClient.newCall(request).enqueue(mOkDownloadCallback);
    }

    private Callback mOkDownloadCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            notifyError(mTaskId, -1, DownloadError.ERROR_NETWORK, e != null ? e.toString() : DownloadError.NAMES.get(DownloadError.ERROR_NETWORK));
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response != null) {
                if (response.code() >= 300) {
                    notifyError(mTaskId, response.code(), DownloadError.ERROR_NETWORK, DownloadError.NAMES.get(DownloadError.ERROR_NETWORK));
                    if (response.body() != null) {
                        response.body().close();
                    }
                    return;
                }
                long responseLen = response.body().contentLength();
                long contentLength = Math.max(0, mRangeOffset + responseLen);
                System.out.println("contentLength===:" + contentLength + ",responseLen:" + responseLen + ",mRangeOffset:" + mRangeOffset);
                long count = mRangeOffset;
                InputStream inStream = response.body().byteStream();
                FileOutputStream buffer = null;
                try {
                    // start
                    notifyStart(mTaskId, count, contentLength);

                    File targetFile = new File(mLocalPath);
                    if (!targetFile.exists()) {
                        FileUtils.createFile(targetFile.getAbsolutePath());
                    }
                    buffer = new FileOutputStream(targetFile, true);
                    if (inStream != null) {
                        byte[] buf = new byte[DOWNLOAD_BUFFER_SIZE];
                        int l;
                        while (!Thread.currentThread().isInterrupted()) {
                            if (mIsCanceled) {
//                                mOkDownload.cancel();
                                break;
                            }
                            l = inStream.read(buf);
                            if (l == -1) {// 写入结束
                                break;
                            }

                            count += l;
                            buffer.write(buf, 0, l);

                            mTotalBytes = contentLength <= 0 ? count : contentLength;
                            mDownloadBytes = count;
                            mRangeOffset = count;

                            if ((System.currentTimeMillis() - mProgressStart) >= INTERVAL_PROGRESS) {
                                notifyProgress(mTaskId, count, mTotalBytes);
                                mProgressStart = System.currentTimeMillis();
                            }
                        }
                        buffer.flush();
                    }
                } catch (Exception e) {
                    notifyError(mTaskId, response.code(), DownloadError.ERROR_IO, e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        if (response.body() != null) {
                            response.body().close();
                        }
                        if (buffer != null) {
                            buffer.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notifyFinished(mTaskId, count, mTotalBytes);
                }
            }
        }
    };

    public void cancel() {
        mIsCanceled = true;
        notifyCancel(mTaskId, mDownloadBytes, mTotalBytes);
    }

    public long getTaskId() {
        return mdTaskId;
    }

    public DownloadInfo getInfo(){
        return mTaskId;
    }
    public String getUrl() {
        return mUrl;
    }

    public String getLocalPath() {
        return mLocalPath;
    }

    private void notifyStart(final DownloadInfo taskId, final long downloadBytes, final long totalBytes) {
        System.out.println("mDownloadTaskListener:" + (mDownloadTaskListener == null) + ",totalBytes:" + totalBytes);
        if (mDownloadTaskListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskId.setDownloadedBytes(downloadBytes);
                    taskId.setTotalSize(totalBytes);
                    mDownloadTaskListener.onStart(taskId, downloadBytes, totalBytes);
                }
            });
        }
    }

    private void notifyProgress(DownloadInfo taskId, long downloadBytes, long totalBytes) {
        if (mDownloadTaskListener != null) {
            taskId.setDownloadedBytes(downloadBytes);
            taskId.setTotalSize(totalBytes);
            mDownloadTaskListener.onProgress(taskId, downloadBytes, totalBytes);
        }
    }

    private void notifyCancel(final DownloadInfo taskId, final long downloadBytes, final long totalBytes) {
        if (mDownloadTaskListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    taskId.setDownloadedBytes(downloadBytes);
                    taskId.setTotalSize(totalBytes);
                    mDownloadTaskListener.onCanceled(taskId, downloadBytes, totalBytes);
                }
            });
        }
    }

    private void notifyError(final DownloadInfo taskId, final int httpStatus, final int errorCode, final String msg) {
        if (mDownloadTaskListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadTaskListener.onError(taskId, httpStatus, errorCode, msg);
                }
            });
        }
    }

    private void notifyFinished(final DownloadInfo taskId, final long downloadBytes, final long totalBytes) {
        if (mDownloadTaskListener != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDownloadTaskListener.onFinished(taskId, downloadBytes, totalBytes);
                }
            });
        }
    }

}
