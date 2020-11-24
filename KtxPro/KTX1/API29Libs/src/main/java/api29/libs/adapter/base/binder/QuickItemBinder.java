package api29.libs.adapter.base.binder;

import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import api29.libs.adapter.base.util.AdapterUtils;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * 使用布局 ID 快速构建 Binder
 *
 * @ T item 数据类型
 */
public abstract class QuickItemBinder<T> extends BaseItemBinder<T, BaseViewHolder> {

    @LayoutRes
    public abstract int getLayoutId();

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(AdapterUtils.getItemView(getLayoutId(), parent));
    }
}

