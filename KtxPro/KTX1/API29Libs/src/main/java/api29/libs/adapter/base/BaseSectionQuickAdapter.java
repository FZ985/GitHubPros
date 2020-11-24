package api29.libs.adapter.base;


import java.util.List;

import androidx.annotation.LayoutRes;
import api29.libs.adapter.base.entity.SectionEntity;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * 快速实现带头部的 Adapter，由于本质属于多布局，所以继承自[BaseMultiItemQuickAdapter]
 *
 * @ T : SectionEntity
 * @ VH : BaseViewHolder
 * @property sectionHeadResId Int
 * @constructor
 */
public abstract class BaseSectionQuickAdapter<T extends SectionEntity, VH extends BaseViewHolder> extends BaseMultiItemQuickAdapter<T, VH> {

    public BaseSectionQuickAdapter(@LayoutRes int sectionHeadResId, List<T> data) {
        super(data);
        addItemType(SectionEntity.HEADER_TYPE, sectionHeadResId);
    }

    public BaseSectionQuickAdapter(@LayoutRes int sectionHeadResId, @LayoutRes int layoutResId, List<T> data) {
        this(sectionHeadResId, data);
        setNormalLayout(layoutResId);
    }

    /**
     * 重写此处，设置 Header
     *
     * @param helper ViewHolder
     * @param item   data
     */
    protected abstract void convertHeader(VH helper, T item);

    /**
     * 重写此处，设置 Diff Header
     *
     * @param helper   VH
     * @param item     T?
     * @param payloads MutableList<Any>
     */
    protected void convertHeader(VH helper, T item, List<Object> payloads) {
    }

    /**
     * 如果 item 不是多布局，可以使用此方法快速设置 item layout
     * 如果需要多布局 item，请使用[addItemType]
     *
     * @param layoutResId Int
     */
    protected void setNormalLayout(@LayoutRes int layoutResId) {
        addItemType(SectionEntity.NORMAL_TYPE, layoutResId);
    }

    @Override
    public boolean isFixedViewType(int type) {
        return super.isFixedViewType(type) || type == SectionEntity.HEADER_TYPE;
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder.getItemViewType() == SectionEntity.HEADER_TYPE) {
//            setFullSpan(holder)
            convertHeader(holder, getItem(position - getHeaderLayoutCount()));
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        if (payloads == null|| payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        if (holder.getItemViewType() == SectionEntity.HEADER_TYPE) {
            convertHeader(holder, getItem(position - getHeaderLayoutCount()), payloads);
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }
}