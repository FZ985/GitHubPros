package com.kmt.pro.ui.activity.order;

import com.kmt.pro.R;
import com.kmt.pro.adapter.order.ExchangeOrderListAdapter;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.callback.ExCahangeOrderClickCall;
import com.kmt.pro.event.ConfimOrderEvent;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.mvp.contract.OrderContract;
import com.kmt.pro.mvp.presenter.ExchangeOrderPresenterImpl;
import com.kmt.pro.widget.Statelayout;
import com.kmtlibs.adapter.base.listener.OnLoadMoreListener;
import com.kmtlibs.app.dialog.NativeDlg;
import com.kmtlibs.app.utils.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Create by JFZ
 * date: 2020-07-28 17:56
 **/
public class ExchangeOrderActivity extends BaseToolBarActivity<OrderContract.ExchangeOrderPresenter> implements ExCahangeOrderClickCall, OnLoadMoreListener, OrderContract.ExchangeOrderView {
    @BindView(R.id.eo_recycler)
    RecyclerView eoRecycler;
    @BindView(R.id.eo_state)
    Statelayout eoState;
    private ExchangeOrderListAdapter adapter;
    private int page;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exchange_order;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("兑换订单");
        eoRecycler.setLayoutManager(new LinearLayoutManager(this));
        eoRecycler.setAdapter(adapter = new ExchangeOrderListAdapter(this));
        adapter.setOnItemClickListener((quickAdapter, view, position) -> {
            ExchangeOrderBean item = adapter.getItem(position);
            if (item != null) {
                ActivityHelper.toOrderDetailActivity(this, item.bookingId, true);
            }
        });
    }

    @Override
    public void initData() {
        ExchangeOrderBean e1 = new ExchangeOrderBean();
        e1.bookingOrderNo = "111111111";
        e1.houseName = "222222";
        e1.createUserName = "33333";
        e1.bookingTimesSecondsLong = "4444444";
        e1.bookingStatus = "1";
        adapter.addData(e1);

        ExchangeOrderBean e2 = new ExchangeOrderBean();
        e2.bookingOrderNo = "111111111";
        e2.houseName = "222222";
        e2.createUserName = "33333";
        e2.bookingTimesSecondsLong = "4444444";
        e2.bookingStatus = "2";
        e2.flag = false;
        adapter.addData(e2);

        ExchangeOrderBean e2d2 = new ExchangeOrderBean();
        e2d2.bookingOrderNo = "111111111";
        e2d2.houseName = "222222";
        e2d2.createUserName = "33333";
        e2d2.bookingTimesSecondsLong = "4444444";
        e2d2.bookingStatus = "2";
        e2d2.flag = true;
        adapter.addData(e2d2);

        ExchangeOrderBean e3 = new ExchangeOrderBean();
        e3.bookingOrderNo = "111111111";
        e3.houseName = "222222";
        e3.createUserName = "33333";
        e3.bookingTimesSecondsLong = "4444444";
        e3.bookingStatus = "3";
        adapter.addData(e3);

        ExchangeOrderBean e4 = new ExchangeOrderBean();
        e4.bookingOrderNo = "111111111";
        e4.houseName = "222222";
        e4.createUserName = "33333";
        e4.bookingTimesSecondsLong = "4444444";
        e4.bookingStatus = "4";
        adapter.addData(e4);

        ExchangeOrderBean e5 = new ExchangeOrderBean();
        e5.bookingOrderNo = "111111111";
        e5.houseName = "222222";
        e5.createUserName = "33333";
        e5.bookingTimesSecondsLong = "4444444";
        e5.bookingStatus = "31";
        adapter.addData(e5);

        ExchangeOrderBean e6 = new ExchangeOrderBean();
        e6.bookingOrderNo = "111111111";
        e6.houseName = "222222";
        e6.createUserName = "33333";
        e6.bookingTimesSecondsLong = "4444444";
        e6.bookingStatus = "35";
        adapter.addData(e6);
    }

    @Override
    public void onOrderListSucc(int page, List<ExchangeOrderBean> datas) {
        if (isFinishing()) return;
        eoState.showContent();
        if (page == 1) {
            if (datas.size() > 0) {
                adapter.setNewInstance(datas);
                adapter.getLoadMoreModule().setOnLoadMoreListener(this);
            } else eoState.showEmpty();
        } else {
            if (datas.size() > 0) {
                adapter.addData(datas);
                adapter.getLoadMoreModule().loadMoreComplete();
            } else adapter.getLoadMoreModule().loadMoreEnd(true);
        }
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(() -> {
            if (!isFinishing()) {
                page = page + 1;
                presenter.reqOrderList(page);
            }
        }, 250);
    }

    @Override
    public void onOrderListErr(int page, String err) {
        if (isFinishing()) return;
        if (page != 1) {
            adapter.getLoadMoreModule().loadMoreEnd(true);
        } else {
            eoState.showEmpty();
        }
    }

    @Override
    public void completeBookingSuccess() {
        page = page + 1;
        presenter.reqOrderList(page);
    }

    @Override
    public void status1(String orderId, int switchFrom) {
        ActivityHelper.toConfimOrderActivity(this, orderId, switchFrom);
    }

    @Override
    public void status2(String orderId) {
        presenter.completeBooking(orderId);
    }

    @Override
    public void status3(String orderId, String investorsCode, String houseName) {
        ActivityHelper.toOrderEvaluateActivity(this, orderId, investorsCode, houseName);
    }

    @Override
    public void showDialog(String message, String delType, String orderId) {
        NativeDlg.create(this)
                .msg(message)
                .msgTextSize(17)
                .cancelClickListener("取消", (dialog, v) -> dialog.dismiss())
                .okClickListener("确定", ContextCompat.getColor(this, R.color.colorAccent), (dialog, v) -> {
                    presenter.cancleBook(orderId, delType);
                    dialog.dismiss();
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConfimOrderEvent(ConfimOrderEvent event) {
        Logger.e("====提交订单回调:" + event.getType());
        if (event.getType() == 2 && !isFinishing()) {
            completeBookingSuccess();
        }
    }

    @Override
    protected OrderContract.ExchangeOrderPresenter getPresenter() {
        return new ExchangeOrderPresenterImpl();
    }
}
