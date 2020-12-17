package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;
import com.kmtlibs.okhttp.callback.Loadding;

import java.io.File;

import io.reactivex.Observable;
import retrofit2.http.Field;

/**
 * Create by JFZ
 * date: 2020-08-05 11:22
 **/
public interface OrderEvaluateContract {
    interface Model {
        Observable<CommonResp> commitBooking(@Field("bookingId") String id, @Field("investorsCode") String code, @Field("content") String content, @Field("images") String image);
    }

    interface OrderEvaluateView extends BaseView {
        void commitSuccess();

        void uploadImageSuccess(int index, String url);

        void uploadImageErr(int index, String err);
    }

    abstract class OrderEvaluatePresenter extends CommonPresenter<OrderEvaluateView> {
        public abstract void commitBooking(Loadding loadding, String id, String code, String content, String image);

        public abstract void uploadImages(int index, File file);
    }
}
