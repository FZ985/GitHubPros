package api29.libs.adapter.base.binder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import api29.libs.adapter.base.BaseBinderAdapter;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * Binder 的基类
 */
public abstract class BaseItemBinder<T, VH extends BaseViewHolder> {

    private ArrayList<Integer> clickViewIds = new ArrayList<>();
    private ArrayList<Integer> longClickViewIds = new ArrayList<>();

    public BaseBinderAdapter _adapter;
    public Context _context;

    public BaseBinderAdapter getAdapter() {
        if (_adapter == null) {
            throw new IllegalArgumentException("This BaseItemBinder has not been attached to BaseBinderAdapter yet. You should not call the method before addItemBinder () ");
        }
        return _adapter;
    }

    public Context getContext() {
        if (_context == null) {
            throw new IllegalArgumentException("This $this has not been attached to BaseBinderAdapter yet. You should not call the method before onCreateViewHolder ().");
        }
        return _context;
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 在此处对设置item数据
     *
     * @param holder VH
     * @param data   T
     */
    public abstract void convert(VH holder, T data);

    /**
     * 使用局部刷新时候，会调用此方法
     *
     * @param holder   VH
     * @param data     T
     * @param payloads List<Any>
     */
    public void convert(VH holder, T data, List<Object> payloads) {
    }

    public boolean onFailedToRecycleView(VH holder) {
        return false;
    }

    /**
     * Called when a view created by this [BaseItemBinder] has been attached to a window.
     * 当此[BaseItemBinder]出现在屏幕上的时候，会调用此方法
     * <p>
     * This can be used as a reasonable signal that the view is about to be seen
     * by the user. If the [BaseItemBinder] previously freed any resources in
     * [onViewDetachedFromWindow][.onViewDetachedFromWindow]
     * those resources should be restored here.
     *
     * @param holder Holder of the view being attached
     */
    public void onViewAttachedToWindow(VH holder) {
    }

    /**
     * Called when a view created by this [BaseItemBinder] has been detached from its
     * window.
     * 当此[BaseItemBinder]从屏幕上移除的时候，会调用此方法
     * <p>
     * Becoming detached from the window is not necessarily a permanent condition;
     * the consumer of an Adapter's views may choose to cache views offscreen while they
     * are not visible, attaching and detaching them as appropriate.
     *
     * @param holder Holder of the view being detached
     */
    public void onViewDetachedFromWindow(VH holder) {
    }

    /**
     * item 若想实现条目点击事件则重写该方法
     *
     * @param holder   VH
     * @param data     T
     * @param position Int
     */
    public void onClick(VH holder, View view, T data, int position) {
    }

    /**
     * item 若想实现条目长按事件则重写该方法
     *
     * @param holder   VH
     * @param data     T
     * @param position Int
     * @return Boolean
     */
    public boolean onLongClick(VH holder, View view, T data, int position) {
        return false;
    }

    /**
     * item 子控件的点击事件
     *
     * @param holder   VH
     * @param view     View
     * @param data     T
     * @param position Int
     */
    public void onChildClick(VH holder, View view, T data, int position) {
    }

    /**
     * item 子控件的长按事件
     *
     * @param holder   VH
     * @param view     View
     * @param data     T
     * @param position Int
     * @return Boolean
     */
    public boolean onChildLongClick(VH holder, View view, T data, int position) {
        return false;
    }

    public void addChildClickViewIds(@IdRes int[] ids) {
        for (int id : ids) {
            if (!this.clickViewIds.contains(id))
                this.clickViewIds.add(id);
        }
    }

    public List<Integer> getChildClickViewIds() {
        return this.clickViewIds;
    }

    public void addChildLongClickViewIds(@IdRes int[] ids) {
        for (int id : ids) {
            if (!this.longClickViewIds.contains(id))
                this.longClickViewIds.add(id);
        }
    }

    public List<Integer> getChildLongClickViewIds() {
        return this.longClickViewIds;
    }
}