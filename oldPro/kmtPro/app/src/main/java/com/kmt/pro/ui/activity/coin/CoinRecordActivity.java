package com.kmt.pro.ui.activity.coin;

import android.view.View;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.coin.CoinRecordListAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.coin.CoinRecordBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.mvp.presenter.CoinPresenterImpl;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.utils.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-23 13:58
 **/
public class CoinRecordActivity extends BaseToolBarActivity<CoinContract.CoinPresenter> implements CoinContract.RecordView, OnLoadMoreListener {

    @BindView(R.id.msgtv)
    TextView msgtv;
    @BindView(R.id.record_status)
    Statelayout recordStatus;
    @BindView(R.id.record_recycler)
    RecyclerView recordRecycler;
    @BindView(R.id.record_sc)
    NestedScrollView recordSc;
    @BindView(R.id.record_refresh)
    SmartRefreshLayout recordRefresh;
    private String symbol_id;
    private int offest = Utils.dip2px(BaseApp.getInstance(), 50);
    private int type;//记录类型； 1： 充值记录  2： 提现记录
    private int page = 1;
    private CoinRecordListAdapter adapter;

    @Override
    public int getLayoutId() {
        symbol_id = getIntent().getStringExtra(KConstant.Key.key_symbol_id);
        type = getIntent().getIntExtra(KConstant.Key.key_symbol_recordType, 1);
        return R.layout.activity_coinrecord;
    }

    @Override
    public void initView() {
        recordRefresh.setEnableRefresh(false);
        recordRefresh.setEnableLoadMore(false);
        recordRefresh.setRefreshFooter(new ClassicsFooter(this));
        recordRecycler.setLayoutManager(new LinearLayoutManager(this));
        recordSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 >= offest) {
                mToolbar.title.setText("历史记录");
                mToolbar.bottomLine(true);
            } else {
                mToolbar.title.setText("");
                mToolbar.bottomLine(false);
            }
        });
        recordRecycler.setAdapter(adapter = new CoinRecordListAdapter(type));
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            CoinRecordBean item = adapter.getItem(position);
            if (item == null) return;
            ActivityHelper.toRocordDetail(CoinRecordActivity.this, type, item.blockHash);
        });

    }

    @Override
    public void onRecordList(int pages, List<CoinRecordBean> datas) {
        if (isFinishing()) return;
        recordStatus.setVisibility(View.GONE);
        if (pages == 1) {
            if (datas.size() > 0) {
                recordStatus.showContent();
                adapter.setNewData(datas);
                if (datas.size() >= 10) {
                    recordRefresh.setEnableLoadMore(true);
                    recordRefresh.setOnLoadMoreListener(this);
                }
            } else {
                recordStatus.setVisibility(View.VISIBLE);
                recordStatus.showEmpty(v -> presenter.reqRecordList(type, page = 1, symbol_id));
                recordRefresh.setEnableLoadMore(false);
            }
        } else {
            if (datas.size() > 0) {
                recordRefresh.finishLoadMore();
                adapter.addData(datas);
            } else {
                recordRefresh.finishLoadMore();
                recordRefresh.setEnableLoadMore(false);
            }
        }
    }

    @Override
    public void onRecordErr(int pagesize, String err) {
        if (isFinishing()) return;
        if (pagesize == 1) {
            recordStatus.setVisibility(View.VISIBLE);
            recordStatus.showEmpty(v -> presenter.reqRecordList(type, page = 1, symbol_id));
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        presenter.reqRecordList(type, page = page + 1, symbol_id);
    }

    @Override
    public void initData() {
        msgtv.setText((type == 1) ? "普通充币" : "普通提币");
        mToolbar.bottomLine(false);
        presenter.reqRecordList(type, page = 1, symbol_id);
    }

    @Override
    protected CoinContract.CoinPresenter getPresenter() {
        return new CoinPresenterImpl();
    }
}
