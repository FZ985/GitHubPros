package com.kmtlibs.adapter.base.diff;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ListUpdateCallback;
import com.kmtlibs.adapter.base.BaseQuickAdapter;

public class BrvahListUpdateCallback implements ListUpdateCallback {

    private BaseQuickAdapter mAdapter;

    public BrvahListUpdateCallback(BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public void onInserted(int position, int count) {
        mAdapter.notifyItemRangeInserted(position + mAdapter.getHeaderLayoutCount(), count);
    }

    @Override
    public void onRemoved(int position, int count) {
        mAdapter.notifyItemRangeRemoved(position + mAdapter.getHeaderLayoutCount(), count);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
        mAdapter.notifyItemMoved(fromPosition + mAdapter.getHeaderLayoutCount(), toPosition + mAdapter.getHeaderLayoutCount());
    }

    @Override
    public void onChanged(int position, int count, @Nullable Object payload) {
        mAdapter.notifyItemRangeChanged(position + mAdapter.getHeaderLayoutCount(), count, payload);
    }
}