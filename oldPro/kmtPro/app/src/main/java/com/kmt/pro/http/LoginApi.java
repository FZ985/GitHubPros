package com.kmt.pro.http;

import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.bean.login.RegisterGetSmsBean;
import com.kmt.pro.http.response.CommonResp;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-03 15:05
 **/
public interface LoginApi {

    //多设备登陆接口 isAudio 是否自动登录  1是 空字符串为否
    // 以下是设备登陆和注册相关的接口
    @POST("security/multiDevLogin.do")
    Observable<CommonResp<LoginBean>> multiDevLogin(@Query("userTel") String tel, @Query("userPassword") String pwd, @Query("uuid") String uuid,
                                                    @Query("veryCode") String code, @Query("isAuto") String isAuto);

    // 快速登陆 //上一次是快捷登陆的用户 下一次通过这个做自动登陆
    @POST("applicationLogin.do")
    Observable<CommonResp<LoginBean>> applicationLogin(@Query("userTel") String tel, @Query("uuid") String uuid,
                                                       @Query("codePassword") String codePassword, @Query("type") String type);

    //获取验证码
    //获取验证码接口,0获取注册的验证码，1忘记密码的时候获取的验证码，2验证手机号码的验证码，3找回支付密码的验证码,6提现验证码,9是快速登陆
    @POST("userVerificationCode.do")
    Observable<CommonResp<RegisterGetSmsBean>> getMsg(@Query("type") String type, @Query("userTel") String tel, @Query("uuid") String uuid, @Query("mobileCountryCode") String conturyCode);

    //注册接口
    @POST("security/userRegister.do")
    Observable<CommonResp<LoginBean>> userRegister(@Query("userTel") String tel, @Query("userPassword") String pwd, @Query("veryCode") String code, @Query("mobileCountryCode") String conturyCode, @Query("user_channel") String channel, @Query("uuid") String uuid, @Query("platform") String platform, @Query("verCode") String verCode);//获取验证码

    //忘记密码接口
    @POST("userForgetPassword.do")
    Observable<CommonResp<LoginBean>> forgetPwd(@Query("userTel") String tel, @Query("userPassword") String pwd, @Query("veryCode") String code, @Query("uuid") String uuid);//获取验证码


    //通过手机号和验证码登陆接口
    @POST("security/fastLogin.do")
    Observable<CommonResp<LoginBean>> fastLoginNew(@Query("userTel") String tel, @Query("uuid") String uuid,
                                                   @Query("veryCode") String code, @Query("appNum") String appNum);
}
