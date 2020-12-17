package com.kmt.pro.ui.activity.login;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.event.CountryCodeEvent;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.RegisterContract;
import com.kmt.pro.mvp.presenter.RegisterPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.web.WebJump;
import com.kmtlibs.app.receive.SendRecvHelper;
import com.kmtlibs.app.utils.Logger;
import com.lxbuytimes.kmtapp.span.Span;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-03 15:21
 **/
public class RegisterActivity extends BaseToolBarActivity<RegisterContract.RegisterPresenter> implements RegisterContract.RegisterView {

    @BindView(R.id.select_country_relative)
    RelativeLayout selectCountryRelative;
    @BindView(R.id.regist_edit_phone)
    EditText registEditPhone;
    @BindView(R.id.regist_phonecode)
    TextView regist_phonecode;
    @BindView(R.id.send_messagebtn)
    TextView sendMessagebtn;
    @BindView(R.id.edit_yan)
    EditText editYan;
    @BindView(R.id.edit_pwd)
    EditText editPwd;
    @BindView(R.id.reg_message)
    TextView regMessage;
    @BindView(R.id.register_commit)
    Button registerCommit;
    private boolean isForget;
    private String fromPage;
    private String phoneCode = "86";

    @Override
    public int getLayoutId() {
        fromPage = getIntent().getStringExtra(KConstant.Key.fromPagerKey);
        isForget = getIntent().getBooleanExtra(KConstant.Key.isForgetKey, false);
        return R.layout.activity_register;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mToolbar.bottomLine(false);
        Span.impl()
                .append(Span.builder("注册即表示同意"))
                .append(Span.builder(getString(R.string.zfw_register)).textColor(ContextCompat.getColor(this, R.color.colorPrimary)).click((text, widget) -> {
                    WebJump.toRegisterWeb(this);
                }))
                .into(regMessage);
        if (isForget) {
            regMessage.setVisibility(View.GONE);
            mToolbar.title.setText(getString(R.string.forgetpwd));
        } else {
            mToolbar.title.setText(getString(R.string.register));
        }
    }

    private String phone;

    @OnClick({R.id.select_country_relative, R.id.send_messagebtn, R.id.register_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_country_relative:
                ActivityHelper.toCountryCodeActivity(this);
                break;
            case R.id.send_messagebtn:
                if (presenter.checkEditText(registEditPhone)) {
                    Tools.showToast("请输入手机号");
                    return;
                }
                if (presenter.checkPhoneLength(phoneCode, registEditPhone)) {
                    phone = registEditPhone.getText().toString().trim();
                    editYan.setFocusable(true);
                    editYan.requestFocus();
                    presenter.sendSms(isForget, phone, phoneCode);
                }
                break;
            case R.id.register_commit:
                if (presenter.checkEditText(registEditPhone)) {
                    Tools.showToast("请输入手机号");
                    return;
                }
                if (!presenter.checkPhoneLength(phoneCode, registEditPhone)) {
                    return;
                }
                if (presenter.checkEditText(editYan)) {
                    Tools.showToast("请输入验证码");
                    return;
                }
                String newphone = registEditPhone.getText().toString().trim();
                if (!TextUtils.isEmpty(phone) && !phone.equals(newphone)) {
                    Tools.showToast("手机号与验证码不匹配");
                    return;
                }
                if (presenter.checkEditText(editPwd)) {
                    Tools.showToast("请输入密码");
                    return;
                }
                if (presenter.checkPassword(editPwd)) {
                    if (!isForget) {
                        //用户注册
                        presenter.register(phone, editYan.getText().toString().trim(), editPwd.getText().toString().trim(), phoneCode);
                    } else {
                        //忘记密码
                        presenter.forget(phone, editYan.getText().toString().trim(), editPwd.getText().toString().trim(), phoneCode);
                    }
                }
                break;
        }
    }

    @Override
    public void registerSuccess() {
        Tools.showToast("修改成功");
        SendRecvHelper.send(this, Actions.ACT_REGISTER_SUCCESS);
        ActivityHelper.toPageActivity(this, fromPage);
        finish();
    }

    private CountDownTimer timer;

    @Override
    public void startTimeDown() {
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFinishing()) {
                    sendMessagebtn.setClickable(false);
                    sendMessagebtn.setEnabled(false);
                    sendMessagebtn.setText(millisUntilFinished / 1000 + "秒");
                    sendMessagebtn.setTextColor(getResources().getColor(R.color.black33));
                }
            }

            @Override
            public void onFinish() {
                if (!isFinishing()) {
                    sendMessagebtn.setText("获取验证码");
                    sendMessagebtn.setClickable(true);
                    sendMessagebtn.setEnabled(true);
                    sendMessagebtn.setTextColor(getResources().getColor(R.color.black66));
                }
            }
        };
        timer.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountryCodeEvent(CountryCodeEvent event) {
        Logger.e("===注册eventbus:" + event.getCountryName() + "," + event.getCountryCode());
        phoneCode = event.getCountryCode();
        regist_phonecode.setText(event.getCountryName() + "+" + phoneCode);
    }

    @Override
    protected RegisterContract.RegisterPresenter getPresenter() {
        return new RegisterPresenterImpl();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        super.onDestroy();
    }
}
