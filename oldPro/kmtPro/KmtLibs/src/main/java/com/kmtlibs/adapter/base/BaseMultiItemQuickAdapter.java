package com.kmtlibs.adapter.base;

import android.util.SparseIntArray;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.LayoutRes;
import com.kmtlibs.adapter.base.entity.MultiItemEntity;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

/**
 * 多类型布局，适用于类型较少，业务不复杂的场景，便于快速使用。
 * data[T]必须实现[MultiItemEntity]
 * <p>
 * 如果数据类无法实现[MultiItemEntity]，请使用[BaseDelegateMultiAdapter]
 * 如果类型较多，为了方便隔离各类型的业务逻辑，推荐使用[BaseProviderMultiAdapter]
 *
 * @ T  : MultiItemEntity
 * @ VH : BaseViewHolder
 * @constructor
 */
public abstract class BaseMultiItemQuickAdapter<T extends MultiItemEntity, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public BaseMultiItemQuickAdapter(List<T> data) {
        super(0, data);
    }

    private SparseIntArray layouts;
    private static final int DEFAULT_VIEW_TYPE = -0xff;
    public static final int TYPE_NOT_FOUND = -404;

    @Override
    protected int getDefItemViewType(int position) {
        T item = data.get(position);
        if (item != null) {
            return item.itemType();
        }
        return DEFAULT_VIEW_TYPE;
    }

    protected void setDefaultViewTypeLayout(@LayoutRes int layoutResId) {
        addItemType(DEFAULT_VIEW_TYPE, layoutResId);
    }

    @Override
    protected VH onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, getLayoutId(viewType));
    }

    private int getLayoutId(int viewType) {
        return layouts.get(viewType, TYPE_NOT_FOUND);
    }

    /**
     * 调用此方法，设置多布局
     *
     * @param type        Int
     * @param layoutResId Int
     */
    protected void addItemType(int type, @LayoutRes int layoutResId) {
        if (layouts == null) {
            layouts = new SparseIntArray();
        }
        layouts.put(type, layoutResId);
    }
}