package com.kmt.pro.ui.activity;

import android.text.TextUtils;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.mvp.contract.SetPayPwdContract;
import com.kmt.pro.mvp.presenter.SetPayPwdPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.IPasswordView2;

import butterknife.BindView;
import butterknife.OnClick;
import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-20 14:02
 * 设置支付密码
 **/
public class SetPayPasswordActivity extends BaseToolBarActivity<SetPayPwdContract.SetPayPwdPresenter> implements SetPayPwdContract.SetPayPwdView {
    @BindView(R.id.set_pay_pwed)
    IPasswordView2 setPayPwed;
    @BindView(R.id.confirm_pay_pwed)
    IPasswordView2 confirmPayPwed;
    private String fromPage;
    private String smsCode;

    @Override
    public int getLayoutId() {
        fromPage = getIntent().getStringExtra(KConstant.Key.fromPagerKey);
        smsCode = getIntent().getStringExtra(KConstant.Key.key_smsCode);
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("设置支付密码");
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.pay_pwd_set_btn)
    public void onViewClicked() {
        String pwd1 = setPayPwed.getPassword().trim();
        String pwd2 = confirmPayPwed.getPassword().trim();
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            Tools.showToast("请输入支付密码");
            return;
        }
        if (!pwd1.equals(pwd2)) {
            Tools.showToast("两次输入密码不一致,请重新输入!");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            presenter.reqSetPayPwd(Md5Utils.MD5(pwd1), "1");
        } else {
            presenter.reqFoundPayPwd(Md5Utils.MD5(pwd1), smsCode, "3");
        }
    }

    @Override
    public void setPayPwdSuccess() {
        Login.get().getData().setUserPayStats("1");
        Tools.showToast("设置成功");
        ActivityHelper.toPageActivity(this, fromPage);
        finish();
    }

    @Override
    public void foundPayPwdSuccess() {
        Login.get().getData().setUserPayStats("1");
        Tools.showToast("设置成功");
        finish();
    }

    @Override
    protected SetPayPwdContract.SetPayPwdPresenter getPresenter() {
        return new SetPayPwdPresenterImpl();
    }
}
