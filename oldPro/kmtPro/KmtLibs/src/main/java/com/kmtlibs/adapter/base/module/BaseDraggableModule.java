package com.kmtlibs.adapter.base.module;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.kmtlibs.R;
import com.kmtlibs.adapter.base.BaseQuickAdapter;
import com.kmtlibs.adapter.base.dragswipe.DragAndSwipeCallback;
import com.kmtlibs.adapter.base.listener.DraggableListenerImp;
import com.kmtlibs.adapter.base.listener.OnItemDragListener;
import com.kmtlibs.adapter.base.listener.OnItemSwipeListener;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Create by JFZ
 * date: 2020-05-07 19:17
 **/
public class BaseDraggableModule implements DraggableListenerImp {
    private BaseQuickAdapter baseQuickAdapter;

    public BaseDraggableModule(BaseQuickAdapter baseQuickAdapter) {
        this.baseQuickAdapter = baseQuickAdapter;
        itemTouchHelperCallback = new DragAndSwipeCallback(this);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
    }

    boolean isDragEnabled = false;
    boolean isSwipeEnabled = false;

    public void setDragEnabled(boolean dragEnabled) {
        isDragEnabled = dragEnabled;
    }

    public void setSwipeEnabled(boolean swipeEnabled) {
        isSwipeEnabled = swipeEnabled;
    }

    public boolean isDragEnabled() {
        return isDragEnabled;
    }

    public boolean isSwipeEnabled() {
        return isSwipeEnabled;
    }

    int toggleViewId = NO_TOGGLE_VIEW;
    ItemTouchHelper itemTouchHelper;
    DragAndSwipeCallback itemTouchHelperCallback;

    protected View.OnTouchListener mOnToggleViewTouchListener;
    protected View.OnLongClickListener mOnToggleViewLongClickListener;
    protected OnItemDragListener mOnItemDragListener;
    protected OnItemSwipeListener mOnItemSwipeListener;


    public void initView(BaseViewHolder holder) {
        if (isDragEnabled) {
            if (hasToggleView()) {
                View toggleView = holder.itemView.findViewById(toggleViewId);
                if (toggleView != null) {
                    toggleView.setTag(R.id.BaseQuickAdapter_viewholder_support, holder);
                    if (isDragOnLongPressEnabled) {
                        toggleView.setOnLongClickListener(mOnToggleViewLongClickListener);
                    } else {
                        toggleView.setOnTouchListener(mOnToggleViewTouchListener);
                    }
                }
            }
        }
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Is there a toggle view which will trigger drag event.
     */
    public boolean hasToggleView() {
        return toggleViewId != NO_TOGGLE_VIEW;
    }

    /**
     * Set the drag event should be trigger on long press.
     * Work when the toggleViewId has been set.
     */
    private boolean isDragOnLongPressEnabled = true;

    @SuppressLint("ClickableViewAccessibility")
    public void setDragOnLongPressEnabled(boolean value) {
        isDragOnLongPressEnabled = value;
        if (value) {
            mOnToggleViewTouchListener = null;
            mOnToggleViewLongClickListener = v -> {
                if (isDragEnabled) {
                    itemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                }
                return true;
            };
        } else {
            mOnToggleViewTouchListener = (v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !isDragOnLongPressEnabled) {
                    if (isDragEnabled) {
                        itemTouchHelper.startDrag((RecyclerView.ViewHolder) v.getTag(R.id.BaseQuickAdapter_viewholder_support));
                    }
                    return true;
                } else {
                    return false;
                }
            };
            mOnToggleViewLongClickListener = null;
        }
    }


    protected int getViewHolderPosition(RecyclerView.ViewHolder viewHolder) {
        return viewHolder.getAdapterPosition() - baseQuickAdapter.getHeaderLayoutCount();
    }

    /************************* Drag *************************/

    public void onItemDragStart(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragStart(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy, long superDuration) {
        if (mOnItemDragListener != null) {
            return mOnItemDragListener.getAnimationDuration(recyclerView, animationType, animateDx, animateDy, superDuration);
        }
        return superDuration;
    }

    public void onItemDragMoving(RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        int from = getViewHolderPosition(source);
        int to = getViewHolderPosition(target);
        if (inRange(from) && inRange(to)) {
            if (from < to) {
                for (int i = from; i < to; i++) {
                    Collections.swap(baseQuickAdapter.data, i, i + 1);
                }
            } else {
                for (int i = from; i < (to + 1); i++) {
                    Collections.swap(baseQuickAdapter.data, i, i - 1);
                }
            }
            baseQuickAdapter.notifyItemMoved(source.getAdapterPosition(), target.getAdapterPosition());
        }
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragMoving(source, from, target, to);
        }
    }

    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onItemDragEnd(viewHolder, getViewHolderPosition(viewHolder));
        }
    }

    /************************* Swipe *************************/

    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder) {
        if (isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.onItemSwipeStart(viewHolder, getViewHolderPosition(viewHolder));
            }
        }
    }

    public void onItemSwipeClear(RecyclerView.ViewHolder viewHolder) {
        if (isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.clearView(viewHolder, getViewHolderPosition(viewHolder));
            }
        }
    }

    public void onItemSwiped(RecyclerView.ViewHolder viewHolder) {
        int pos = getViewHolderPosition(viewHolder);
        if (inRange(pos)) {
            baseQuickAdapter.data.remove(pos);
            baseQuickAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            if (isSwipeEnabled) {
                if (mOnItemSwipeListener != null) {
                    mOnItemSwipeListener.onItemSwiped(viewHolder, pos);
                }
            }
        }
    }

    public void onItemSwiping(Canvas canvas, RecyclerView.ViewHolder viewHolder, Float dX, Float dY, Boolean isCurrentlyActive) {
        if (isSwipeEnabled) {
            if (mOnItemSwipeListener != null) {
                mOnItemSwipeListener.onItemSwipeMoving(canvas, viewHolder, dX, dY, isCurrentlyActive);
            }
        }
    }

    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (mOnItemDragListener != null) {
            mOnItemDragListener.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private boolean inRange(int position) {
        return position >= 0 && position < baseQuickAdapter.data.size();
    }

    /**
     * 设置监听
     *
     * @param onItemDragListener OnItemDragListener?
     */
    @Override
    public void setOnItemDragListener(OnItemDragListener onItemDragListener) {
        this.mOnItemDragListener = onItemDragListener;
    }

    @Override
    public void setOnItemSwipeListener(OnItemSwipeListener onItemSwipeListener) {
        this.mOnItemSwipeListener = onItemSwipeListener;
    }

    private static int NO_TOGGLE_VIEW = 0;
}
