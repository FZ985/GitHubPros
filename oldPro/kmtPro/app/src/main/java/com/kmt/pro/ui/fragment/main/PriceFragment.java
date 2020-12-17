package com.kmt.pro.ui.fragment.main;

import android.view.View;
import android.widget.LinearLayout;

import com.kmt.pro.R;
import com.kmt.pro.adapter.PriceListAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.bean.ProductBean;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.mvp.contract.PriceContract;
import com.kmt.pro.mvp.presenter.PricePresenterImpl;
import com.kmt.pro.ui.fragment.ContentFragment;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.utils.Utils;
import com.kmtlibs.app.widget.ShadowLayout;
import com.kmtlibs.banner.Banner;
import com.kmtlibs.okhttp.HttpImpl;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-06-30 18:38
 **/
public class PriceFragment extends ContentFragment<PriceContract.PricePresenter> implements PriceContract.PriceView, OnRefreshListener {
    @BindView(R.id.price_refresh_header)
    MaterialHeader priceRefreshHeader;
    @BindView(R.id.price_banner)
    Banner priceBanner;
    @BindView(R.id.price_refresh_layout)
    SmartRefreshLayout priceRefreshLayout;
    @BindView(R.id.price_recycle)
    RecyclerView priceRecycle;
    @BindView(R.id.price_statelayout)
    Statelayout priceStatelayout;
    @BindView(R.id.price_scrollview)
    NestedScrollView priceScrollview;
    @BindView(R.id.price_toolbar)
    LinearLayout priceToolbar;
    @BindView(R.id.price_alphatoolbar)
    ShadowLayout priceAlphatoolbar;
    private PriceListAdapter adapter;
    private float offset = 184;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_price;
    }

    @Override
    public void initView() {
        offset = Utils.dip2px(BaseApp.getInstance(), 184);
        priceRefreshHeader.setColorSchemeResources(R.color.colorPrimary);
        priceRefreshLayout.setEnableLoadMore(false);
        priceRefreshLayout.setOnRefreshListener(this);
        priceRecycle.setLayoutManager(new LinearLayoutManager(mContext));
        priceRecycle.setNestedScrollingEnabled(false);
        priceRecycle.setHasFixedSize(true);
        priceRecycle.setAdapter(adapter = new PriceListAdapter());
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            if (adapter.getItem(position) != null) {
                ProductBean item = adapter.getItem(position);
                presenter.onItemClick(getActivity(), item, position);
            }
        });
        toolbarTrans(0);
        priceScrollview.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            toolbarTrans(i1);
        });
        priceStatelayout.showLoading();
        request();
    }

    @Override
    public void initData() {

    }

    @Override
    public Banner getBanner() {
        return priceBanner;
    }

    @Override
    public void bannerEmpty() {
        if (isDetached()) return;
        priceBanner.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListSucc(int page, List<ProductBean> datas) {
        if (isDetached()) return;
        priceStatelayout.showContent();
        if (page == 0) {
            priceRefreshLayout.finishRefresh();
            if (datas.size() > 0) {
                priceStatelayout.showContent();
                adapter.setNewInstance(datas);
            } else {
                priceStatelayout.showEmpty(v -> {
                    priceStatelayout.showLoading();
                    presenter.requestList(page);
                });
            }
        }
    }

    @Override
    public void onListErr(int page, String msg) {
        if (isDetached()) return;
        priceRefreshLayout.finishRefresh();
        if (Utils.isConnection(BaseApp.getInstance())) {
            priceStatelayout.showError(v -> {
                priceStatelayout.showLoading();
                presenter.requestList(page);
            });
        } else {
            priceStatelayout.showNoNetwork(v -> {
                priceStatelayout.showLoading();
                presenter.requestList(page);
            });
        }
    }


    private void request() {
        presenter.requestBanner().requestList(0);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        request();
    }

    @Override
    public void refresh() {

    }

    @Override
    public LifecycleOwner lifecycle() {
        return this;
    }

    @Override
    protected PriceContract.PricePresenter getPresenter() {
        return new PricePresenterImpl();
    }

    public static PriceFragment instance(MainFragmentPageCall call) {
        PriceFragment fragment = new PriceFragment();
        fragment.call = call;
        return fragment;
    }

    private void toolbarTrans(final int y) {
        mHandler.post(() -> {
            float alpha = 0f;
            if (y <= 0) {
                alpha = 0f;
                isTop = true;
            }
            if (call != null && y <= 10) {
                isChangeBar = false;
                HttpImpl.THREAD_POOL.execute(() -> call.onFragmentPage(0, 1));
            }
            if (y > 0 && y <= offset) {
                alpha = y / offset;
                isTop = true;
            }
            if (y > offset) {
                alpha = 1f;
            }
            int colorAlpha = (int) (alpha * 255);
            if (colorAlpha >= 229) {
                if (isTop) {
                    isTop = false;
                }
                colorAlpha = 229;
            }
            if (call != null && 229 == colorAlpha && !isChangeBar) {
                isChangeBar = true;
                HttpImpl.THREAD_POOL.execute(() -> call.onFragmentPage(0, 2));
            }
            priceToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(ContextCompat.getColor(BaseApp.getInstance(), R.color.white), colorAlpha));
            priceAlphatoolbar.setAlpha(alpha);
        });
    }

    private boolean isTop;
    private boolean isChangeBar;//

    public boolean isTop() {
        return isTop;
    }

    @OnClick(R.id.prive_message_rl)
    public void onViewClicked() {
        Tools.showToast("message");
    }
}
