package com.kmtlibs.adapter.base;

import android.animation.Animator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kmtlibs.adapter.base.animation.AlphaInAnimation;
import com.kmtlibs.adapter.base.animation.BaseAnimation;
import com.kmtlibs.adapter.base.animation.ScaleInAnimation;
import com.kmtlibs.adapter.base.animation.SlideInBottomAnimation;
import com.kmtlibs.adapter.base.animation.SlideInLeftAnimation;
import com.kmtlibs.adapter.base.animation.SlideInRightAnimation;
import com.kmtlibs.adapter.base.diff.BrvahAsyncDiffer;
import com.kmtlibs.adapter.base.diff.BrvahAsyncDifferConfig;
import com.kmtlibs.adapter.base.diff.BrvahListUpdateCallback;
import com.kmtlibs.adapter.base.listener.BaseListenerImp;
import com.kmtlibs.adapter.base.listener.GridSpanSizeLookup;
import com.kmtlibs.adapter.base.listener.OnItemChildClickListener;
import com.kmtlibs.adapter.base.listener.OnItemChildLongClickListener;
import com.kmtlibs.adapter.base.listener.OnItemClickListener;
import com.kmtlibs.adapter.base.listener.OnItemLongClickListener;
import com.kmtlibs.adapter.base.module.BaseDraggableModule;
import com.kmtlibs.adapter.base.module.BaseLoadMoreModule;
import com.kmtlibs.adapter.base.module.BaseUpFetchModule;
import com.kmtlibs.adapter.base.util.AdapterUtils;
import com.kmtlibs.adapter.base.viewholder.BaseViewHolder;

import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Create by JFZ
 * date: 2020-05-07 9:58
 **/
public abstract class BaseQuickAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> implements BaseListenerImp {
    @LayoutRes
    private int layoutResId;

    public BaseQuickAdapter(@LayoutRes int layoutResId, List<T> datas) {
        this.data = datas == null ? new ArrayList<T>() : datas;
        if (layoutResId != 0) {
            this.layoutResId = layoutResId;
        }
        init();
    }

    public BaseQuickAdapter(@LayoutRes int layoutResId) {
        this(layoutResId, new ArrayList<>());
    }

    /**
     * 获取模块
     */
    private class BaseQuickAdapterModuleImp {
        /**
         * 重写此方法，返回自定义模块
         *
         * @param baseQuickAdapter BaseQuickAdapter<*, *>
         * @return BaseLoadMoreModule
         */
        BaseLoadMoreModule addLoadMoreModule(BaseQuickAdapter baseQuickAdapter) {
            return new BaseLoadMoreModule(baseQuickAdapter);
        }

        /**
         * 重写此方法，返回自定义模块
         *
         * @param baseQuickAdapter BaseQuickAdapter<*, *>
         * @return BaseUpFetchModule
         */
        BaseUpFetchModule addUpFetchModule(BaseQuickAdapter baseQuickAdapter) {
            return new BaseUpFetchModule(baseQuickAdapter);
        }

        /**
         * 重写此方法，返回自定义模块
         *
         * @param baseQuickAdapter BaseQuickAdapter<*, *>
         * @return BaseExpandableModule
         */
        BaseDraggableModule addDraggableModule(BaseQuickAdapter baseQuickAdapter) {
            return new BaseDraggableModule(baseQuickAdapter);
        }
    }

    public static final int HEADER_VIEW = 0x10000111;
    public static final int LOAD_MORE_VIEW = 0x10000222;
    public static final int FOOTER_VIEW = 0x10000333;
    public static final int EMPTY_VIEW = 0x10000555;

    /***************************** Public property settings *************************************/
    /**
     * data, Only allowed to get.
     * 数据, 只允许 get。
     */
    public List<T> data = new ArrayList<>();


    /**
     * 当显示空布局时，是否显示 Header
     */
    private boolean headerWithEmptyEnable = false;

    public void setHeaderWithEmptyEnable(boolean headerWithEmptyEnable) {
        this.headerWithEmptyEnable = headerWithEmptyEnable;
    }

    /**
     * 当显示空布局时，是否显示 Foot
     */
    private boolean footerWithEmptyEnable = false;

    public void setFooterWithEmptyEnable(boolean footerWithEmptyEnable) {
        this.footerWithEmptyEnable = footerWithEmptyEnable;
    }

    /**
     * 是否使用空布局
     */
    private boolean isUseEmpty = true;

    public void setUseEmpty(boolean useEmpty) {
        isUseEmpty = useEmpty;
    }

    public List<T> getData() {
        return data;
    }

    /**
     * if asFlow is true, footer/header will arrange like normal item view.
     * only works when use [GridLayoutManager],and it will ignore span size.
     */
    private boolean headerViewAsFlow = false;
    private boolean footerViewAsFlow = false;

