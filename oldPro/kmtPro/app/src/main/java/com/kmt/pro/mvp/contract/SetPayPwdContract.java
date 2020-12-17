package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-20 14:42
 **/
public interface SetPayPwdContract {
    interface Model {
        Observable<CommonResp> setPayPwd(@Query("userPayPassword") String old, @Query("type") String type);

        Observable<CommonResp> foundPayPwd(@Query("userPayPassword") String old, @Query("veryCode") String code, @Query("type") String type);
    }

    interface SetPayPwdView extends BaseView {
        void setPayPwdSuccess();

        void foundPayPwdSuccess();
    }

    abstract class SetPayPwdPresenter extends CommonPresenter<SetPayPwdView> {
        public abstract void reqSetPayPwd(String pwd, String type);

        public abstract void reqFoundPayPwd(String pwd, String smsCode, String type);
    }
}
