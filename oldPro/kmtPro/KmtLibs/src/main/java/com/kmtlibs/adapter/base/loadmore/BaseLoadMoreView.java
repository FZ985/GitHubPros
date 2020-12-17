package com.kmtlibs.adapter.base.loadmore;

import android.view.View;
import android.view.ViewGroup;

import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

/**
 * 继承此类，实行自定义loadMore视图
 */
public abstract class BaseLoadMoreView {

    /**
     * 根布局
     *
     * @param parent ViewGroup
     * @return View
     */
    public abstract View getRootView(ViewGroup parent);

    /**
     * 布局中的 加载更多视图
     *
     * @param holder BaseViewHolder
     * @return View
     */
    abstract View getLoadingView(BaseViewHolder holder);

    /**
     * 布局中的 加载完成布局
     *
     * @param holder BaseViewHolder
     * @return View
     */
    abstract View getLoadComplete(BaseViewHolder holder);

    /**
     * 布局中的 加载结束布局
     *
     * @param holder BaseViewHolder
     * @return View
     */
    abstract View getLoadEndView(BaseViewHolder holder);

    /**
     * 布局中的 加载失败布局
     *
     * @param holder BaseViewHolder
     * @return View
     */
    abstract View getLoadFailView(BaseViewHolder holder);

    /**
     * 可重写此方式，实行自定义逻辑
     *
     * @param holder         BaseViewHolder
     * @param position       Int
     * @param loadMoreStatus LoadMoreStatus
     */
    public void convert(BaseViewHolder holder, int position, LoadMoreStatus loadMoreStatus) {
        switch (loadMoreStatus) {
            case Complete:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.VISIBLE);
                getLoadFailView(holder).setVisibility(View.GONE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case Loading:
                getLoadingView(holder).setVisibility(View.VISIBLE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.GONE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case Fail:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.VISIBLE);
                getLoadEndView(holder).setVisibility(View.GONE);
                break;
            case End:
                getLoadingView(holder).setVisibility(View.GONE);
                getLoadComplete(holder).setVisibility(View.GONE);
                getLoadFailView(holder).setVisibility(View.GONE);
                getLoadEndView(holder).setVisibility(View.VISIBLE);
                break;
        }
    }
}

