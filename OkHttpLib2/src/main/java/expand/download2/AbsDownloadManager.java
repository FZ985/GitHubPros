package expand.download2;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import expand.download2.async.WorkHandler;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public abstract class AbsDownloadManager<T> {
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected WorkHandler mWorkHandler = new WorkHandler();
    protected List<IDownloadObserver> mObservers = new CopyOnWriteArrayList<>();

    public abstract void add(final T... items);
    public abstract void pause(final long id);
    public abstract void pauseAll();
    public abstract void resume(final long id);
    public abstract void resumeAll();

    public void registerDownloadObserver(IDownloadObserver observer) {
        if (observer != null && !mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void unRegisterDownloadObserver(IDownloadObserver observer) {
        if (observer != null && mObservers.contains(observer)) {
            mObservers.remove(observer);
        }
    }

    protected void notifyUpdateStatus(final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IDownloadObserver observer : mObservers) {
                    observer.onUpdateStatus(t);
                }
            }
        });
    }

    protected void notifyProgress(final T t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                for (IDownloadObserver observer : mObservers) {
                    observer.onProgress(t);
                }
            }
        });
    }

    public interface QueryCallback<Data> {
        boolean onQueryFinished(int errorCode, Data[] items);
    }

    public interface DeleteCallback<Data> {
        boolean onDeleteFinished(int errorCode, Data[] items);
    }

    public interface OnCheckListener {
        void onCheckFinished(boolean success);
    }

    public interface IDownloadObserver<Data> {
        void onUpdateStatus(Data data);

        void onProgress(Data data);
    }


}
