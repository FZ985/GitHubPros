package com.kmt.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kmt.pro.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by JFZ
 * date: 2020-07-01 16:34
 **/
public class Statelayout extends FrameLayout {
    public static final int STATUS_CONTENT = 0x00;
    public static final int STATUS_LOADING = 0x01;
    public static final int STATUS_EMPTY = 0x02;
    public static final int STATUS_ERROR = 0x03;
    public static final int STATUS_NO_NETWORK = 0x04;
    private View mEmptyView;
    private View mErrorView;
    private View mLoadingView;
    private View mNoNetworkView;
    private View mContentView;
    private int mEmptyViewResId;
    private int mErrorViewResId;
    private int mLoadingViewResId;
    private int mNoNetworkViewResId;
    private int mViewStatus;

    private LayoutInflater mInflater;
    private final ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    public Statelayout(@NonNull Context context) {
        this(context, null);
    }

    public Statelayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Statelayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IStateLayout, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.IStateLayout_emptyLayout, R.layout.default_layout_empty);
        mErrorViewResId = a.getResourceId(R.styleable.IStateLayout_errorLayout, R.layout.default_layout_error);
        mLoadingViewResId = a.getResourceId(R.styleable.IStateLayout_loadingLayout, R.layout.default_layout_loading);
        mNoNetworkViewResId = a.getResourceId(R.styleable.IStateLayout_noNetworkLayout, R.layout.default_layout_nonetwork);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mInflater = LayoutInflater.from(getContext());
        showContent();
    }

    public void showEmpty() {
        showEmpty(null);
    }

    public void showEmpty(View.OnClickListener listener) {
        mViewStatus = STATUS_EMPTY;
        if (null == mEmptyView) {
            mEmptyView = mInflater.inflate(mEmptyViewResId, null);
            if (mEmptyViewResId == R.layout.default_layout_empty) {
                TextView emptyTv = mEmptyView.findViewById(R.id.empty_retry_tv);
                clickListener(emptyTv, listener);
            } else {
                clickListener(mEmptyView, listener);
            }
            addView(mEmptyView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    public void showError() {
        showError(null);
    }

    public void showError(View.OnClickListener listener) {
        mViewStatus = STATUS_ERROR;
        if (null == mErrorView) {
            mErrorView = mInflater.inflate(mErrorViewResId, null);
            if (mErrorViewResId == R.layout.default_layout_error) {
                TextView emptyTv = mErrorView.findViewById(R.id.error_retry_tv);
                clickListener(emptyTv, listener);
            } else {
                clickListener(mErrorView, listener);
            }
            addView(mErrorView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    public void showLoading() {
        mViewStatus = STATUS_LOADING;
        if (null == mLoadingView) {
            mLoadingView = mInflater.inflate(mLoadingViewResId, null);
            addView(mLoadingView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    public final void showNoNetwork() {
        showNoNetwork(null);
    }

    public final void showNoNetwork(View.OnClickListener listener) {
        mViewStatus = STATUS_NO_NETWORK;
        if (null == mNoNetworkView) {
            mNoNetworkView = mInflater.inflate(mNoNetworkViewResId, null);
            if (mNoNetworkViewResId == R.layout.default_layout_nonetwork) {
                TextView emptyTv = mNoNetworkView.findViewById(R.id.network_retry_tv);
                clickListener(emptyTv, listener);
            } else {
                clickListener(mNoNetworkView, listener);
            }
            addView(mNoNetworkView, 0, mLayoutParams);
        }
        showViewByStatus(mViewStatus);
    }

    public void clickListener(View view, View.OnClickListener listener) {
        if (listener != null) {
            view.setOnClickListener(listener);
        }
    }

    public void showContent() {
        mViewStatus = STATUS_CONTENT;
        showViewByStatus(mViewStatus);
    }

    public void setEmptyView(View view) {
        if (mEmptyView != null) {
            removeView(mEmptyView);
        }
        if (view != null) {
            this.mEmptyView = view;
            addView(mEmptyView, 0, mLayoutParams);
        }
    }

    public void setErrorView(View view) {
        if (mErrorView != null) {
            removeView(mErrorView);
        }
        if (view != null) {
            this.mErrorView = view;
            addView(mErrorView, 0, mLayoutParams);
        }
    }

    public void setNetWorkView(View view) {
        if (mNoNetworkView != null) {
            removeView(mNoNetworkView);
        }
        if (view != null) {
            this.mNoNetworkView = view;
            addView(mNoNetworkView, 0, mLayoutParams);
        }
    }

    public void setLoadingView(View view) {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        if (view != null) {
            this.mLoadingView = view;
            addView(mLoadingView, 0, mLayoutParams);
        }
    }

    public void setContentView(View view) {
        if (mContentView != null) {
            removeView(mContentView);
        }
        if (view != null) {
            this.mContentView = view;
            addView(mContentView, 0, mLayoutParams);
        }
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public View getErrorView() {
        return mErrorView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getNoNetworkView() {
        return mNoNetworkView;
    }

    public View getContentView() {
        return mContentView;
    }

    private void showViewByStatus(int viewStatus) {
        if (null != mLoadingView) {
            mLoadingView.setVisibility(viewStatus == STATUS_LOADING ? View.VISIBLE : View.GONE);
        }
        if (null != mEmptyView) {
            mEmptyView.setVisibility(viewStatus == STATUS_EMPTY ? View.VISIBLE : View.GONE);
        }
        if (null != mErrorView) {
            mErrorView.setVisibility(viewStatus == STATUS_ERROR ? View.VISIBLE : View.GONE);
        }
        if (null != mNoNetworkView) {
            mNoNetworkView.setVisibility(viewStatus == STATUS_NO_NETWORK ? View.VISIBLE : View.GONE);
        }
        if (null != mContentView) {
            mContentView.setVisibility(viewStatus == STATUS_CONTENT ? View.VISIBLE : View.GONE);
        }
    }
}
