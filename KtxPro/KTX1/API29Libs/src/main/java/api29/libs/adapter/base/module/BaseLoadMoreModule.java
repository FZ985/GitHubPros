package api29.libs.adapter.base.module;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import api29.libs.adapter.base.BaseQuickAdapter;
import api29.libs.adapter.base.listener.LoadMoreListenerImp;
import api29.libs.adapter.base.listener.OnLoadMoreListener;
import api29.libs.adapter.base.loadmore.BaseLoadMoreView;
import api29.libs.adapter.base.loadmore.LoadMoreStatus;
import api29.libs.adapter.base.loadmore.SimpleLoadMoreView;
import api29.libs.adapter.base.viewholder.BaseViewHolder;

/**
 * Create by JFZ
 * date: 2020-05-07 12:30
 * 需要【向下加载更多】功能的，[BaseQuickAdapter]继承此接口
 **/
public class BaseLoadMoreModule implements LoadMoreListenerImp {

    private BaseQuickAdapter baseQuickAdapter;

    public BaseLoadMoreModule(BaseQuickAdapter adapter) {
        this.baseQuickAdapter = adapter;
    }

    private OnLoadMoreListener mLoadMoreListener;
    /**
     * 不满一屏时，是否可以继续加载的标记位
     */
    private boolean mNextLoadEnable = true;

    public LoadMoreStatus loadMoreStatus = LoadMoreStatus.Complete;
    private boolean isLoadEndMoreGone = false;
    public BaseLoadMoreView loadMoreView = new SimpleLoadMoreView();
    /**
     * 加载完成后是否允许点击
     */
    private boolean enableLoadMoreEndClick = false;
    /**
     * 是否打开自动加载更多
     */
    private boolean isAutoLoadMore = true;
    public void setAutoLoadMore(boolean isAutoLoadMore){
        this.isAutoLoadMore = isAutoLoadMore;
    }
    /**
     * 当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多
     */
    private boolean isEnableLoadMoreIfNotFullPage = true;
    /**
     * 预加载
     */
    private int preLoadNumber = 1;

    public void setPreLoadNumber(int value) {
        if (value > 1) {
            this.preLoadNumber = value;
        }
    }

    /**
     * 是否加载中
     */
    private boolean isLoading;

    public boolean isLoading() {
        return loadMoreStatus == LoadMoreStatus.Loading;
    }


    /**
     * Gets to load more locations
     *
     * @return
     */

    public int getLoadMoreViewPosition() {
        if (baseQuickAdapter.hasEmptyView()) {
            return  -1;
        }
        return  (baseQuickAdapter.getHeaderLayoutCount() + baseQuickAdapter.data.size() + baseQuickAdapter.getFooterLayoutCount());
    }

    /**
     * 是否打开加载更多
     */
    private boolean isEnableLoadMore = false;

    public void setEnableLoadMore(boolean enableLoadMore) {
        boolean oldHasLoadMore = hasLoadMoreView();
        isEnableLoadMore = enableLoadMore;
        boolean newHasLoadMore = hasLoadMoreView();
        if (oldHasLoadMore) {
            if (!newHasLoadMore) {
                baseQuickAdapter.notifyItemRemoved(getLoadMoreViewPosition());
            }
        } else {
            if (newHasLoadMore) {
                loadMoreStatus = LoadMoreStatus.Complete;
                baseQuickAdapter.notifyItemInserted(getLoadMoreViewPosition());
            }
        }
    }

    public void setupViewHolder(BaseViewHolder viewHolder) {
        viewHolder.itemView.setOnClickListener(v -> {
            if (loadMoreStatus == LoadMoreStatus.Fail) {
                loadMoreToLoading();
            } else if (loadMoreStatus == LoadMoreStatus.Complete) {
                loadMoreToLoading();
            } else if (enableLoadMoreEndClick && loadMoreStatus == LoadMoreStatus.End) {
                loadMoreToLoading();
            }
        });
    }

