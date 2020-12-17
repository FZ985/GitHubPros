package com.kmt.pro.mvp.presenter;

import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.AccountIncomeDetailContract;
import com.kmt.pro.mvp.model.AccountIncomeDetailModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-27 18:06
 **/
public class AccountIncomeDetailPresenterImpl extends AccountIncomeDetailContract.AccountIncomeDetailPresenter {
    private AccountIncomeDetailContract.Model model;

    public AccountIncomeDetailPresenterImpl() {
        model = new AccountIncomeDetailModel();
    }

    @Override
    public void reqCancel(String id) {
        model.cancelWithdraw(id)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onCancelSuccess();
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {

                    }
                });
    }
}
