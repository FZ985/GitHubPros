package api29.libs.adapter.base;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import api29.libs.adapter.base.binder.BaseItemBinder;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * 使用 Binder 来实现adapter，既可以实现单布局，也能实现多布局
 * 数据实体类也不存继承问题
 * <p>
 * 当有多种条目的时候，避免在convert()中做太多的业务逻辑，把逻辑放在对应的 BaseItemBinder 中。
 * 适用于以下情况：
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，默认情况下不需要实现 getItemType
 * 2、item 类型较多，在convert()中管理起来复杂
 * <p>
 * ViewHolder 由 [BaseItemBinder] 实现，并且每个[BaseItemBinder]可以拥有自己类型的ViewHolder类型。
 * <p>
 * 数据类型为Any
 */
public class BaseBinderAdapter<T extends Object> extends BaseQuickAdapter<T, BaseViewHolder> {

    public BaseBinderAdapter() {
        this(new ArrayList<>());
    }

    public BaseBinderAdapter(List<T> list) {
        super(0, list);
        setDiffCallback(new ItemCallback<T>());
    }

    /**
     * 用于存储每个 Binder 类型对应的 Diff
     */
    private HashMap<Class, DiffUtil.ItemCallback<Object>> classDiffMap = new HashMap<Class, DiffUtil.ItemCallback<Object>>();

    private HashMap<Class, Integer> mTypeMap = new HashMap<Class, Integer>();
    private SparseArray<BaseItemBinder<T, ?>> mBinderArray = new SparseArray<BaseItemBinder<T, ?>>();

    /**
     * 添加 ItemBinder
     */
    public BaseBinderAdapter<T> addItemBinder(Class<T> clazz, BaseItemBinder<T, ?> baseItemBinder) {
        return addItemBinder(clazz, baseItemBinder, null);
    }

    public BaseBinderAdapter<T> addItemBinder(Class<T> clazz, BaseItemBinder<T, ?> baseItemBinder, DiffUtil.ItemCallback<T> callback) {
        int itemType = mTypeMap.size() + 1;
        mTypeMap.put(clazz, itemType);
        mBinderArray.append(itemType, (BaseItemBinder<T, ?>) baseItemBinder);
        baseItemBinder._adapter = this;
        if (callback != null) {
            classDiffMap.put(clazz, (DiffUtil.ItemCallback<Object>) callback);
        }
        return this;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseItemBinder<T, BaseViewHolder> binder = getItemBinder(viewType);
        binder._context = context;
        return binder.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void convert(BaseViewHolder holder, T item) {
        getItemBinder(holder.getItemViewType()).convert(holder, item);
    }

    @Override
    protected void convert(BaseViewHolder holder, T item, List<Object> payloads) {
        super.convert(holder, item, payloads);
    }

    public BaseItemBinder<T, BaseViewHolder> getItemBinder(int viewType) {
        BaseItemBinder<T, ?> binder = mBinderArray.get(viewType);
        if (binder == null) {
            throw new IllegalArgumentException("getItemBinder: viewType '$viewType' no such Binder found，please use addItemBinder() first!");
        }
        return (BaseItemBinder<T, BaseViewHolder>) binder;
    }

    public BaseItemBinder<T, BaseViewHolder> getItemBinderOrNull(int viewType) {
        BaseItemBinder<T, ?> binder = mBinderArray.get(viewType);
        return (BaseItemBinder<T, BaseViewHolder>) binder;
    }

    @Override
    protected int getDefItemViewType(int position) {
        return findViewType(data.get(position).getClass());
    }

    @Override
    protected void bindViewClickListener(BaseViewHolder viewHolder, int viewType) {
        super.bindViewClickListener(viewHolder, viewType);
        bindClick(viewHolder);
        bindChildClick(viewHolder, viewType);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (getItemBinderOrNull(holder.getItemViewType()) != null) {
            getItemBinderOrNull(holder.getItemViewType()).onViewAttachedToWindow(holder);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (getItemBinderOrNull(holder.getItemViewType()) != null) {
            getItemBinderOrNull(holder.getItemViewType()).onViewDetachedFromWindow(holder);
        }
    }

    @Override
    public boolean onFailedToRecycleView(@NonNull BaseViewHolder holder) {
        return getItemBinderOrNull(holder.getItemViewType()) != null && getItemBinderOrNull(holder.getItemViewType()).onFailedToRecycleView(holder);
    }

    private int findViewType(Class clazz) {
        if (!mTypeMap.containsKey(clazz)) {
            throw new IllegalArgumentException("findViewType: ViewType: $clazz Not Find!");
        }
        return mTypeMap.get(clazz);
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
                BaseItemBinder<T, BaseViewHolder> binder = getItemBinder(itemViewType);
                binder.onClick(viewHolder, v, data.get(position), position);
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
                BaseItemBinder<T, BaseViewHolder> binder = getItemBinder(itemViewType);
                return binder.onLongClick(viewHolder, v, data.get(position), position);
            });
        }
    }

    protected void bindChildClick(BaseViewHolder viewHolder, int viewType) {
        if (getOnItemChildClickListener() == null) {
            BaseItemBinder<T, BaseViewHolder> provider = getItemBinder(viewType);
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
            BaseItemBinder<T, BaseViewHolder> provider = getItemBinder(viewType);
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

    /**
     * Diff Callback
     */
    private class ItemCallback<T> extends DiffUtil.ItemCallback<T> {

        @Override
        public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                DiffUtil.ItemCallback<Object> it = classDiffMap.get(oldItem.getClass());
                if (it != null)
                    return it.areItemsTheSame(oldItem, newItem);
            }
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                DiffUtil.ItemCallback<Object> it = classDiffMap.get(oldItem.getClass());
                if (it != null)
                    return it.areContentsTheSame(oldItem, newItem);
            }
            return true;
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull T oldItem, @NonNull T newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                if (classDiffMap.get(oldItem.getClass()) != null)
                    return classDiffMap.get(oldItem.getClass()).getChangePayload(oldItem, newItem);
            }
            return null;
        }
    }
}