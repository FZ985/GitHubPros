package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.OrderContract;
import com.kmt.pro.mvp.model.OrderModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-29 10:04
 **/
public class ExchangeOrderPresenterImpl extends OrderContract.ExchangeOrderPresenter {
    private OrderContract.Model model;

    public ExchangeOrderPresenterImpl() {
        model = new OrderModel();
    }

    @Override
    public void reqOrderList(int page) {
        model.queryOrderList(page + "", "10")
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<ExchangeOrderBean>>() {
                    @Override
                    public void onResponse(CommonListResp<ExchangeOrderBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onOrderListSucc(page, data.getLIST());
                        } else if (data.getSTATUS().equals("3")) {
                            Login((bool) -> {
                                if (bool) {
                                    reqOrderList(page);
                                } else getView().onOrderListErr(page, data.MSG);
                            });
                        } else {
                            getView().onOrderListErr(page, data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        getView().onOrderListErr(page, e.getMessage());
                    }
                });
    }

    @Override
    public void completeBooking(String bookingId) {
        model.completeBooking(bookingId)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            Tools.showToast("交易完成");
                            getView().completeBookingSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }

    @Override
    public void cancleBook(String id, String type) {
        model.cancleBook(id, type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            if ("0".equals(type)) {
                                Tools.showToast("取消兑换成功");
                            } else if ("1".equals(type)) {
                                Tools.showToast("删除兑换成功");
                            }
                            getView().completeBookingSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }
}
