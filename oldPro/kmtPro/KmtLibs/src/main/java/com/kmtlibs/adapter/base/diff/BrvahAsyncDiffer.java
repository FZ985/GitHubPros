package com.kmtlibs.adapter.base.diff;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DiffUtil.DiffResult;
import androidx.recyclerview.widget.ListUpdateCallback;
import com.kmtlibs.adapter.base.BaseQuickAdapter;

public class BrvahAsyncDiffer<T> implements DifferImp<T> {
    private BaseQuickAdapter adapter;
    private BrvahAsyncDifferConfig config;
    private Executor sMainThreadExecutor = new MainThreadExecutor();
    private Executor mMainThreadExecutor;
    private ListUpdateCallback mUpdateCallback;
    private List<ListChangeListener<T>> mListeners = new ArrayList<>();
    private int mMaxScheduledGeneration = 0;

    public BrvahAsyncDiffer(BaseQuickAdapter adapter, BrvahAsyncDifferConfig config) {
        this.adapter = adapter;
        this.config = config;
        mUpdateCallback = new BrvahListUpdateCallback(this.adapter);
        mMainThreadExecutor = (this.config != null) ? config.mainThreadExecutor : sMainThreadExecutor;
    }

    private class MainThreadExecutor implements Executor {
        Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mHandler.post(command);
        }
    }

    public void submitList(List<T> newList, Runnable commitCallback) {
        // incrementing generation means any currently-running diffs are discarded when they finish
        int runGeneration = ++mMaxScheduledGeneration;
        if (newList == adapter.data) {
            // nothing to do (Note - still had to inc generation, since may have ongoing work)
            if (commitCallback != null) {
                commitCallback.run();
            }
            return;
        }
        List<T> oldList = adapter.data;
        // fast simple remove all
        if (newList == null) {
            int countRemoved = adapter.data.size();
            adapter.data = new ArrayList();
            // notify last, after list is updated
            mUpdateCallback.onRemoved(0, countRemoved);
            onCurrentListChanged(oldList, commitCallback);
            return;
        }
        // fast simple first insert
        if (adapter.data == null || adapter.data.isEmpty()) {
            adapter.data = newList;
            // notify last, after list is updated
            mUpdateCallback.onInserted(0, newList.size());
            onCurrentListChanged(oldList, commitCallback);
            return;
        }
        config.backgroundThreadExecutor.execute(() -> {
            DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {

                @Override
                public int getOldListSize() {
                    return oldList.size();
                }

                @Override
                public int getNewListSize() {
                    return newList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    T oldItem = oldList.get(oldItemPosition);
                    T newItem = newList.get(newItemPosition);
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.areItemsTheSame(oldItem, newItem);
                    } else return oldItem == null && newItem == null;
                    // If both items are null we consider them the same.
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    T oldItem = oldList.get(oldItemPosition);
                    T newItem = newList.get(newItemPosition);
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.areContentsTheSame(oldItem, newItem);
                    }
                    if (oldItem == null && newItem == null) {
                        return true;
                    }
                    throw new AssertionError();
                }

                @Nullable
                @Override
                public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                    T oldItem = oldList.get(oldItemPosition);
                    T newItem = newList.get(newItemPosition);
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.getChangePayload(oldItem, newItem);
                    }
                    throw new AssertionError();
                }
            });

            mMainThreadExecutor.execute(() -> {
                if (mMaxScheduledGeneration == runGeneration) {
                    latchList(newList, result, commitCallback);
                }
            });
        });
    }

    private void latchList(List<T> newList, DiffResult diffResult, Runnable commitCallback) {
        List<T> previousList = adapter.data;
        adapter.data = newList;
        diffResult.dispatchUpdatesTo(mUpdateCallback);
        onCurrentListChanged(previousList, commitCallback);
    }

    private void onCurrentListChanged(List<T> previousList, Runnable commitCallback) {
        for (ListChangeListener<T> listener : mListeners) {
            listener.onCurrentListChanged(previousList, adapter.data);
        }
        if (commitCallback != null) {
            commitCallback.run();
        }
    }

    /**
     * Add a ListListener to receive updates when the current List changes.
     *
     * @param listener Listener to receive updates.
     * @see .getCurrentList
     * @see .removeListListener
     */
    @Override
    public void addListListener(@NonNull ListChangeListener<T> listener) {
        mListeners.add(listener);
    }

    /**
     * Remove a previously registered ListListener.
     *
     * @param listener Previously registered listener.
     * @see .getCurrentList
     * @see .addListListener
     */
    public void removeListListener(ListChangeListener<T> listener) {
        mListeners.remove(listener);
    }

    public void clearAllListListener() {
        mListeners.clear();
    }
}