    /**
     * The notification starts the callback and loads more
     */
    private void loadMoreToLoading() {
        if (loadMoreStatus == LoadMoreStatus.Loading) {
            return;
        }
        loadMoreStatus = LoadMoreStatus.Loading;
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition());
        invokeLoadMoreListener();
    }

    public boolean hasLoadMoreView() {
        if (mLoadMoreListener == null || !isEnableLoadMore) {
            return false;
        }
        if (loadMoreStatus == LoadMoreStatus.End && isLoadEndMoreGone) {
            return false;
        }
        return (baseQuickAdapter.data != null && !baseQuickAdapter.data.isEmpty());
    }

    /**
     * 自动加载数据
     *
     * @param position Int
     */
    public void autoLoadMore(int position) {
        if (!isAutoLoadMore) {
            //如果不需要自动加载更多，直接返回
            return;
        }
        if (!hasLoadMoreView()) {
            return;
        }
        if (position < baseQuickAdapter.getItemCount() - preLoadNumber) {
            return;
        }
        if (loadMoreStatus != LoadMoreStatus.Complete) {
            return;
        }
        if (loadMoreStatus == LoadMoreStatus.Loading) {
            return;
        }
        if (!mNextLoadEnable) {
            return;
        }
        invokeLoadMoreListener();
    }

    /**
     * 触发加载更多监听
     */
    private void invokeLoadMoreListener() {
        loadMoreStatus = LoadMoreStatus.Loading;
        if (baseQuickAdapter.getWeakRecyclerView().get() != null) {
            ((RecyclerView) baseQuickAdapter.getWeakRecyclerView().get()).postDelayed(() -> {
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }, 0);
        } else {
            if (mLoadMoreListener != null) {
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    /**
     * check if full page after [BaseQuickAdapter.setNewInstance] [BaseQuickAdapter.setList],
     * if full, it will enable load more again.
     * <p>
     * 用来检查数据是否满一屏，如果满足条件，再开启
     */
    public void checkDisableLoadMoreIfNotFullPage() {
        if (isEnableLoadMoreIfNotFullPage) {
            return;
        }
        // 先把标记位设置为false
        mNextLoadEnable = false;
        if (baseQuickAdapter.getWeakRecyclerView().get() == null) return;
        RecyclerView recyclerView = (RecyclerView) baseQuickAdapter.getWeakRecyclerView().get();
        if (recyclerView.getLayoutManager() == null) return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            recyclerView.postDelayed(() -> {
                if (isFullScreen((LinearLayoutManager) manager)) {
                    mNextLoadEnable = true;
                }
            }, 50);
        } else if (manager instanceof StaggeredGridLayoutManager) {
            recyclerView.postDelayed(() -> {
                int[] positions = new int[((StaggeredGridLayoutManager) manager).getSpanCount()];
                ((StaggeredGridLayoutManager) manager).findLastCompletelyVisibleItemPositions(positions);
                int pos = getTheBiggestNumber(positions) + 1;
                if (pos != baseQuickAdapter.getItemCount()) {
                    mNextLoadEnable = true;
                }
            }, 50);
        }
    }

    private boolean isFullScreen(LinearLayoutManager llm) {
        return (llm.findLastCompletelyVisibleItemPosition() + 1) != baseQuickAdapter.getItemCount() ||
                llm.findFirstCompletelyVisibleItemPosition() != 0;
    }

    private int getTheBiggestNumber(int[] numbers) {
        int tmp = -1;
        if (numbers == null || numbers.length == 0) {
            return tmp;
        }
        for (Integer num : numbers) {
            if (num > tmp) {
                tmp = num;
            }
        }
        return tmp;
    }

    /**
     * Refresh end, no more data
     *
     * @param gone if true gone the load more view
     */
    public void loadMoreEnd(boolean gone) {
        if (!hasLoadMoreView()) {
            return;
        }
//        mNextLoadEnable = false
        isLoadEndMoreGone = gone;

        loadMoreStatus = LoadMoreStatus.End;

        if (gone) {
            baseQuickAdapter.notifyItemRemoved(getLoadMoreViewPosition());
        } else {
            baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition());
        }
    }

    /**
     * Refresh complete
     */
    public void loadMoreComplete() {
        if (!hasLoadMoreView()) {
            return;
        }
//        mNextLoadEnable = true
        loadMoreStatus = LoadMoreStatus.Complete;
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition());
        checkDisableLoadMoreIfNotFullPage();
    }

    /**
     * Refresh failed
     */
    public void loadMoreFail() {
        if (!hasLoadMoreView()) {
            return;
        }
        loadMoreStatus = LoadMoreStatus.Fail;
        baseQuickAdapter.notifyItemChanged(getLoadMoreViewPosition());
    }

    /**
     * 设置加载监听事件
     *
     * @param listener OnLoadMoreListener?
     */
    @Override
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
        isEnableLoadMore = true;
    }

    /**
     * 重置状态
     */
    public void reset() {
        if (mLoadMoreListener != null) {
            isEnableLoadMore = true;
            loadMoreStatus = LoadMoreStatus.Complete;
        }
    }
}
