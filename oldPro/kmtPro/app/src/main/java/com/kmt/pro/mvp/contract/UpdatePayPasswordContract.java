package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-17 18:27
 **/
public interface UpdatePayPasswordContract {

    interface Model {
        Observable<CommonResp> verifyPayPwd(@Query("oldPayPassword") String old, @Query("type") String type);

        Observable<CommonResp> updatePayPwd(@Query("oldPayPassword") String old, @Query("newPayPassword") String newpwd, @Query("type") String type);
    }

    interface UpdatePayPasswordView extends BaseView {

        void checkPayPwdSuccess(String old);

        void updatePwdSuccess();
    }

    abstract class PasswordPresenter extends CommonPresenter<UpdatePayPasswordView> {
        public abstract void checkPayPwd(String old, String type);

        public abstract void updatePayPwd(String old, String newpwd, String type);
    }
}
