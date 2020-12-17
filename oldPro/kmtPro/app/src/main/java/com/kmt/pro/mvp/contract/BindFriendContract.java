package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.OneKeyBuyBean;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-16 14:31
 **/
public interface BindFriendContract {

    interface Model {
        Observable<CommonResp<OneKeyBuyBean>> bindFriend(@Query("higherLevel") String code);
    }

    interface BindFriendView extends BaseView {
        void onBindSuccess(OneKeyBuyBean data,String code);
    }

    abstract class BindFriendPresenter extends CommonPresenter<BindFriendView> {
        public abstract void bindFriend(String code);
    }
}
