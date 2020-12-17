package com.kmt.pro.ui.activity;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.kmt.pro.R;
import com.kmt.pro.adapter.AccountIncomeAdapter;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.mvp.contract.AccountIncomeContract;
import com.kmt.pro.mvp.presenter.AccountIncomePresenterImpl;
import com.kmt.pro.ui.dialog.pop.IncomeScreenPop;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-24 9:53
 **/
public class AccountIncomeActivity extends BaseToolBarActivity<AccountIncomeContract.AccountIncomePresenter> implements AccountIncomeContract.AccountIncomeView, OnRefreshLoadMoreListener {
    @BindView(R.id.account_head)
    MaterialHeader accountHead;
    @BindView(R.id.account_recycle)
    RecyclerView accountRecycle;
    @BindView(R.id.account_refresh)
    SmartRefreshLayout accountRefresh;
    @BindView(R.id.account_state)
    Statelayout accountState;
    private AccountIncomeAdapter adapter;
    private int page;
    private String type = "0";

    @Override
    public int getLayoutId() {
        return R.layout.activity_account_income;
    }

    @Override
    public void initView() {
        regBroadcastRecv(Actions.CANCEL_WITHDRAW_SUCCESS);
        accountHead.setColorSchemeResources(R.color.colorAccent);
        accountRefresh.setEnableRefresh(true);
        accountRefresh.setEnableLoadMore(true);
        accountRefresh.setEnableLoadMoreWhenContentNotFull(false);
        accountRefresh.setRefreshFooter(new ClassicsFooter(this));
        accountRefresh.setOnRefreshLoadMoreListener(this);
        accountRecycle.setLayoutManager(new LinearLayoutManager(this));
        accountRecycle.setAdapter(adapter = new AccountIncomeAdapter());
        mToolbar.title.setText("交易记录");
        mToolbar.right.setText("筛选");
        mToolbar.right.setOnClickListener(this::showPop);
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            AccountIncomeBean item = adapter.getItem(position);
            ActivityHelper.toAccountIncomeDetail(mContext, item);
        });
    }

    @Override
    public void initData() {
        request((page = 1) + "", type);
    }

    @Override
    public void onAccountIncomeDatas(List<AccountIncomeBean> datas, String page, String type) {
        if (isFinishing()) return;
        accountState.showContent();
        if (page.equals("1")) {
            accountRefresh.finishRefresh();
            adapter.setNewInstance(datas);
            if (datas.size() == 0) {
                accountState.showEmpty(v -> initData());
            }
        } else {
            if (datas.size() > 0) {
                adapter.addData(datas);
                accountRefresh.finishLoadMore();
            } else {
                accountRefresh.finishLoadMore();
                Tools.showToast("到底了~");
                accountRefresh.setEnableLoadMore(false);
            }
        }
    }

    @Override
    public void onAccountIncomeErr(String pagesize, String type) {
        if (isFinishing()) return;
        if (pagesize.equals("1")) {
            accountRefresh.finishRefresh();
            if (Utils.isConnection(this)) {
                accountState.showEmpty(v -> request((page = 1) + "", type));
            } else accountState.showNoNetwork(v -> request((page = 1) + "", type));
        } else {
            accountRefresh.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        accountRefresh.setEnableLoadMore(true);
        request((page = 1) + "", type);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page = page + 1;
        request(page + "", type);
    }

    private void request(String page, String type) {
        presenter.reqAccountList(page, type);
    }

    private void showPop(View view) {
        IncomeScreenPop pop = new IncomeScreenPop(this, (popupWindow, type1) -> {
            type = type1;
            accountRefresh.setEnableLoadMore(true);
            request((page = 1) + "", type);
            popupWindow.dismiss();
        });
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        Logger.e("==坐标位置:" + location[0] + "," + location[1]);
        pop.showAtLocation(view, Gravity.TOP | Gravity.RIGHT,
                Utils.dip2px(view.getContext(), 10),
                location[1] + view.getMeasuredHeight());
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
        pop.setOnDismissListener(() -> {
            final WindowManager.LayoutParams params1 = getWindow().getAttributes();
            params1.alpha = 1.0f;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getWindow().setAttributes(params1);
        });
    }

    @Override
    public void onSafeReceive(Intent intent, String action) {
        super.onSafeReceive(intent, action);
        if (action.equals(Actions.CANCEL_WITHDRAW_SUCCESS)){
            accountRefresh.setEnableLoadMore(true);
            request((page = 1) + "", type);
        }
    }

    @Override
    protected AccountIncomeContract.AccountIncomePresenter getPresenter() {
        return new AccountIncomePresenterImpl();
    }

}
