package com.kmt.pro.mvp.presenter;

import com.kmt.pro.http.response.CommonResp;
import com.kmt.pro.mvp.contract.UpdatePayPasswordContract;
import com.kmt.pro.mvp.model.UpdatePasswordModel;
import com.kmt.pro.utils.Tools;
import com.lxbuytimes.kmtapp.retrofit.callback.ICall;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.retrofit.tool.RxHelper;

/**
 * Create by JFZ
 * date: 2020-07-17 18:29
 **/
public class PasswordPresenterImpl extends UpdatePayPasswordContract.PasswordPresenter {

    private UpdatePayPasswordContract.Model model;

    public PasswordPresenterImpl() {
        model = new UpdatePasswordModel();
    }

    @Override
    public void checkPayPwd(String old, String type) {
        model.verifyPayPwd(old, type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>() {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().checkPayPwdSuccess(old);
                        } else if (data.getSTATUS().equals("3")) {
                            Login((b) -> {
                                if (b) checkPayPwd(old, type);
                                else Tools.showToast(data.MSG);
                            });
                        } else Tools.showToast(data.MSG);
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }

    @Override
    public void updatePayPwd(String old, String newpwd, String type) {
        model.updatePayPwd(old, newpwd, type)
                .compose(RxHelper.observableIO2Main())
                .subscribe(new ICall<CommonResp>(DefLoad.use(getActivity())) {
                    @Override
                    public void onResponse(CommonResp data) {
                        if (!isViewAttached()) return;
                        if (data.getSTATUS().equals("0")) {
                            getView().updatePwdSuccess();
                        } else {
                            Tools.showToast(data.MSG);
                        }
                    }

                    @Override
                    public void onCallError(int code, Throwable e) {
                        logFailMsg(code, e.getMessage());
                    }
                });
    }
}
