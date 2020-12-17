package com.kmt.pro.callback.impl;

import android.graphics.Canvas;

import com.kmtlibs.adapter.base.listener.OnItemDragListener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by JFZ
 * date: 2020-08-04 17:23
 **/
public class OnItemDragListenerImpl implements OnItemDragListener {
    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

    }

    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }

    @Override
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy, long superDuration) {
        return superDuration;
    }
}
