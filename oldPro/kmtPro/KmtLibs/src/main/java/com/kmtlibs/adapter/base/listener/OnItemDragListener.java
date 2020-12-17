package com.kmtlibs.adapter.base.listener;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by luoxw on 2016/6/20.
 */
public interface OnItemDragListener {
    void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos);

    void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to);

    void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos);

    void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

    long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy, long superDuration);
}
