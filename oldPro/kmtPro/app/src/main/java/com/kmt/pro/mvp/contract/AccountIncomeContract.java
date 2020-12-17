package com.kmt.pro.mvp.contract;

import com.kmt.pro.base.BaseView;
import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.CommonPresenter;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Query;

/**
 * Create by JFZ
 * date: 2020-07-24 11:18
 **/
public interface AccountIncomeContract {

    interface Model {
        Observable<CommonListResp<AccountIncomeBean>> newOrderRecord(@Query("currentPage") String currentPager, @Query("pageSize") String pageSize, @Query("type") String type);
    }

    interface AccountIncomeView extends BaseView {

        void onAccountIncomeDatas(List<AccountIncomeBean> datas, String page, String type);

        void onAccountIncomeErr(String page, String type);
    }

    abstract class AccountIncomePresenter extends CommonPresenter<AccountIncomeView> {
        public abstract void reqAccountList(String page, String type);
    }

}
