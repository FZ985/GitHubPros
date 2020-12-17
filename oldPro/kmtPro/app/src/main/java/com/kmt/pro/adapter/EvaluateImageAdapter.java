package com.kmt.pro.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by JFZ
 * date: 2020-08-04 12:10
 **/
public class EvaluateImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int size;

    public EvaluateImageAdapter(int size) {
        super(R.layout.item_evaluate_image);
        this.size = size;
    }

    @Override
    protected void convert(BaseViewHolder holder, String item) {
        ImageView item_showimg = holder.getView(R.id.item_showimg);
        ImageView item_delete = holder.getView(R.id.item_delete);
        holder.itemView.setVisibility(View.VISIBLE);
        holder.itemView.setAlpha(1f);
        Glide.with(BaseApp.getInstance())
                .load(item)
                .centerCrop()
                .into(item_showimg);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(size, size));
    }

}
