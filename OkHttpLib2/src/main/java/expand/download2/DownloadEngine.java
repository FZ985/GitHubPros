package expand.download2;

import android.util.Log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import expand.download2.interfaces.DownloadTaskCompleteListener;
import expand.download2.interfaces.DownloadTaskListener;

/**
 * Description: 下载引擎，下载任务调度
 * Author: jfz
 * Date: 2020-12-10 16:29
 */
class DownloadEngine {
    private static final int DEFAULT_MAX_RUNNING_TASKS = 3; // 默认最大下载任务
    private int mMaxRunningTasks = DEFAULT_MAX_RUNNING_TASKS; // 最大下载任务

    private DownloadTaskListener mDownloadTaskListener;

    private DownloadTaskCompleteListener mDownloadTaskCompleteListener;

    /**
     * 暂存队列，如果正在执行的任务队列已是最大可执行任务数{@link #mMaxRunningTasks}，就将新添加进的任务放入到该暂存队列中
     */
    private Queue<DownloadTask> mTemporaryTasks = new ConcurrentLinkedQueue<>();

    /**
     * 正在执行的任务队列
     */
    private Queue<DownloadTask> mActiveTasks = new ConcurrentLinkedQueue<>();

    public void setMaxRunningTasks(int max) {
        if (max > 0) {
            mMaxRunningTasks = max;
        }
    }

    public void addTask(DownloadInfo taskId, DownloadTaskListener listener) {
        Log.d("Download", "addTask_taskId:" + taskId.getId() + "  url:" + taskId.getUrl() + "  path:" + taskId.getPath() + "  rangeOffset:" + taskId.getDownloadedBytes());

        mDownloadTaskListener = listener;
        boolean isTemporaryTask = isTemporaryTask(taskId.getId());
        boolean isActiveTask = isActiveTask(taskId.getId());
        Log.d("Download", "addTask_isTemporaryTask:" + isTemporaryTask + "  isActiveTask:" + isActiveTask);
        if (!isTemporaryTask && !isActiveTask) {
            DownloadTask downloadTask = new DownloadTask(taskId, mInnerDownloadTaskListener);
            mTemporaryTasks.add(downloadTask);

            scheduleNext();
        }
    }

    private void scheduleNext() {
        if (mActiveTasks.size() < mMaxRunningTasks) {
            DownloadTask downloadTask = mTemporaryTasks.poll();
            if (downloadTask != null) {
                mActiveTasks.add(downloadTask);
                downloadTask.execute();
            } else {
                Log.d("Download", "No download task need to execute.");

                if (mDownloadTaskCompleteListener != null) {
                    mDownloadTaskCompleteListener.onComplete();
                }
            }
        }
    }

    public void cancelTask(long taskId) {
        cancelTemporaryTask(taskId);
        cancelActiveTask(taskId);
    }

    private void cancelTemporaryTask(long taskId) {
        DownloadTask downloadTask = findTemporaryTask(taskId);
        if (downloadTask != null) {
            downloadTask.cancel();
            mTemporaryTasks.remove(downloadTask);
        }
    }

    private void cancelActiveTask(long taskId) {
        DownloadTask downloadTask = findActiveTask(taskId);
        if (downloadTask != null) {
            downloadTask.cancel();
            mActiveTasks.remove(downloadTask);
        }
    }

    private void removeActiveTask(long taskId) {
        DownloadTask downloadTask = findActiveTask(taskId);
        if (downloadTask != null) {
            mActiveTasks.remove(downloadTask);
        }
    }

    private boolean isTemporaryTask(long taskId) {
        return findTemporaryTask(taskId) != null;
    }

    private boolean isActiveTask(long taskId) {
        return findActiveTask(taskId) != null;
    }

    private DownloadTask findTemporaryTask(long taskId) {
        for (DownloadTask task : mTemporaryTasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    public DownloadTask findActiveTask(long taskId) {
        for (DownloadTask task : mActiveTasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    private DownloadTaskListener mInnerDownloadTaskListener = new DownloadTaskListener() {

        @Override
        public void onStart(DownloadInfo taskId, long downloadBytes, long totalBytes) {
            if (mDownloadTaskListener != null) {
                mDownloadTaskListener.onStart(taskId, downloadBytes, totalBytes);
            }
        }

        @Override
        public void onProgress(DownloadInfo taskId, long downloadBytes, long totalBytes) {
            if (mDownloadTaskListener != null) {
                mDownloadTaskListener.onProgress(taskId, downloadBytes, totalBytes);
            }
        }

        @Override
        public void onCanceled(DownloadInfo taskId, long downloadBytes, long totalBytes) {
            if (mDownloadTaskListener != null) {
                mDownloadTaskListener.onCanceled(taskId, downloadBytes, totalBytes);
            }
            scheduleNext();
        }

        @Override
        public void onError(DownloadInfo taskId, int httpStatus, int errorCode, String msg) {
            if (mDownloadTaskListener != null) {
                mDownloadTaskListener.onError(taskId, httpStatus, errorCode, msg);
            }
            cancelActiveTask(taskId.getId());
            scheduleNext();
        }

        @Override
        public void onFinished(DownloadInfo taskId, long downloadBytes, long totalBytes) {
            if (mDownloadTaskListener != null) {
                mDownloadTaskListener.onFinished(taskId, downloadBytes, totalBytes);
            }
            removeActiveTask(taskId.getId());
            scheduleNext();
        }

    };
}