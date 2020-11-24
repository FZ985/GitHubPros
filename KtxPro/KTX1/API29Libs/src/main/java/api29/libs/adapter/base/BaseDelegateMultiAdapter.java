package api29.libs.adapter.base;

import android.view.ViewGroup;

import java.util.List;

import api29.libs.adapter.base.delegate.BaseMultiTypeDelegate;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * 多类型布局，通过代理类的方式，返回布局 id 和 item 类型；
 * 适用于:
 * 1、实体类不方便扩展，此Adapter的数据类型可以是任意类型，只需要在[BaseMultiTypeDelegate.getItemType]中返回对应类型
 * 2、item 类型较少
 * 如果类型较多，为了方便隔离各类型的业务逻辑，推荐使用[BaseBinderAdapter]
 *
 * @ T
 * @ VH : BaseViewHolder
 * @property mMultiTypeDelegate BaseMultiTypeDelegate<T>?
 * @constructor
 */
public abstract class BaseDelegateMultiAdapter<T, VH extends BaseViewHolder> extends BaseQuickAdapter<T, VH> {
    public BaseDelegateMultiAdapter(List<T> data) {
        super(0, data);
    }

    private BaseMultiTypeDelegate<T> mMultiTypeDelegate = null;

    /**
     * 通过此方法设置代理
     *
     * @param multiTypeDelegate BaseMultiTypeDelegate<T>
     */
    public void setMultiTypeDelegate(BaseMultiTypeDelegate<T> multiTypeDelegate) {
        this.mMultiTypeDelegate = multiTypeDelegate;
    }

    public BaseMultiTypeDelegate<T> getMultiTypeDelegate() {
        return mMultiTypeDelegate;
    }

    @Override
    public VH onCreateDefViewHolder(ViewGroup parent, int viewType) {
        BaseMultiTypeDelegate<T> delegate = getMultiTypeDelegate();
        if (delegate == null){
            throw  new IllegalArgumentException("Please use setMultiTypeDelegate first!");
        }
        int layoutId = delegate.getLayoutId(viewType);
        return createBaseViewHolder(parent, layoutId);
    }

    @Override
    public int getDefItemViewType(int position) {
        BaseMultiTypeDelegate<T> delegate = getMultiTypeDelegate();
        if (delegate == null){
            throw  new IllegalArgumentException("Please use setMultiTypeDelegate first!");
        }
        return delegate.getItemType(data, position);
    }
}