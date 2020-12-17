package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.CommonPresenter;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-27 18:03
 **/
public interface AccountIncomeDetailContract {
    interface Model {
        Observable<CommonResp> cancelWithdraw(@Query("withdrawId") String id);
    }

    interface AccountIncomeDetailView extends BaseView {
        void onCancelSuccess();
    }

    abstract class AccountIncomeDetailPresenter extends CommonPresenter<AccountIncomeDetailView> {
        public abstract void reqCancel(String id);
    }
}
