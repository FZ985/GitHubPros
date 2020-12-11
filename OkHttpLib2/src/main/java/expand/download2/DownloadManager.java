package expand.download2;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import expand.download2.interfaces.DownloadError;
import expand.download2.interfaces.DownloadStatus;
import expand.download2.interfaces.DownloadTaskListener;
import expand.download2.utils.FileUtils;
import expand.download2.utils.MD5Checksum;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 * desc：下载管理器
 */
public class DownloadManager extends AbsDownloadManager<DownloadInfo> {

    public static final String DOWNLOAD_SUFFIX = "tmp"; // 下载临时文件后缀

    private Context mContext;
    private DownloadEngine mDownloadEngine;
    private MD5Checksum mMD5Check = MD5Checksum.getDefault();

    private boolean mIsProcessed = false;

    private static DownloadManager sInstance;

    public static DownloadManager instance(Context context) {
        if (sInstance == null) {
            synchronized (DownloadManager.class) {
                if (sInstance == null) {
                    sInstance = new DownloadManager(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    private DownloadManager(Context context) {
        mContext = context;
        mDownloadEngine = new DownloadEngine();
    }

    public void setMaxRunningTask(int max) {
        mDownloadEngine.setMaxRunningTasks(max);
    }

    private DownloadTaskListener mDownloadTaskListener = new DownloadTaskListener() {

        @Override
        public void onStart(final DownloadInfo taskId, final long downloadBytes, final long totalBytes) {
            Log.d("Download", "onStart");
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (taskId != null) {
                        taskId.setStatus(DownloadStatus.STATUS_STARTED);
                        taskId.setErrorCode(DownloadError.ERROR_SUCCESS);
                        taskId.setModifyTime(System.currentTimeMillis());
                        notifyUpdateStatus(taskId);
                    }
                }
            });
        }

        @Override
        public void onProgress(final DownloadInfo taskId, final long downloadBytes, final long totalBytes) {
            Log.d("Download", "onProgress");
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (taskId != null) {
                        notifyProgress(taskId);
                    }
                }
            });

        }

