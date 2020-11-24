package com.example.wechataddresslist.callback;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public interface OnRecyclerItemListener<T> {

    void onItemClick(RecyclerView.Adapter adapter, View view, int position, T data);
}