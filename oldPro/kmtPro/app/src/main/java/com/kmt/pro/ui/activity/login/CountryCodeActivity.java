package com.kmt.pro.ui.activity.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.kmt.pro.R;
import com.kmt.pro.adapter.CountryCodeAdapter;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.event.CountryCodeEvent;
import com.kmt.pro.mvp.contract.CountryCodeContract;
import com.kmt.pro.mvp.presenter.CountryPresenterImpl;
import com.kmt.pro.utils.chinese2pinyin.CityItem;
import com.kmt.pro.widget.SideBar;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.sortapi.StickyRecyclerHeadersDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-06 15:05
 **/
public class CountryCodeActivity extends BaseToolBarActivity<CountryCodeContract.CountryPresenter> implements CountryCodeContract.CountryCodeView {
    @BindView(R.id.route_mude_text)
    EditText routeMudeText;
    @BindView(R.id.country_recycler)
    RecyclerView countryRecycler;
    @BindView(R.id.country_state)
    Statelayout countryState;
    @BindView(R.id.country_side)
    SideBar sideBar;
    private CountryCodeAdapter adapter;
    private LinearLayoutManager manager;
    private StickyRecyclerHeadersDecoration headersDecor;

    @Override
    public int getLayoutId() {
        return R.layout.activity_countrycode;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("选择区号");
    }

    @Override
    public void initData() {
        mToolbar.bottomLine(false);
        manager = new LinearLayoutManager(this);
        countryRecycler.setLayoutManager(manager);
        adapter = new CountryCodeAdapter();
        countryRecycler.setAdapter(adapter);
        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        countryRecycler.addItemDecoration(headersDecor);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        sideBar.setOnTouchingLetterChangedListener(s -> {
            int index = adapter.getIndex(s);
            if (index != -1 && manager != null) {
                manager.scrollToPositionWithOffset(index, 0);
            }
        });
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            CityItem item = adapter.getItem(position);
            EventBus.getDefault().post(new CountryCodeEvent(item.getNickName(), item.getCode()));
            finish();
        });
        countryState.showLoading();
        presenter.requestCountryCode();
        routeMudeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String endStr = s.toString();
                if (TextUtils.isEmpty(endStr)) {
                    if (datas.size() > 0) adapter.setNewData(datas);
                } else {
                    List<CityItem> filterDatas = new ArrayList<>();
                    filterDatas.clear();
                    for (CityItem data : datas) {
                        if (data.getNickName().contains(endStr)) {
                            filterDatas.add(data);
                        }
                    }
                    if (filterDatas.size() > 0) {
                        adapter.setNewData(filterDatas);
                    }
                }
            }
        });
    }

    private List<CityItem> datas = new ArrayList<>();

    @Override
    public void onList(List<CityItem> data) {
        if (data.size() > 0) {
            countryState.showContent();
            if (datas.size() > 0) {
                datas.clear();
            }
            datas.addAll(data);
            adapter.setNewData(datas);
        } else countryState.showEmpty(v -> {
            countryState.showLoading();
            presenter.requestCountryCode();
        });
    }

    @Override
    public void onListErr(String err) {
        if (isFinishing()) return;
        if (Utils.isConnection(this)) {
            countryState.showError(v -> {
                countryState.showLoading();
                presenter.requestCountryCode();
            });
        } else {
            countryState.showNoNetwork(v -> {
                countryState.showLoading();
                presenter.requestCountryCode();
            });
        }
    }

    @Override
    protected CountryCodeContract.CountryPresenter getPresenter() {
        return new CountryPresenterImpl();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        datas = null;
        headersDecor = null;
        adapter = null;
    }
}