        @Override
        public void onCanceled(final DownloadInfo info, final long downloadBytes, final long totalBytes) {
            Log.d("Download", "onCanceled");
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (info != null && totalBytes > 0) {
                        int originStatus = info.getStatus();
                        if (downloadBytes == totalBytes) {
                            if (md5Check(info.getMD5(), info.getPath())) {
                                info.setStatus(DownloadStatus.STATUS_DOWNLOAD_COMPLETED);
                                System.out.println("Download_cancel");
                                info.setErrorCode(DownloadError.ERROR_SUCCESS);
                            } else {
                                info.setStatus(DownloadStatus.STATUS_ERROR);
                                info.setErrorCode(DownloadError.ERROR_MD5_NOT_MATCH);
                            }
                        } else {
                            info.setStatus(DownloadStatus.STATUS_PAUSED);
                        }

                        info.setDownloadedBytes(downloadBytes);
                        info.setTotalSize(totalBytes);
                        if (originStatus == DownloadStatus.STATUS_PAUSED
                                && info.getStatus() == DownloadStatus.STATUS_PAUSED) {
                            return;
                        }
                        if (originStatus == DownloadStatus.STATUS_ERROR
                                && info.getStatus() == DownloadStatus.STATUS_ERROR) {
                            return;
                        }
                        if (originStatus == DownloadStatus.STATUS_DOWNLOAD_COMPLETED
                                && info.getStatus() == DownloadStatus.STATUS_DOWNLOAD_COMPLETED) {
                            return;
                        }
                        notifyUpdateStatus(info);
                    }
                }
            });
        }

        @Override
        public void onError(final DownloadInfo info, int httpStatus, final int errorCode, String msg) {
            Log.d("Download", "onError");
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (info != null) {
                        int originStatus = info.getStatus();

                        info.setStatus(DownloadStatus.STATUS_ERROR);
                        info.setErrorCode(errorCode);
                        if (originStatus == DownloadStatus.STATUS_ERROR
                                && info.getStatus() == DownloadStatus.STATUS_ERROR) {
                            return;
                        }
                        notifyUpdateStatus(info);
                    }
                }
            });
        }

        @Override
        public void onFinished(final DownloadInfo info, final long downloadBytes, final long totalBytes) {
            Log.d("Download", "onFinished");
            mWorkHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (info != null) {
                        info.setDownloadedBytes(downloadBytes);
                        info.setTotalSize(totalBytes);

                        if (info.getStatus() == DownloadStatus.STATUS_STARTED
                                || info.getStatus() == DownloadStatus.STATUS_DOWNLOAD_COMPLETED) {
                            if (downloadBytes == totalBytes) {
                                // md5 check
                                if (md5Check(info.getMD5(), info.getPath())) {
                                    info.setStatus(DownloadStatus.STATUS_DOWNLOAD_COMPLETED);

                                    // After download complete, rename file suffix(xxx.apk.tmp -> xxx.apk)
                                    if (info.getPath().endsWith(DOWNLOAD_SUFFIX)) {// 排除将下载完成的物理文件后恢复下载的任务
                                        String destPath = info.getPath().substring(0, info.getPath().length() - DOWNLOAD_SUFFIX.length() - 1);
                                        File tmpFile = new File(info.getPath());
                                        if (tmpFile.renameTo(new File(destPath))) {
                                            info.setPath(destPath);
                                        }
                                    }
                                } else {
                                    info.setStatus(DownloadStatus.STATUS_ERROR);
                                    info.setErrorCode(DownloadError.ERROR_MD5_NOT_MATCH);
                                }
                            } else {
                                info.setStatus(DownloadStatus.STATUS_PAUSED);
                            }
                        }
                        notifyUpdateStatus(info);
                    }
                }
            });
        }
    };

    /**
     * 同时添加多个下载任务
     *
     * @param downloadInfos
     */
    @Override
    public void add(final DownloadInfo... downloadInfos) {
        if (downloadInfos == null || downloadInfos.length == 0) {
            return;
        }
        DownloadInfo[] infos = new DownloadInfo[downloadInfos.length];
        for (int i = 0; i < downloadInfos.length; i++) {
            downloadInfos[i].setPath(genUniqueDownloadPath(downloadInfos[i].getPath(), DOWNLOAD_SUFFIX));
            downloadInfos[i].setStatus(DownloadStatus.STATUS_PENDED);
            infos[i] = (downloadInfos[i]);
        }
        for (DownloadInfo info : infos) {
            mDownloadEngine.addTask(info,
                    mDownloadTaskListener);
        }
    }

    @Override
    public void pause(final long downloadId) {
        mDownloadEngine.cancelTask(downloadId);
        DownloadTask activeTask = mDownloadEngine.findActiveTask(downloadId);
        if (activeTask != null && activeTask.getInfo() != null) {
            activeTask.getInfo().setStatus(DownloadStatus.STATUS_PAUSED);
            notifyUpdateStatus(activeTask.getInfo());
        }
    }

    @Override
    public void pauseAll() {
//        ParallelScheduler.execute(new Task<Object, DownloadInfo[]>(null) {
//            @Override
//            public DownloadInfo[] doInBackground(Object o) {
//                QueryParameter queryParameter = new QueryParameter();
//                String[] status = new String[]{
//                        String.valueOf(DownloadStatus.STATUS_PENDED),
//                        String.valueOf(DownloadStatus.STATUS_STARTED)
//                };
//                queryParameter.setCondition(new Condition().add(DownloadColumns.COLUMN_TASK_STATUS, OperatorFactory.in(status.length), status));
//                DownloadInfo[] infos = mDownloadDBHelper.query(queryParameter);
//                if (infos != null) {
//                    for (DownloadInfo info : infos) {
//                        info.setStatus(DownloadStatus.STATUS_PAUSED);
//                    }
//                    mDownloadDBHelper.update(infos);
//                }
//
//                return infos;
//            }
//
//            @Override
//            public void onPostExecute(DownloadInfo[] downloadInfos) {
//                if (downloadInfos != null) {
//                    for (DownloadInfo info : downloadInfos) {
//                        mDownloadEngine.cancelTask(info.getId());
//
//                        notifyUpdateStatus(info);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void resume(final long downloadId) {
//        SerialScheduler.execute(new Task<Long, DownloadInfo>(downloadId) {
//            @Override
//            public DownloadInfo doInBackground(Long aLong) {
//                DownloadInfo info = mDownloadDBHelper.query(downloadId);
//                if (info != null) {
//                    File targetFile = new File(info.getPath());
//                    if (targetFile != null && targetFile.exists()) {
//                        long downloaded = targetFile.length();
//                        info.setDownloadedBytes(downloaded);
//                    }
//                    int status = isDownloadComplete(info) ? DownloadStatus.STATUS_DOWNLOAD_COMPLETED : DownloadStatus.STATUS_PENDED;
//                    info.setStatus(status);
//                    mDownloadDBHelper.update(info);
//                }
//
//                return info;
//            }
//
//            @Override
//            public void onPostExecute(DownloadInfo downloadInfo) {
//                if (downloadInfo != null) {
//                    if (downloadInfo.getStatus() != DownloadStatus.STATUS_DOWNLOAD_COMPLETED) {
//                        long rangeOffset = new File(downloadInfo.getPath()).exists() ? downloadInfo.getDownloadedBytes() : 0;
//                        mDownloadEngine.addTask(downloadInfo.getId(), downloadInfo.getUrl(), downloadInfo.getPath(), rangeOffset, mDownloadTaskListener);
//                    }
//
//                    notifyUpdateStatus(downloadInfo);
//                }
//            }
//        });
    }

    @Override
    public void resumeAll() {
//        SerialScheduler.execute(new Task<Object, DownloadInfo[]>(null) {
//            @Override
//            public DownloadInfo[] doInBackground(Object o) {
//                DownloadInfo[] infos = mDownloadDBHelper.queryUnComplete();
//                if (infos != null) {
//                    for (DownloadInfo info : infos) {
//                        File targetFile = new File(info.getPath());
//                        if (targetFile.exists()) {
//                            long length = targetFile.length();
//                            info.setDownloadedBytes(length);
//                        }
//                        int status = isDownloadComplete(info) ? DownloadStatus.STATUS_DOWNLOAD_COMPLETED : DownloadStatus.STATUS_PENDED;
//                        info.setStatus(status);
//                    }
//                    mDownloadDBHelper.update(infos);
//                }
//
//                return infos;
//            }
//
//            @Override
//            public void onPostExecute(DownloadInfo[] downloadInfos) {
//                if (downloadInfos != null) {
//                    for (DownloadInfo info : downloadInfos) {
//                        if (info.getStatus() != DownloadStatus.STATUS_DOWNLOAD_COMPLETED) {
//                            long rangeOffset = new File(info.getPath()).exists() ? info.getDownloadedBytes() : 0;
//                            mDownloadEngine.addTask(info.getId(), info.getUrl(), info.getPath(), rangeOffset, mDownloadTaskListener);
//                        }
//
//                        notifyUpdateStatus(info);
//                    }
//                }
//            }
//        });
    }

    /**
     * 下载完成?
     *
     * @param downloadInfo
     * @return
     */
    private boolean isDownloadComplete(DownloadInfo downloadInfo) {
        if (downloadInfo == null) {
            return false;
        }
        return (downloadInfo.getTotalSize() > 0 && downloadInfo.getDownloadedBytes() == downloadInfo.getTotalSize());
    }

    /**
     * 生成唯一的文件名
     *
     * @param path
     * @param fixedSuffix
     * @return
     */
    private static String genUniqueDownloadPath(String path, String fixedSuffix) {
        int number = 0;
        String directory = FileUtils.getFilePathDir(path);
        String shortFileName = FileUtils.getFileShortName(path); // 文件名，不包含后缀
        String suffix = FileUtils.getFileExtension(path); // 后缀
        String uniqueFilename = path;

        String unionSuffix = TextUtils.isEmpty(suffix) ? fixedSuffix : suffix + "." + fixedSuffix;
        String unionSuffixFilename = directory + File.separator + shortFileName + "." + unionSuffix;

        File file = new File(uniqueFilename);
        File unionFile = new File(unionSuffixFilename);
        while (file.exists() || unionFile.exists()) {
            ++number;
            uniqueFilename = directory + File.separator + shortFileName + "_" + number + "." + suffix;
            unionSuffixFilename = directory + File.separator + shortFileName + "_" + number + "." + unionSuffix;
            file = new File(uniqueFilename);
            unionFile = new File(unionSuffixFilename);
        }
        return unionSuffixFilename;
    }

    /**
     * md5检查
     *
     * @param md5
     * @param path
     * @return md5值不存在，return true， 否则进行md5检查
     */
    private boolean md5Check(String md5, String path) {
        if (TextUtils.isEmpty(md5)) {
            return true;
        }
        return mMD5Check.check(md5, new File(path));
    }

}
