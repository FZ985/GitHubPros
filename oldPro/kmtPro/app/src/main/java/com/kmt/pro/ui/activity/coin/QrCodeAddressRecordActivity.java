package com.kmt.pro.ui.activity.coin;

import com.kmt.pro.R;
import com.kmt.pro.adapter.coin.QrcodeAddressAdapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.app.utils.Utils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-04-15 18:59
 **/
public class QrCodeAddressRecordActivity extends BaseToolBarActivity {
    @BindView(R.id.status)
    Statelayout status;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.recharge_sc)
    NestedScrollView rechargeSc;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int offest = Utils.dip2px(BaseApp.getInstance(), 50);
    private QrcodeAddressAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qrcode_address_record;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() { mToolbar.bottomLine(false);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        refresh.setEnableRefresh(false);
        refresh.setEnableLoadMore(false);
        refresh.setRefreshFooter(new ClassicsFooter(this));
        rechargeSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (nestedScrollView, i, i1, i2, i3) -> {
            if (i1 >= offest) {
                mToolbar.title.setText("GBT地址");
                mToolbar.bottomLine(true);
            } else {
                mToolbar.title.setText("");
                mToolbar.bottomLine(false);
            }
        });
        recycler.setAdapter(adapter = new QrcodeAddressAdapter());

        for (int i = 0; i < 20; i++) {
            adapter.addData("");
        }
    }

}
