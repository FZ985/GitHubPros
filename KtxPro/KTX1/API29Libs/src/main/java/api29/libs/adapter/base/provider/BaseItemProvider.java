package api29.libs.adapter.base.provider;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import api29.libs.adapter.base.BaseProviderMultiAdapter;
import api29.libs.adapter.base.util.AdapterUtils;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * [BaseProviderMultiAdapter] 的Provider基类
 *
 * @ T 数据类型
 */
public abstract class BaseItemProvider<T> {

    public Context context;

    private WeakReference<BaseProviderMultiAdapter<T>> weakAdapter = null;
    private List<Integer> clickViewIds = new ArrayList<>();
    private List<Integer> longClickViewIds = new ArrayList<>();

    public void setAdapter(BaseProviderMultiAdapter<T> adapter) {
        weakAdapter = new WeakReference(adapter);
    }

    public BaseProviderMultiAdapter<T> getAdapter() {
        if (weakAdapter != null) return weakAdapter.get();
        return null;
    }

    public abstract int itemViewType();

    @LayoutRes
    public abstract int layoutId();

    public abstract void convert(BaseViewHolder helper, T item);

    public void convert(BaseViewHolder helper, T item, List<Object> payloads) {
    }

    /**
     * （可选重写）创建 ViewHolder。
     * 默认实现返回[BaseViewHolder]，可重写返回自定义 ViewHolder
     *
     * @param parent
     */
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(AdapterUtils.getItemView(layoutId(), parent));
    }

    /**
     * （可选重写）ViewHolder创建完毕以后的回掉方法。
     *
     * @param viewHolder VH
     */
    public void onViewHolderCreated(BaseViewHolder viewHolder, int viewType) {
    }

    /**
     * item 若想实现条目点击事件则重写该方法
     *
     * @param holder   VH
     * @param data     T
     * @param position Int
     */
    public void onClick(BaseViewHolder holder, View view, T data, int position) {
    }

    /**
     * item 若想实现条目长按事件则重写该方法
     *
     * @param holder   VH
     * @param data     T
     * @param position Int
     * @return Boolean
     */
    public boolean onLongClick(BaseViewHolder holder, View view, T data, int position) {
        return false;
    }

    public void onChildClick(BaseViewHolder holder, View view, T data, int position) {
    }

    public boolean onChildLongClick(BaseViewHolder holder, View view, T data, int position) {
        return false;
    }

    public void addChildClickViewIds(@IdRes int[] ids) {
        if (clickViewIds == null) {
            clickViewIds = new ArrayList<>();
        }
        for (int id : ids) {
            this.clickViewIds.add(id);
        }
    }

    public List<Integer> getChildClickViewIds() {
        if (clickViewIds == null) {
            clickViewIds = new ArrayList<>();
        }
        return this.clickViewIds;
    }

    public void addChildLongClickViewIds(@IdRes int[] ids) {
        if (longClickViewIds == null) {
            longClickViewIds = new ArrayList<>();
        }
        for (int id : ids) {
            this.longClickViewIds.add(id);
        }

    }

    public List<Integer> getChildLongClickViewIds() {
        return this.longClickViewIds;
    }
}