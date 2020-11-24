package com.lib.sticky;


import androidx.recyclerview.widget.RecyclerView;

import com.lib.chinese2pinyin.SortObject;
import com.lib.sticky.callback.StickyRecyclerHeadersAdapter;



public abstract class BaseStickyHeaderAdapter<T extends SortObject,
        VH extends RecyclerView.ViewHolder,
        HVH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements StickyRecyclerHeadersAdapter<HVH> {

    public abstract T getItem(int position);

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getLetter().charAt(0);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }
}
