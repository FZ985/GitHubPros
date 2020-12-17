package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.order.OrderDetailbean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.OrderContract;
import com.kmt.pro.mvp.model.OrderModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-08-05 15:55
 **/
public class OrderDetailPresenterImpl extends OrderContract.OrderDetailPresenter {
    private OrderContract.Model model;

    public OrderDetailPresenterImpl() {
        model = new OrderModel();
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
                            getView().onCancelOrder(Integer.valueOf(type));
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        Tools.showToast(e.getMessage());
                    }
                });
    }

    @Override
    public void queryBookingDetails(String id) {
        model.queryBookingDetails(id)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp<OrderDetailbean>>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp<OrderDetailbean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            if (data.OBJECT != null) {
                                getView().onDetailData(data.OBJECT);
                            }
                        } else {
                            Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        Tools.showToast(e.getMessage());
                    }
                });
    }
}
