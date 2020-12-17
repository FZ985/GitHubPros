package com.kmt.pro.ui.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.LoginBean;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.mvp.contract.ForgetPayPwdContract;
import com.kmt.pro.mvp.presenter.ForgetPayPwdPresenterImpl;
import com.kmt.pro.utils.Check;
import com.kmt.pro.utils.Tools;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-20 11:11
 **/
public class PayPwdForgetActivity extends BaseToolBarActivity<ForgetPayPwdContract.ForgetPayPwdPresenter> implements ForgetPayPwdContract.ForgetPayPwdView {
    @BindView(R.id.forgetmobile_text)
    TextView forgetmobileText;
    @BindView(R.id.forget_sms_code_et)
    EditText forgetSmsCodeEt;
    @BindView(R.id.forget_sms_send)
    TextView forgetSmsSend;
    String mobile;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_pwd_forget;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("找回密码");
    }

    @Override
    public void initData() {
        LoginBean data = Login.get().getData();
        if (data != null && !TextUtils.isEmpty(data.userTel)) {
            mobile = data.userTel;
            forgetmobileText.setText("请输入" + Check.getGoneCenterMobile(mobile) + "收到的短信校检码。");
        }

    }

    @OnClick({R.id.forget_sms_send, R.id.forget_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_sms_send:
                if (TextUtils.isEmpty(mobile)) {
                    Tools.showToast("手机号为空");
                    return;
                }
                presenter.sendSms("3", mobile, Login.get().getData().mobileCountryCode);
                break;
            case R.id.forget_btn:
                String smsCode = forgetSmsCodeEt.getText().toString().trim();
                if (TextUtils.isEmpty(smsCode)) {
                    Tools.showToast("请输入验证码");
                    return;
                }
                if (smsCode.length() < 6) {
                    Tools.showToast("验证码为6位");
                    return;
                }
                presenter.checkSmsCode(smsCode);
                break;
        }
    }

    private CountDownTimer timer;

    @Override
    public void sendSmsCodeSuccess() {
        Tools.showToast("发送成功!");
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFinishing()) {
                    forgetSmsSend.setClickable(false);
                    forgetSmsSend.setEnabled(false);
                    forgetSmsSend.setText(millisUntilFinished / 1000 + "秒");
                }
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    forgetSmsSend.setClickable(true);
                    forgetSmsSend.setEnabled(true);
                    forgetSmsSend.setText("获取验证码");
                }
            }
        };
        timer.start();
    }

    @Override
    public void sendCheck() {
        ActivityHelper.toSetPayPwdActivity(this, forgetSmsCodeEt.getText().toString().trim(), KConstant.BackPage.normal);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer.onFinish();
        }
        timer = null;
        super.onDestroy();
    }

    @Override
    protected ForgetPayPwdContract.ForgetPayPwdPresenter getPresenter() {
        return new ForgetPayPwdPresenterImpl();
    }
}
