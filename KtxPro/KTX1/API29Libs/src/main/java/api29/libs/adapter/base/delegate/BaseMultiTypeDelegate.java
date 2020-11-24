package api29.libs.adapter.base.delegate;

import android.util.SparseIntArray;

import java.util.List;

import androidx.annotation.LayoutRes;

/**
 * help you to achieve multi type easily
 * <p>
 * <p>
 * Created by tysheng
 * Date: 2017/4/6 08:41.
 * Email: tyshengsx@gmail.com
 * <p>
 * more information: https://github.com/CymChad/BaseRecyclerViewAdapterHelper/issues/968
 */

public abstract class BaseMultiTypeDelegate<T> {

    private SparseIntArray layouts;
    private boolean autoMode;
    private boolean selfMode;

    public BaseMultiTypeDelegate() {
        this.layouts = new SparseIntArray();
    }

    /**
     * get the item type from specific entity.
     *
     * @param data     entity
     * @param position
     * @return item type
     */
    public abstract int getItemType(List<T> data, int position);

    public int getLayoutId(int viewType) {
        int layoutResId = layouts.get(viewType);
        if (layoutResId == 0) {
            throw new IllegalArgumentException("ViewType: viewType found layoutResId，please use registerItemType() first!");
        }
        return layoutResId;
    }

    private void registerItemType(int type, @LayoutRes int layoutResId) {
        this.layouts.put(type, layoutResId);
    }

    /**
     * auto increase type vale, start from 0.
     *
     * @param layoutResIds layout id arrays
     * @return MultiTypeDelegate
     */
    public BaseMultiTypeDelegate<T> addItemTypeAutoIncrease(@LayoutRes int[] layoutResIds) {
        autoMode = true;
        checkMode(selfMode);
        for (int i = 0; i < layoutResIds.length; i++) {
            registerItemType(i, layoutResIds[i]);
        }
        return this;
    }

    /**
     * set your own type one by one.
     *
     * @param type        type value
     * @param layoutResId layout id
     * @return MultiTypeDelegate
     */
    public BaseMultiTypeDelegate<T> addItemType(int type, @LayoutRes int layoutResId) {
        selfMode = true;
        checkMode(autoMode);
        registerItemType(type, layoutResId);
        return this;
    }

    private void checkMode(boolean mode) {
        if (mode) {
            throw new IllegalArgumentException("Don't mess two register mode");
        }
    }

}
