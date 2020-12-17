package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;

/**
 * Create by JFZ
 * date: 2020-07-16 13:33
 **/
public interface SettingContract {

    interface Model {
        Observable<CommonResp> userLogout();
    }

    interface SetView extends BaseView {

        void exitSuccess();
    }

    abstract class SetPresenter extends CommonPresenter<SetView> {
        public abstract void exitApp();
    }
}
