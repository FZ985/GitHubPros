package com.kotlin.k1.sticky;


import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;


public abstract class BaseStickyHeaderAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

//    public BaseStickyHeaderAdapter(int layoutResId, @Nullable List<T> data) {
//        super(layoutResId, data);
//    }
//
//    public T getItem(int position) {
//        return getData().get(position);
//    }


}
