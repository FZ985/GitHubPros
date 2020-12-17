package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.SplashBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-06-29 17:36
 **/
public interface SplashContract {

    interface Model {
        Observable<CommonResp<SplashBean>> getSplash();
    }

    interface SplashView extends BaseView {

        void background(String image);
    }

    abstract class SplashPresenter extends CommonPresenter<SplashView> {

        public abstract void queryBackground();
    }

}
