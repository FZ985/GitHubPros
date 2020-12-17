package com.kmt.pro.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.mine.MineItemBean;

import java.util.List;

/**
 * Create by JFZ
 * date: 2020-03-30 15:00
 **/
public class MineItemAdapter extends BaseAdapter {
    private List<MineItemBean> list;

    public MineItemAdapter(List<MineItemBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MineItemViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_linear, null);
            holder = new MineItemViewHolder(convertView);
        } else {
            holder = (MineItemViewHolder) convertView.getTag();
        }
        holder.mine_item_title.setText(list.get(position).title + "");
        Glide.with(BaseApp.getInstance())
                .load(list.get(position).image + "")
                .into(holder.mine_item_image);
        return convertView;
    }

    class MineItemViewHolder {
        public ImageView mine_item_image;
        public TextView mine_item_title;
        public ImageView share_distribution;

        public MineItemViewHolder(View view) {
            mine_item_image = view.findViewById(R.id.mine_item_image);
            mine_item_title = view.findViewById(R.id.mine_item_title);
            share_distribution = view.findViewById(R.id.share_distribution);
            view.setTag(this);
        }
    }
}
