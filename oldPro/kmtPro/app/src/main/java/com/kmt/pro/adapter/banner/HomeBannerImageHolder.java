package com.kmt.pro.adapter.banner;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeBannerImageHolder extends RecyclerView.ViewHolder {
    public ImageView imageView;

    public HomeBannerImageHolder(@NonNull View view) {
        super(view);
        this.imageView = (ImageView) view;
    }
}