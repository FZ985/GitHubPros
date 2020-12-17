package com.kmtlibs.adapter.base.diff;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.DiffUtil;

public class BrvahAsyncDifferConfig<T> {
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    Executor mainThreadExecutor;
    Executor backgroundThreadExecutor;
    DiffUtil.ItemCallback<T> diffCallback;

    public BrvahAsyncDifferConfig(Executor mainThreadExecutor, Executor backgroundThreadExecutor, DiffUtil.ItemCallback<T> diffCallback) {
        this.mainThreadExecutor = mainThreadExecutor;
        this.backgroundThreadExecutor = backgroundThreadExecutor;
        this.diffCallback = diffCallback;
    }

    /**
     * Builder class for [BrvahAsyncDifferConfig].
     *
     * @param <T> </T>
     */
    public static   class Builder<T> {
        private DiffUtil.ItemCallback<T> mDiffCallback;
        private Executor mMainThreadExecutor = null;
        private Executor mBackgroundThreadExecutor = null;

        public Builder(DiffUtil.ItemCallback<T> mDiffCallback) {
            this.mDiffCallback = mDiffCallback;
        }

        /**
         * If provided, defines the main thread executor used to dispatch adapter update
         * notifications on the main thread.
         * <p>
         * <p>
         * If not provided, it will default to the main thread.
         *
         * @param executor The executor which can run tasks in the UI thread.
         * @return this
         * @hide
         */
        public Builder<T> setMainThreadExecutor(Executor executor) {
            mMainThreadExecutor = executor;
            return this;
        }

        /**
         * If provided, defines the background executor used to calculate the diff between an old
         * and a new list.
         * <p>
         * <p>
         * If not provided, defaults to two thread pool executor, shared by all ListAdapterConfigs.
         *
         * @param executor The background executor to run list diffing.
         * @return this
         */
        public Builder<T> setBackgroundThreadExecutor(Executor executor) {
            mBackgroundThreadExecutor = executor;
            return this;
        }

        /**
         * Creates a [BrvahAsyncDifferConfig] with the given parameters.
         *
         * @return A new AsyncDifferConfig.
         */
        public BrvahAsyncDifferConfig<T> build() {
            if (mBackgroundThreadExecutor == null) {
                synchronized (sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                }
                mBackgroundThreadExecutor = sDiffExecutor;
            }
            return new BrvahAsyncDifferConfig(
                    mMainThreadExecutor,
                    mBackgroundThreadExecutor,
                    mDiffCallback);
        }

        // TODO: remove the below once supportlib has its own appropriate executors
        private Object sExecutorLock = new Object();
        private Executor sDiffExecutor = null;

    }
}