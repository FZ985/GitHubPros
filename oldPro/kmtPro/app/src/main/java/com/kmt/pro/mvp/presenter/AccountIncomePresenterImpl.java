package com.kmt.pro.mvp.presenter;

import com.kmt.pro.bean.AccountIncomeBean;
import com.kmt.pro.http.response.CommonListResp;
import com.kmt.pro.mvp.contract.AccountIncomeContract;
import com.kmt.pro.mvp.model.AccountIncomeModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-24 11:27
 **/
public class AccountIncomePresenterImpl extends AccountIncomeContract.AccountIncomePresenter {
    private AccountIncomeContract.Model model;

    public AccountIncomePresenterImpl() {
        model = new AccountIncomeModel();
    }

    @Override
    public void reqAccountList(String page, String type) {
        model.newOrderRecord(page, "15", type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonListResp<AccountIncomeBean>>(page.equals("1") ? DefLoad.use(getActivity()) : null) {
                    @Override
                    public void onResponse(CommonListResp<AccountIncomeBean> data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().onAccountIncomeDatas(data.getLIST(), page, type);
                        } else if (data.getSTATUS().equals("3")) {
                            Login((bool) -> {
                                if (bool) {
                                    reqAccountList(page, type);
                                } else {
                                    getView().onAccountIncomeErr(page, type);
                                    Tools.showToast(data.MSG);
                                }
                            });
                        } else {
                            getView().onAccountIncomeErr(page, type);
                            Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        if (!isViewAttached()) return;
                        Tools.showToast("网络拥堵,请稍后重试");
                        getView().onAccountIncomeErr(page, type);
                    }
                });
    }
}
