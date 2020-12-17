package com.kmt.pro.mvp.model;

import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.bean.order.OrderDetailbean;
import com.kmt.pro.http.ApiManager;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.OrderContract;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-29 10:03
 **/
public class OrderModel implements OrderContract.Model {
    @Override
    public Observable<CommonListResp<ExchangeOrderBean>> queryOrderList(String currentPage, String size) {
        return ApiManager.get().kmtApi().queryOrderList(currentPage, size);
    }

    @Override
    public Observable<CommonResp> completeBooking(String bookingId) {
        return ApiManager.get().kmtApi().completeBooking(bookingId);
    }

    @Override
    public Observable<CommonResp> cancleBook(String id, String type) {
        return ApiManager.get().kmtApi().cancleBook(id, type);
    }

    @Override
    public Observable<CommonResp<OrderDetailbean>> queryBookingDetails(String id) {
        return ApiManager.get().kmtApi().queryBookingDetails(id);
    }
}
