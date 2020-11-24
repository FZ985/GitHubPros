package com.app.api29.sticky;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import api29.libs.adapter.base.BaseQuickAdapter;
import api29.libs.adapter.base.viewholder.BaseViewHolder;


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
