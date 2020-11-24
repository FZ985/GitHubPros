package com.kotlin.k1.adapter2;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.kotlin.k1.R;
import com.kotlin.k1.sticky.BaseStickyHeaderAdapter;
import com.kotlin.k1.sticky.callback.StickyRecyclerHeadersAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter3 extends BaseStickyHeaderAdapter<SortBean, BaseViewHolder> implements StickyRecyclerHeadersAdapter<BaseViewHolder> {
    private List<SortBean> datas;
//    @Override
//    protected void convert(@NotNull BaseViewHolder baseViewHolder, SortBean sortBean) {
//        baseViewHolder.setText(R.id.item_tv,sortBean.getDisplayInfo());
//    }
//
//    @Override
//    public long getHeaderId(int position) {
//        return getItem(position).getFullName().charAt(0);
//    }

    @Override
    public BaseViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.head_sort, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int position) {
        holder.setText(R.id.head_tv, getItem(position).getLetter());
    }

    public Adapter3(@Nullable List<SortBean> data) {
//        super(R.layout.item_list, data);
        this.datas = data;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.setText(R.id.item_tv, datas.get(position).getDisplayInfo());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public SortBean getItem(int pos) {
        return datas.get(pos);
    }

    @Override
    public long getHeaderId(int position) {
        return datas.get(position).getFullName().charAt(0);
    }
}