    public void setHeaderViewAsFlow(boolean headerViewAsFlow) {
        this.headerViewAsFlow = headerViewAsFlow;
    }

    public void setFooterViewAsFlow(boolean footerViewAsFlow) {
        this.footerViewAsFlow = footerViewAsFlow;
    }

    /**
     * 是否打开动画
     */
    private boolean animationEnable = false;

    public void setAnimationEnable(boolean animationEnable) {
        this.animationEnable = animationEnable;
    }

    /**
     * 动画是否仅第一次执行
     */
    private boolean isAnimationFirstOnly = true;

    public void setAnimationFirstOnly(boolean animationFirstOnly) {
        isAnimationFirstOnly = animationFirstOnly;
    }

    /**
     * 设置自定义动画
     */
    private BaseAnimation adapterAnimation = null;

    public void setAdapterAnimation(BaseAnimation adapterAnimation) {
        animationEnable = true;
        this.adapterAnimation = adapterAnimation;
    }

    /**
     * 加载更多模块
     */
    public BaseLoadMoreModule getLoadMoreModule() {
        return mLoadMoreModule;
    }

    /**
     * 向上加载模块
     */
    public BaseUpFetchModule getUpFetchModule() {
        return mUpFetchModule;
    }

    /**
     * 拖拽模块
     */
    public BaseDraggableModule getDraggableModule() {
        return mDraggableModule;
    }

    /********************************* Private property *****************************************/
    private BrvahAsyncDiffer<T> mDiffHelper = null;

    private LinearLayout mHeaderLayout;
    private LinearLayout mFooterLayout;
    private FrameLayout mEmptyLayout;

    private int mLastPosition = -1;

    private GridSpanSizeLookup mSpanSizeLookup = null;
    private OnItemClickListener mOnItemClickListener = null;
    private OnItemLongClickListener mOnItemLongClickListener = null;
    private OnItemChildClickListener mOnItemChildClickListener = null;
    private OnItemChildLongClickListener mOnItemChildLongClickListener = null;
    private BaseLoadMoreModule mLoadMoreModule = null;
    private BaseUpFetchModule mUpFetchModule = null;
    private BaseDraggableModule mDraggableModule = null;

    protected Context context;
    private WeakReference<RecyclerView> weakRecyclerView;


    /******************************* RecyclerView Method ****************************************/

    private void init() {
        checkModule();
    }

