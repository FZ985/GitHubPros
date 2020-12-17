package com.lxbuytimes.kmtapp.sortapi;


import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;


public abstract class BaseStickyHeaderAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    public BaseStickyHeaderAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

}
