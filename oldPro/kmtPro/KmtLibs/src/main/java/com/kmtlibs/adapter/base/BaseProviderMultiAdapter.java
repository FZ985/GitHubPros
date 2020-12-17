package com.kmtlibs.adapter.base;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import com.kmtlibs.adapter.base.provider.BaseItemProvider;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

/**
 * 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的 ItemProvider 中。
 * 适用于以下情况：
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，只需要在[getItemType]中返回对应类型
 * 2、item 类型较多，在convert()中管理起来复杂
 * <p>
 * ViewHolder 由 [BaseItemProvider] 实现，并且每个[BaseItemProvider]可以拥有自己类型的ViewHolder类型。
 *
 * @ T data 数据类型
 * @constructor
 */
public abstract class BaseProviderMultiAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public BaseProviderMultiAdapter(List<T> data) {
        super(0, data);
    }

    private SparseArray<BaseItemProvider<T>> mItemProviders = new SparseArray<>();

    /**
     * 返回 item 类型
     *
     * @param data     List<T>
     * @param position Int
     * @return Int
     */
    protected abstract int getItemType(List<T> data, int position);

    /**
     * 必须通过此方法，添加 provider
     *
     * @param provider BaseItemProvider
     */
    public void addItemProvider(BaseItemProvider<T> provider) {
        provider.setAdapter(this);
        mItemProviders.put(provider.itemViewType(), provider);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseItemProvider<T> provider = getItemProvider(viewType);
        if (provider == null) {
            throw new IllegalArgumentException("ViewType: $viewType no such provider found，please use addItemProvider() first!");
        }
        provider.context = parent.getContext();
        BaseViewHolder holder = provider.onCreateViewHolder(parent, viewType);
        provider.onViewHolderCreated(holder, viewType);
        return holder;
    }

    @Override
    public int getDefItemViewType(int position) {
        return getItemType(data, position);
    }

    @Override
    public void convert(BaseViewHolder holder, T item) {
        getItemProvider(holder.getItemViewType()).convert(holder, item);
    }

    @Override
    public void convert(BaseViewHolder holder, T item, List<Object> payloads) {
        getItemProvider(holder.getItemViewType()).convert(holder, item, payloads);
    }

    @Override
    public void bindViewClickListener(BaseViewHolder viewHolder, int viewType) {
        super.bindViewClickListener(viewHolder, viewType);
        bindClick(viewHolder);
        bindChildClick(viewHolder, viewType);
    }

    /**
     * 通过 ViewType 获取 BaseItemProvider
     * 例如：如果ViewType经过特殊处理，可以重写此方法，获取正确的Provider
     * （比如 ViewType 通过位运算进行的组合的）
     *
     * @param viewType Int
     * @return BaseItemProvider
     */
    protected BaseItemProvider<T> getItemProvider(int viewType) {
        return mItemProviders.get(viewType);
    }

    protected void bindClick(BaseViewHolder viewHolder) {
        if (getOnItemClickListener() == null) {
            //如果没有设置点击监听，则回调给 itemProvider
            //Callback to itemProvider if no click listener is set
            viewHolder.itemView.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                position -= getHeaderLayoutCount();

                int itemViewType = viewHolder.getItemViewType();
                BaseItemProvider<T> provider = mItemProviders.get(itemViewType);
                provider.onClick(viewHolder, v, data.get(position), position);
            });
        }
        if (getOnItemLongClickListener() == null) {
            //如果没有设置长按监听，则回调给itemProvider
            // If you do not set a long press listener, callback to the itemProvider
            viewHolder.itemView.setOnLongClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return false;
                }
                position -= getHeaderLayoutCount();

                int itemViewType = viewHolder.getItemViewType();
                BaseItemProvider<T> provider = mItemProviders.get(itemViewType);
                return provider.onLongClick(viewHolder, v, data.get(position), position);
            });
        }
    }

    protected void bindChildClick(BaseViewHolder viewHolder, int viewType) {
        if (getOnItemChildClickListener() == null) {
            if (getItemProvider(viewType) == null) return;
            BaseItemProvider<T> provider = getItemProvider(viewType);
            List<Integer> ids = provider.getChildClickViewIds();
            for (int id : ids) {
                View it = viewHolder.itemView.findViewById(id);
                if (it != null) {
                    if (!it.isClickable()) {
                        it.setClickable(true);
                    }
                    it.setOnClickListener(v -> {
                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        position -= getHeaderLayoutCount();
                        provider.onChildClick(viewHolder, v, data.get(position), position);
                    });
                }
            }
        }
        if (getOnItemChildLongClickListener() == null) {
            if (getItemProvider(viewType) == null) return;
            BaseItemProvider<T> provider = getItemProvider(viewType);
            List<Integer> ids = provider.getChildLongClickViewIds();
            for (int id : ids) {
                View it = viewHolder.itemView.findViewById(id);
                if (it != null) {
                    if (!it.isLongClickable()) {
                        it.setLongClickable(true);
                    }
                    it.setOnLongClickListener(v -> {
                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return false;
                        }
                        position -= getHeaderLayoutCount();
                        return provider.onChildLongClick(viewHolder, v, data.get(position), position);
                    });
                }
            }
        }
    }
}