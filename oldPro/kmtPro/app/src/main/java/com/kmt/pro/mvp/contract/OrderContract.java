package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.order.ExchangeOrderBean;
import com.kmt.pro.bean.order.OrderDetailbean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-29 9:58
 * 订单契约
 **/
public interface OrderContract {

    interface Model {
        Observable<CommonListResp<ExchangeOrderBean>> queryOrderList(@Query("currentPage") String currentPage, @Query("pageSize") String size);

        Observable<CommonResp> completeBooking(@Query("bookingId") String bookingId);

        Observable<CommonResp> cancleBook(@Query("bookingId") String id, @Query("type") String type);

        Observable<CommonResp<OrderDetailbean>> queryBookingDetails(@Query("bookingId") String id);
    }

    interface OrderView extends BaseView {
        void completeBookingSuccess();
    }

    interface OrderDetailView extends OrderView {
        void onDetailData(OrderDetailbean data);
        void onCancelOrder(int delType);
    }

    interface ExchangeOrderView extends OrderView {
        void onOrderListSucc(int page, List<ExchangeOrderBean> datas);

        void onOrderListErr(int page, String err);
    }

    abstract class OrderPresenter<V extends BaseView> extends CommonPresenter<V> {
        public abstract void completeBooking(String bookingId);

        public abstract void cancleBook(String id, String type);
    }

    abstract class OrderDetailPresenter extends OrderPresenter<OrderDetailView> {
        public abstract void queryBookingDetails(String id);
    }

    abstract class ExchangeOrderPresenter extends OrderPresenter<ExchangeOrderView> {
        public abstract void reqOrderList(int page);
    }


}
