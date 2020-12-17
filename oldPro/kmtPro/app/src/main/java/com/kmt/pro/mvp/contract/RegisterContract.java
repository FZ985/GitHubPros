package com.kmt.pro.mvp.contract;

import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;
import com.kmtlibs.okhttp.callback.Loadding;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-07 14:24
 **/
public interface RegisterContract {

    interface Model {
        Observable<CommonResp<RegisterGetSmsBean>> getMsg(@Query("type") String type, @Query("userTel") String tel, @Query("uuid") String uuid, @Query("mobileCountryCode") String conturyCode);

        Observable<CommonResp<LoginBean>> userRegister(@Query("userTel") String tel, @Query("userPassword") String pwd, @Query("veryCode") String code, @Query("mobileCountryCode") String conturyCode, @Query("user_channel") String channel, @Query("uuid") String uuid, @Query("platform") String platform, @Query("verCode") String verCode);//获取验证码

        Observable<CommonResp<LoginBean>> forgetPwd(@Query("userTel") String tel, @Query("userPassword") String pwd, @Query("veryCode") String code, @Query("uuid") String uuid);//获取验证码

    }

    interface LoginModel extends RegisterContract.Model {
        Observable<CommonResp<LoginBean>> fastLoginNew(@Query("userTel") String tel, @Query("uuid") String uuid,
                                                       @Query("veryCode") String code, @Query("appNum") String appNum);
    }

    interface RegisterView extends BaseView {

        void startTimeDown();

        void registerSuccess();
    }


    interface LoginView extends BaseView {
        //验证码登录——获取验证码按钮
        TextView getFastSmsCodeTv();

        //账号登录——获取验证码按钮
        TextView getNumberSmsCodeTv();

        RelativeLayout getNumberSmsRL();

        EditText getNumberSmsET();

        void loginSuccess();
    }

    abstract class LoginPresenter extends CommonPresenter<LoginView> {
        public abstract void sendMSG(final String type, String conturyCode, String user_phone);

        public abstract void numLogin(String phone, String pwd, String smsCode, Loadding loadding);

        public abstract void fastLogin(String phone, String smsCode);

    }

    abstract class RegisterPresenter extends CommonPresenter<RegisterView> {

        public abstract void sendSms(boolean isForget, String tel, String countryCode);

        public abstract void register(String phone, String sms, String pwd, String countryCode);

        public abstract void forget(String phone, String sms, String pwd, String countryCode);

    }

}
