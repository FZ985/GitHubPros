package com.example.wechataddresslist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wechataddresslist.R;
import com.example.wechataddresslist.bean.SortBean;
import com.example.wechataddresslist.callback.OnRecyclerItemListener;
import com.example.wechataddresslist.utils.Utils;
import com.lib.sticky.BaseStickyHeaderAdapter;

import java.util.List;

public class NativeAdapter extends BaseStickyHeaderAdapter<SortBean, NativeAdapter.ViewHolder, NativeAdapter.SortHeadViewHolder> {
    private List<SortBean> datas;
    private OnRecyclerItemListener itemListener;

    public void setItemListener(OnRecyclerItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public NativeAdapter(List<SortBean> datas) {
        this.datas = datas;
    }

    @Override
    public SortBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
    }

    @Override
    public long getHeaderId(int position) {
        if (getItemViewType(position) == Utils.HEAD) return -1;
        return super.getHeaderId(position);
    }

    @NonNull
    @Override
    public NativeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Utils.HEAD) {
            return new HeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null));
        }
        return new SortViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull NativeAdapter.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case Utils.HEAD:
                ((HeadViewHolder) holder).tv.setText(datas.get(position).getDisplayInfo());
                break;
            case Utils.DATA:
                ((SortViewHolder) holder).tv.setText(datas.get(position).getDisplayInfo());
                break;
        }
        holder.itemView.setOnClickListener(v -> {
            if (itemListener != null) {
                itemListener.onItemClick(this, holder.itemView, position, datas.get(position));
            }
        });
    }

    @Override
    public SortHeadViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new SortHeadViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.head_sort, parent, false));
    }

    @Override
    public void onBindHeaderViewHolder(SortHeadViewHolder holder, int position) {
        holder.head.setText(datas.get(position).getLetter());
    }

    public int getIndex(String str) {
        if (datas == null) return -1;
        for (int i = 0; i < datas.size(); i++) {
            SortBean item = getItem(i);
            String substring = item.getLetter();
            if (item.type == Utils.DATA && substring.toLowerCase().equals(str.toLowerCase()))
                return i;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public abstract static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class SortHeadViewHolder extends ViewHolder {
        public TextView head;

        public SortHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            this.head = itemView.findViewById(R.id.head_tv);
        }
    }

    public static class SortViewHolder extends ViewHolder {
        public TextView tv;

        public SortViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.item_tv);
        }
    }

    public static class HeadViewHolder extends ViewHolder {
        public TextView tv;

        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv = itemView.findViewById(R.id.item_tv);
        }
    }
}