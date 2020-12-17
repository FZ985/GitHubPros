package com.kmt.pro.ui.activity.coin;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.adapter.coin.CoinListAdapter;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.bean.coin.RechargeSearchBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.CoinContract;
import com.kmt.pro.mvp.presenter.CoinPresenterImpl;
import com.kmt.pro.widget.SideBar;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.lxbuytimes.kmtapp.sortapi.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-21 10:31
 **/
public class CoinListActivity extends BaseActivity<CoinContract.CoinPresenter> implements CoinContract.CoinListView {

    @BindView(R.id.recharge_et)
    EditText rechargeEt;
    @BindView(R.id.recharge_msg)
    TextView rechargeMsg;
    @BindView(R.id.recharge_recycler)
    RecyclerView rechargeRecycler;
    @BindView(R.id.status)
    Statelayout status;
    @BindView(R.id.recharge_sidebar)
    SideBar rechargeSidebar;

    private CoinListAdapter adapter;
    private LinearLayoutManager manager;
    private StickyRecyclerHeadersDecoration headersDecor;
    private List<RechargeSearchBean> datas = new ArrayList<>();
    private int type;//数字货币列表类型  1： 充币 2：提币 3： 兑换

    @Override
    public int getLayoutId() {
        type = getIntent().getIntExtra(KConstant.Key.key_coinlist_type, -1);
        return R.layout.activity_recharge_coin_list;
    }

    @Override
    public void initView() {
        manager = new LinearLayoutManager(this);
        rechargeRecycler.setLayoutManager(manager);
        adapter = new CoinListAdapter();
        rechargeRecycler.setAdapter(adapter);
        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        rechargeRecycler.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        rechargeSidebar.setOnTouchingLetterChangedListener(s -> {
            int index = adapter.getIndex(s);
            if (index != -1 && manager != null) {
                manager.scrollToPositionWithOffset(index, 0);
            }
        });
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            RechargeSearchBean item = adapter.getItem(position);
            if (item == null) return;
            if (type == 1) {
                ActivityHelper.toRecharge(mContext, item.symbol_id, item.tokenSymbol);
            } else if (type == 2) {
                ActivityHelper.toMention(mContext, item.symbol_id, item.tokenSymbol);
            }
        });
        rechargeEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    List<RechargeSearchBean> d = filterData(s.toString().trim());
                    if (d.size() > 0)
                        adapter.setNewData(d);
                    else adapter.setNewData(datas);
                } else {
                    adapter.setNewData(datas);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (type == 1) {
            rechargeEt.setHint("请输入您要充值的币种名称");
            rechargeMsg.setText("以下币种支持用GBT充值");
        } else if (type == 2) {
            rechargeEt.setHint("请输入您要提的币种名称");
            rechargeMsg.setText("以下币种支持用GBT购买");
        } else if (type == 3) {
            rechargeEt.setHint("请输入您要兑换的币种名称");
            rechargeMsg.setText("以下币种支持用GBT兑换");
        }
    }

    @Override
    public void initData() {
        mPresenter.reqCoinList();
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true).init();
    }

    @Override
    public void onCoinList(List<RechargeSearchBean> data) {
        if (isFinishing()) return;
        if (data.size() > 0) {
            if (datas == null) {
                datas = new ArrayList<>();
            }
            datas.clear();
            datas.addAll(data);
            adapter.setNewData(datas);
        } else onCoinListEmpty("");
    }

    @Override
    public void onCoinListEmpty(String msg) {
        if (isFinishing()) return;
        status.showEmpty();
    }

    @OnClick({R.id.recharge_btn})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.recharge_btn) {
            hideSoftKeyBoard();
            finish();
        }
    }

    private List<RechargeSearchBean> filterData(String filterStr) {
        List<RechargeSearchBean> data = new ArrayList<>();
        if (datas != null) {
            for (RechargeSearchBean d : datas) {
                if (d.tokenSymbol.toLowerCase().startsWith(filterStr.toLowerCase())) {
                    data.add(d);
                }
            }
        }
        return data;
    }

    @Override
    protected CoinContract.CoinPresenter getPresenter() {
        return new CoinPresenterImpl();
    }
}