    /**
     * 检查模块
     */
    private void checkModule() {
        BaseQuickAdapterModuleImp imp = new BaseQuickAdapterModuleImp();
        mLoadMoreModule = imp.addLoadMoreModule(this);
        mUpFetchModule = imp.addUpFetchModule(this);
        mDraggableModule = imp.addDraggableModule(this);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     * <p>
     * 实现此方法，并使用 helper 完成 item 视图的操作
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(VH holder, T item);

    /**
     * Optional implementation this method and use the helper to adapt the view to the given item.
     * If use [payloads], will perform this method, Please implement this method for partial refresh.
     * If use [RecyclerView.Adapter.notifyItemChanged(Int, Object)] with payload,
     * Will execute this method.
     * <p>
     * 可选实现，如果你是用了[payloads]刷新item，请实现此方法，进行局部刷新
     *
     * @param holder   A fully initialized holder.
     * @param item     The item that needs to be displayed.
     * @param payloads payload info.
     */
    protected void convert(VH holder, T item, List<Object> payloads) {
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH baseViewHolder = null;
        switch (viewType) {
            case LOAD_MORE_VIEW:
                if (getLoadMoreModule() != null) {
                    View view = getLoadMoreModule().loadMoreView.getRootView(parent);
                    baseViewHolder = createBaseViewHolder(view);
                    getLoadMoreModule().setupViewHolder(baseViewHolder);
                }
                break;
            case HEADER_VIEW:
                ViewParent headerLayoutVp = mHeaderLayout.getParent();
                if (headerLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) headerLayoutVp).removeView(mHeaderLayout);
                }
                baseViewHolder = createBaseViewHolder(mHeaderLayout);
                break;
            case EMPTY_VIEW:
                ViewParent emptyLayoutVp = mEmptyLayout.getParent();
                if (emptyLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) emptyLayoutVp).removeView(mEmptyLayout);
                }
                baseViewHolder = createBaseViewHolder(mEmptyLayout);
                break;
            case FOOTER_VIEW:
                ViewParent footerLayoutVp = mFooterLayout.getParent();
                if (footerLayoutVp instanceof ViewGroup) {
                    ((ViewGroup) footerLayoutVp).removeView(mFooterLayout);
                }
                baseViewHolder = createBaseViewHolder(mFooterLayout);
                break;
            default:
                VH viewHolder = onCreateDefViewHolder(parent, viewType);
                bindViewClickListener(viewHolder, viewType);
                if (getDraggableModule() != null) {
                    getDraggableModule().initView(viewHolder);
                }
                onItemViewHolderCreated(viewHolder, viewType);
                baseViewHolder = viewHolder;
                break;
        }
        return baseViewHolder;
    }

    /**
     * Don't override this method. If need, please override [getItemCount]
     * 不要重写此方法，如果有需要，请重写[getDefItemViewType]
     *
     * @return Int
     */
    @Override
    public int getItemCount() {
        if (hasEmptyView()) {
            int count = 1;
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                count++;
            }
            if (footerWithEmptyEnable && hasFooterLayout()) {
                count++;
            }
            return count;
        } else {
            int loadMoreCount = (getLoadMoreModule() != null && getLoadMoreModule().hasLoadMoreView()) ? 1 : 0;
            return getHeaderLayoutCount() + getDefItemCount() + getFooterLayoutCount() + loadMoreCount;
        }
    }

    /**
     * Don't override this method. If need, please override [getDefItemViewType]
     * 不要重写此方法，如果有需要，请重写[getDefItemViewType]
     *
     * @param position Int
     * @return Int
     */
    @Override
    public int getItemViewType(int position) {
        if (hasEmptyView()) {
            boolean header = headerWithEmptyEnable && hasHeaderLayout();
            switch (position) {
                case 0:
                    if (header) {
                        return HEADER_VIEW;
                    } else {
                        return EMPTY_VIEW;
                    }
                case 1:
                    if (header) {
                        return EMPTY_VIEW;
                    } else {
                        return FOOTER_VIEW;
                    }
                case 2:
                    return FOOTER_VIEW;
                default:
                    return EMPTY_VIEW;
            }
        }
        boolean hasHeader = hasHeaderLayout();
        if (hasHeader && position == 0) {
            return HEADER_VIEW;
        } else {
            int adjPosition = position;
            if (hasHeader) {
                adjPosition = position - 1;
            }
            int dataSize = data.size();
            if (adjPosition < dataSize) {
                return getDefItemViewType(adjPosition);
            } else {
                adjPosition -= dataSize;
                int numFooters = 0;
                if (hasFooterLayout()) {
                    numFooters = 1;
                }
                if (adjPosition < numFooters) {
                    return FOOTER_VIEW;
                } else {
                    return LOAD_MORE_VIEW;
                }
            }
        }
    }

    public void onBindViewHolder(VH holder, int position) {
        //Add up fetch logic, almost like load more, but simpler.
        if (getUpFetchModule() != null) {
            getUpFetchModule().autoUpFetch(position);
        }
        //Do not move position, need to change before LoadMoreView binding
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().autoLoadMore(position);
        }
        switch (holder.getItemViewType()) {
            case LOAD_MORE_VIEW:
                if (getLoadMoreModule() != null) {
                    getLoadMoreModule().loadMoreView.convert(holder, position, getLoadMoreModule().loadMoreStatus);
                }
                break;
            case HEADER_VIEW:
            case EMPTY_VIEW:
            case FOOTER_VIEW:
                break;
            default:
                convert(holder, getItem(position - getHeaderLayoutCount()));
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        //Add up fetch logic, almost like load more, but simpler.
        if (getUpFetchModule() != null) {
            getUpFetchModule().autoUpFetch(position);
        }
        //Do not move position, need to change before LoadMoreView binding
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().autoLoadMore(position);
        }
        switch (holder.getItemViewType()) {
            case LOAD_MORE_VIEW:
                if (getLoadMoreModule() != null) {
                    getLoadMoreModule().loadMoreView.convert(holder, position, getLoadMoreModule().loadMoreStatus);
                }
                break;
            case HEADER_VIEW:
            case EMPTY_VIEW:
            case FOOTER_VIEW:
                break;
            default:
                convert(holder, getItem(position - getHeaderLayoutCount()), payloads);
                break;
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Called when a view created by this holder has been attached to a window.
     * simple to solve item will layout using all
     * [setFullSpan]
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
        int type = holder.getItemViewType();
        if (isFixedViewType(type)) {
            setFullSpan(holder);
        } else {
            addAnimation(holder);
        }
    }

    public WeakReference<RecyclerView> getWeakRecyclerView() {
        return weakRecyclerView;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        weakRecyclerView = new WeakReference(recyclerView);
        this.context = recyclerView.getContext();
        if (getDraggableModule() != null) {
            getDraggableModule().attachToRecyclerView(recyclerView);
        }
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager.SpanSizeLookup defSpanSizeLookup = ((GridLayoutManager) manager).getSpanSizeLookup();
            ((GridLayoutManager) manager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = getItemViewType(position);
                    if (type == HEADER_VIEW && headerViewAsFlow) {
                        return 1;
                    }
                    if (type == FOOTER_VIEW && footerViewAsFlow) {
                        return 1;
                    }
                    if (mSpanSizeLookup == null) {
                        if (isFixedViewType(type)) {
                            return ((GridLayoutManager) manager).getSpanCount();
                        } else return defSpanSizeLookup.getSpanSize(position);
                    } else {
                        if (isFixedViewType(type))
                            return ((GridLayoutManager) manager).getSpanCount();
                        else
                            return mSpanSizeLookup.getSpanSize((GridLayoutManager) manager, type, position - getHeaderLayoutCount());
                    }
                }
            });
        }
    }

    protected boolean isFixedViewType(int type) {
        return type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOAD_MORE_VIEW;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    public T getItem(@IntRange(from = 0) int position) {
        return data.get(position);
    }

    /**
     * 如果返回 -1，表示不存在
     *
     * @param item T?
     * @return Int
     */
    public int getItemPosition(T item) {
        return (item != null && data != null && !data.isEmpty()) ? data.indexOf(item) : -1;
    }

    /**
     * 用于保存需要设置点击事件的 item
     */
    private LinkedHashSet<Integer> childClickViewIds = new LinkedHashSet<Integer>();

    private LinkedHashSet<Integer> getChildClickViewIds() {
        return childClickViewIds;
    }

    /**
     * 设置需要点击事件的子view
     *
     * @param viewIds IntArray
     */
    protected void addChildClickViewIds(@IdRes int[] viewIds) {
        for (int viewId : viewIds) {
            childClickViewIds.add(viewId);
        }
    }

    /**
     * 用于保存需要设置长按点击事件的 item
     */
    private LinkedHashSet<Integer> childLongClickViewIds = new LinkedHashSet<Integer>();

    private LinkedHashSet<Integer> getChildLongClickViewIds() {
        return childLongClickViewIds;
    }

    /**
     * 设置需要长按点击事件的子view
     *
     * @param viewIds IntArray
     */
    protected void addChildLongClickViewIds(@IdRes int[] viewIds) {
        for (int viewId : viewIds) {
            childLongClickViewIds.add(viewId);
        }
    }

    /**
     * 绑定 item 点击事件
     *
     * @param viewHolder VH
     */
    protected void bindViewClickListener(VH viewHolder, int viewType) {
        if (viewHolder == null) return;
        if (getOnItemClickListener() != null) {
            viewHolder.itemView.setOnClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return;
                }
                position -= getHeaderLayoutCount();
                setOnItemClick(v, position);
            });
        }
        if (mOnItemLongClickListener != null) {
            viewHolder.itemView.setOnLongClickListener(v -> {
                int position = viewHolder.getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return false;
                }
                position -= getHeaderLayoutCount();
                return setOnItemLongClick(v, position);
            });
        }

        if (mOnItemChildClickListener != null) {
            for (int id : getChildClickViewIds()) {
                View childView = viewHolder.itemView.findViewById(id);
                if (childView != null) {
                    if (!childView.isClickable()) {
                        childView.setClickable(true);
                    }
                    childView.setOnClickListener(v -> {
                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }
                        position -= getHeaderLayoutCount();
                        setOnItemChildClick(v, position);
                    });
                }
            }
        }
        if (mOnItemChildLongClickListener != null) {
            for (int id : getChildLongClickViewIds()) {
                View childView = viewHolder.itemView.findViewById(id);
                if (childView != null) {
                    if (!childView.isLongClickable()) {
                        childView.setLongClickable(true);
                    }
                    childView.setOnLongClickListener(v -> {
                        int position = viewHolder.getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return false;
                        }
                        position -= getHeaderLayoutCount();
                        return setOnItemChildLongClick(v, position);
                    });
                }
            }
        }
    }

    /**
     * override this method if you want to override click event logic
     * <p>
     * 如果你想重新实现 item 点击事件逻辑，请重写此方法
     *
     * @param v
     * @param position
     */
    protected void setOnItemClick(View v, int position) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(this, v, position);
        }
    }

    /**
     * override this method if you want to override longClick event logic
     * <p>
     * 如果你想重新实现 item 长按事件逻辑，请重写此方法
     *
     * @param v
     * @param position
     * @return
     */
    protected boolean setOnItemLongClick(View v, int position) {
        return (mOnItemLongClickListener != null) && mOnItemLongClickListener.onItemLongClick(this, v, position);
    }

    protected void setOnItemChildClick(View v, int position) {
        if (mOnItemChildClickListener != null) {
            mOnItemChildClickListener.onItemChildClick(this, v, position);
        }
    }

    protected boolean setOnItemChildLongClick(View v, int position) {
        return (mOnItemChildLongClickListener != null) && mOnItemChildLongClickListener.onItemChildLongClick(this, v, position);
    }

    /**
     * （可选重写）当 item 的 ViewHolder创建完毕后，执行此方法。
     * 可在此对 ViewHolder 进行处理，例如进行 DataBinding 绑定 view
     *
     * @param viewHolder VH
     * @param viewType   Int
     */
    protected void onItemViewHolderCreated(VH viewHolder, int viewType) {
    }

    /**
     * Override this method and return your data size.
     * 重写此方法，返回你的数据数量。
     */
    protected int getDefItemCount() {
        return data.size();
    }

    /**
     * Override this method and return your ViewType.
     * 重写此方法，返回你的ViewType。
     */
    protected int getDefItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Override this method and return your ViewHolder.
     * 重写此方法，返回你的ViewHolder。
     */
    protected VH onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return createBaseViewHolder(parent, layoutResId);
    }

    protected VH createBaseViewHolder(ViewGroup parent, @LayoutRes int layoutResId) {
        return createBaseViewHolder(AdapterUtils.getItemView(layoutResId, parent));
    }

    /**
     * 创建 ViewHolder。可以重写
     *
     * @param view View
     * @return VH
     */
    @SuppressWarnings("unchecked")
    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH k;
        // 泛型擦除会导致z为null
        if (z == null) {
            k = (VH) new BaseViewHolder(view);
        } else {
            k = createBaseGenericKInstance(z, view);
        }
        return k != null ? k : (VH) new BaseViewHolder(view);
    }

    /**
     * get generic parameter VH
     *
     * @param z
     * @return
     */
    private Class getInstancedGenericKClass(Class z) {
        try {
            Type type = z.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                for (Type temp : types) {
                    if (temp instanceof Class) {
                        if (BaseViewHolder.class.isAssignableFrom((Class<?>) temp)) {
                            return (Class) temp;
                        }
                    } else if (temp instanceof ParameterizedType) {
                        Type rawType = ((ParameterizedType) temp).getRawType();
                        if (rawType instanceof Class<?> && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                            return (Class) rawType;
                        }
                    }
                }
            }
        } catch (java.lang.reflect.GenericSignatureFormatError e) {
            e.printStackTrace();
        } catch (TypeNotPresentException e) {
            e.printStackTrace();
        } catch (java.lang.reflect.MalformedParameterizedTypeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * try to create Generic VH instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private VH createBaseGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * When set to true, the item will layout using all span area. That means, if orientation
     * is vertical, the view will have full width; if orientation is horizontal, the view will
     * have full height.
     * if the hold view use StaggeredGridLayoutManager they should using all span area
     *
     * @param holder True if this item should traverse all spans.
     */
    protected void setFullSpan(RecyclerView.ViewHolder holder) {
        if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder
                    .itemView.getLayoutParams();
            params.setFullSpan(true);
        }
    }

    /**
     * get the specific view by position,e.g. getViewByPosition(2, R.id.textView)
     * <p>
     * bind [RecyclerView.setAdapter] before use!
     */
    public View getViewByPosition(int position, @IdRes int viewId) {
        RecyclerView recyclerView = weakRecyclerView.get();
        if (recyclerView == null) return null;
        BaseViewHolder viewHolder = (BaseViewHolder) recyclerView.findViewHolderForLayoutPosition(position);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.getViewOrNull(viewId);
    }

    /********************************************************************************************/
    /********************************* HeaderView Method ****************************************/
    /********************************************************************************************/
    public int addHeaderView(View view) {
        return addHeaderView(view, -1, LinearLayout.VERTICAL);
    }

    public int addHeaderView(View view, int index, int orientation) {
        if (mHeaderLayout == null) {
            mHeaderLayout = new LinearLayout(view.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mHeaderLayout.setOrientation(LinearLayout.VERTICAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mHeaderLayout.setOrientation(LinearLayout.HORIZONTAL);
                mHeaderLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }

        final int childCount = mHeaderLayout.getChildCount();
        int mIndex = index;
        if (index < 0 || index > childCount) {
            mIndex = childCount;
        }
        mHeaderLayout.addView(view, mIndex);
        if (mHeaderLayout.getChildCount() == 1) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return mIndex;
    }

    public int setHeaderView(View view) {
        return setHeaderView(view, 0, LinearLayout.VERTICAL);
    }

    public int setHeaderView(View view, int index, int orientation) {
        if (mHeaderLayout == null || mHeaderLayout.getChildCount() <= index) {
            return addHeaderView(view, index, orientation);
        } else {
            mHeaderLayout.removeViewAt(index);
            mHeaderLayout.addView(view, index);
            return index;
        }
    }

    /**
     * 是否有 HeaderLayout
     *
     * @return Boolean
     */
    private boolean hasHeaderLayout() {
        if (mHeaderLayout != null && mHeaderLayout.getChildCount() > 0) {
            return true;
        }
        return false;
    }

    public void removeHeaderView(View header) {
        if (!hasHeaderLayout()) return;
        mHeaderLayout.removeView(header);
        if (mHeaderLayout.getChildCount() == 0) {
            int position = getHeaderViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllHeaderView() {
        if (!hasHeaderLayout()) return;
        mHeaderLayout.removeAllViews();
        int position = getHeaderViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    private int getHeaderViewPosition() {
        if (hasEmptyView()) {
            if (headerWithEmptyEnable) {
                return 0;
            }
        } else {
            return 0;
        }
        return -1;
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getHeaderLayoutCount() {
        if (hasHeaderLayout()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 获取头布局
     */
    private LinearLayout headerLayout;

    public LinearLayout getHeaderLayout() {
        if (mHeaderLayout != null)
            return headerLayout;
        else return null;
    }

    /********************************************************************************************/
    /********************************* FooterView Method ****************************************/
    /********************************************************************************************/
    public int addFooterView(View view) {
        return addFooterView(view, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View view, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(view.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new RecyclerView.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        int mIndex = index;
        if (index < 0 || index > childCount) {
            mIndex = childCount;
        }
        mFooterLayout.addView(view, index);
        if (mFooterLayout.getChildCount() == 1) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemInserted(position);
            }
        }
        return mIndex;
    }

    public int setFooterView(View view) {
        return setFooterView(view, 0, LinearLayout.VERTICAL);
    }

    public int setFooterView(View view, int index, int orientation) {
        if (mFooterLayout == null || mFooterLayout.getChildCount() <= index) {
            return addFooterView(view, index, orientation);
        } else {
            mFooterLayout.removeViewAt(index);
            mFooterLayout.addView(view, index);
            return index;
        }
    }

    public void removeFooterView(View footer) {
        if (!hasFooterLayout()) return;
        mFooterLayout.removeView(footer);
        if (mFooterLayout.getChildCount() == 0) {
            int position = getFooterViewPosition();
            if (position != -1) {
                notifyItemRemoved(position);
            }
        }
    }

    public void removeAllFooterView() {
        if (!hasFooterLayout()) return;
        mFooterLayout.removeAllViews();
        int position = getFooterViewPosition();
        if (position != -1) {
            notifyItemRemoved(position);
        }
    }

    private boolean hasFooterLayout() {
        if (mFooterLayout != null && mFooterLayout.getChildCount() > 0) {
            return true;
        }
        return false;
    }

    private int getFooterViewPosition() {
        if (hasEmptyView()) {
            int position = 1;
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++;
            }
            if (footerWithEmptyEnable) {
                return position;
            }
        } else {
            return getHeaderLayoutCount() + data.size();
        }
        return -1;
    }

    /**
     * if addHeaderView will be return 1, if not will be return 0
     */
    public int getFooterLayoutCount() {
        return hasFooterLayout() ? 1 : 0;
    }

    /**
     * 获取脚布局
     *
     * @return LinearLayout
     */
    public LinearLayout getFooterLayout() {
        return mFooterLayout;
    }

    /********************************************************************************************/
    /********************************** EmptyView Method ****************************************/
    /********************************************************************************************/
    /**
     * 设置空布局视图，注意：[data]必须为空数组
     *
     * @param emptyView View
     */
    public void setEmptyView(View emptyView) {
        int oldItemCount = getItemCount();
        boolean insert = false;
        if (mEmptyLayout == null) {
            mEmptyLayout = new FrameLayout(emptyView.getContext());
            final RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = emptyView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mEmptyLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mEmptyLayout.removeAllViews();
        mEmptyLayout.addView(emptyView);
        isUseEmpty = true;
        if (insert && hasEmptyView()) {
            int position = 0;
            if (headerWithEmptyEnable && hasHeaderLayout()) {
                position++;
            }
            if (getItemCount() > oldItemCount) {
                notifyItemInserted(position);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void setEmptyView(int layoutResId) {
        if (weakRecyclerView != null && weakRecyclerView.get() != null) {
            View view = LayoutInflater.from(weakRecyclerView.get().getContext()).inflate(layoutResId, weakRecyclerView.get(), false);
            setEmptyView(view);
        }
    }

    public void removeEmptyView() {
        if (mEmptyLayout != null) {
            mEmptyLayout.removeAllViews();
        }
    }

    public boolean hasEmptyView() {
        if (mEmptyLayout == null || mEmptyLayout.getChildCount() == 0) {
            return false;
        }
        if (!isUseEmpty) {
            return false;
        }
        return data.isEmpty();
    }

    /**
     * When the current adapter is empty, the BaseQuickAdapter can display a special view
     * called the empty view. The empty view is used to provide feedback to the user
     * that no data is available in this AdapterView.
     *
     * @return The view to show if the adapter is empty.
     */
    private FrameLayout emptyLayout;

    public FrameLayout getEmptyLayout() {
        return emptyLayout;
    }

    /*************************** Animation ******************************************/

    /**
     * add animation when you want to show time
     *
     * @param holder
     */
    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (animationEnable) {
            if (!isAnimationFirstOnly || holder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (adapterAnimation != null) {
                    animation = adapterAnimation;
                } else {
                    animation = new AlphaInAnimation();
                }
                for (Animator anim : animation.animators(holder.itemView)) {
                    startAnim(anim, holder.getLayoutPosition());
                }
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }

    /**
     * 开始执行动画方法
     * 可以重写此方法，实行更多行为
     *
     * @param anim
     * @param index
     */
    protected void startAnim(Animator anim, int index) {
        anim.start();
    }

    /**
     * 内置默认动画类型
     */
    public enum AnimationType {
        AlphaIn, ScaleIn, SlideInBottom, SlideInLeft, SlideInRight
    }

    /**
     * 使用内置默认动画设置
     *
     * @param animationType AnimationType
     */
    public BaseAnimation setAnimationWithDefault(AnimationType animationType) {
        switch (animationType) {
            default:
            case AlphaIn:
                return adapterAnimation = new AlphaInAnimation();
            case ScaleIn:
                return adapterAnimation = new ScaleInAnimation();
            case SlideInBottom:
                return adapterAnimation = new SlideInBottomAnimation();
            case SlideInLeft:
                return adapterAnimation = new SlideInLeftAnimation();
            case SlideInRight:
                return adapterAnimation = new SlideInRightAnimation();
        }
    }

    /*************************** 设置数据相关 ******************************************/
    /**
     * setting up a new instance to data;
     * 设置新的数据实例
     * Please use setNewInstance()
     *
     * @param data
     */
    @Deprecated
    public void setNewData(List<T> data) {
        setNewInstance(data);
    }

    /**
     * setting up a new instance to data;
     * 设置新的数据实例，替换原有内存引用。
     * 通常情况下，如非必要，请使用[setList]修改内容
     *
     * @param list
     */
    public void setNewInstance(List<T> list) {
        if (list == this.data) {
            return;
        }
        this.data = (list == null) ? new ArrayList<>() : list;
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().reset();
        }
        mLastPosition = -1;
        notifyDataSetChanged();
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().checkDisableLoadMoreIfNotFullPage();
        }
    }

    /**
     * use data to replace all item in mData. this method is different [setList],
     * it doesn't change the [BaseQuickAdapter.data] reference
     * Deprecated, Please use [setList]
     * Please use setData()
     *
     * @param newData data collection
     */
    @Deprecated
    public void replaceData(Collection<T> newData) {
        setList(newData);
    }

    /**
     * 使用新的数据集合，改变原有数据集合内容。
     * 注意：不会替换原有的内存引用，只是替换内容
     *
     * @param list Collection<T>?
     */
    public void setList(Collection<T> list) {
        if (list != this.data) {
            this.data.clear();
            if (list != null && !list.isEmpty()) {
                this.data.addAll(list);
            }
        } else {
            if (list != null && !list.isEmpty()) {
                List<T> newList = new ArrayList(list);
                this.data.clear();
                this.data.addAll(newList);
            } else {
                this.data.clear();
            }
        }
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().reset();
        }
        mLastPosition = -1;
        notifyDataSetChanged();
        if (getLoadMoreModule() != null) {
            getLoadMoreModule().checkDisableLoadMoreIfNotFullPage();
        }
    }

    /**
     * change data
     * 改变某一位置数据
     */
    public void setData(@IntRange(from = 0) int index, T data) {
        if (index >= this.data.size()) {
            return;
        }
        this.data.set(index, data);
        notifyItemChanged(index + getHeaderLayoutCount());
    }

    /**
     * add one new data in to certain location
     * 在指定位置添加一条新数据
     *
     * @param position
     */
    public void addData(@IntRange(from = 0) int position, T data) {
        this.data.add(position, data);
        notifyItemInserted(position + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * add one new data
     * 添加一条新数据
     */
    public void addData(@NonNull T data) {
        this.data.add(data);
        notifyItemInserted(this.data.size() + getHeaderLayoutCount());
        compatibilityDataSizeChanged(1);
    }

    /**
     * add new data in to certain location
     * 在指定位置添加数据
     *
     * @param position the insert position
     * @param newData  the new data collection
     */
    public void addData(@IntRange(from = 0) int position, Collection<T> newData) {
        this.data.addAll(position, newData);
        notifyItemRangeInserted(position + getHeaderLayoutCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    public void addData(@NonNull Collection<T> newData) {
        this.data.addAll(newData);
        notifyItemRangeInserted(this.data.size() - newData.size() + getHeaderLayoutCount(), newData.size());
        compatibilityDataSizeChanged(newData.size());
    }

    /**
     * remove the item associated with the specified position of adapter
     * 删除指定位置的数据
     *
     * @param position
     */
    public void removeAt(@IntRange(from = 0) int position) {
        if (position >= data.size()) {
            return;
        }
        this.data.remove(position);
        int internalPosition = position + getHeaderLayoutCount();
        notifyItemRemoved(internalPosition);
        compatibilityDataSizeChanged(0);
        notifyItemRangeChanged(internalPosition, this.data.size() - internalPosition);
    }

    public void remove(T data) {
        int index = this.data.indexOf(data);
        if (index == -1) {
            return;
        }
        remove(index);
    }

    /**
     * Please use removeAt()
     */
    @Deprecated
    public void remove(@IntRange(from = 0) int position) {
        removeAt(position);
    }

    /**
     * compatible getLoadMoreViewCount and getEmptyViewCount may change
     *
     * @param size Need compatible data size
     */
    protected void compatibilityDataSizeChanged(int size) {
        if (this.data.size() == size) {
            notifyDataSetChanged();
        }
    }

    /**
     * 设置Diff Callback，用于快速生成 Diff Config。
     *
     * @param diffCallback ItemCallback<T>
     */
    public void setDiffCallback(DiffUtil.ItemCallback<T> diffCallback) {
        this.setDiffConfig(new BrvahAsyncDifferConfig.Builder(diffCallback).build());
    }

    /**
     * 设置Diff Config。如需自定义线程，请使用此方法。
     * 在使用 [setDiffNewData] 前，必须设置此方法
     *
     * @param config BrvahAsyncDifferConfig<T>
     */
    public void setDiffConfig(BrvahAsyncDifferConfig<T> config) {
        mDiffHelper = new BrvahAsyncDiffer(this, config);
    }

    public BrvahAsyncDiffer<T> getDiffHelper() {
        if (mDiffHelper == null) {
            throw new IllegalStateException("Please use setDiffCallback() or setDiffConfig() first!");
        }
        return mDiffHelper;
    }

    /**
     * 使用 Diff 设置新实例.
     * 此方法为异步Diff，无需考虑性能问题.
     * 使用之前请先设置 [setDiffCallback] 或者 [setDiffConfig].
     * <p>
     * Use Diff setting up a new instance to data.
     * This method is asynchronous.
     *
     * @param list MutableList<T>?
     */
    public void setDiffNewData(List<T> list) {
        if (hasEmptyView()) {
            // If the current view is an empty view, set the new data directly without diff
            setNewInstance(list);
            return;
        }
        if (mDiffHelper != null) {
            mDiffHelper.submitList(list, null);
        }
    }

    /**
     * 使用 DiffResult 设置新实例.
     * Use DiffResult setting up a new instance to data.
     *
     * @param diffResult DiffResult
     * @param list       New Data
     */
    public void setDiffNewData(@NonNull DiffUtil.DiffResult diffResult, List<T> list) {
        if (hasEmptyView()) {
            // If the current view is an empty view, set the new data directly without diff
            setNewInstance(list);
            return;
        }
        diffResult.dispatchUpdatesTo(new BrvahListUpdateCallback(this));
        this.data = list;
    }

    /************************************** Set Listener ****************************************/
    public void setGridSpanSizeLookup(GridSpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public void setOnItemChildClickListener(OnItemChildClickListener listener) {
        this.mOnItemChildClickListener = listener;
    }

    @Override
    public void setOnItemChildLongClickListener(OnItemChildLongClickListener listener) {
        this.mOnItemChildLongClickListener = listener;
    }

    final OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    final OnItemLongClickListener getOnItemLongClickListener() {
        return mOnItemLongClickListener;
    }

    final OnItemChildClickListener getOnItemChildClickListener() {
        return mOnItemChildClickListener;
    }

    final OnItemChildLongClickListener getOnItemChildLongClickListener() {
        return mOnItemChildLongClickListener;
    }
}
