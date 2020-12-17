package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-20 11:57
 **/
public interface ForgetPayPwdContract {
    interface Model {
        Observable<CommonResp> checkSmsCode(@Query("veryCode") String code);
    }

    interface ForgetPayPwdView extends BaseView {
        void sendSmsCodeSuccess();

        void sendCheck();
    }

    abstract class ForgetPayPwdPresenter extends CommonPresenter<ForgetPayPwdView> {
        public abstract void sendSms(String type, String mobile, String countryCode);

        public abstract void checkSmsCode(String code);
    }
}
