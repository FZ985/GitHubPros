package com.kmt.pro.ui.activity.login;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.event.CountryCodeEvent;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.mvp.contract.RegisterContract;
import com.kmt.pro.mvp.presenter.LoginPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.receive.SendRecvHelper;
import com.kmtlibs.app.utils.Logger;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-03 15:06
 **/
public class LoginActivity extends BaseToolBarActivity<RegisterContract.LoginPresenter> implements RegisterContract.LoginView {
    @BindView(R.id.login_number_rb)
    RadioButton loginNumberRb;
    @BindView(R.id.login_group)
    RadioGroup loginGroup;
    @BindView(R.id.login_num_et)
    EditText loginNumEt;
    @BindView(R.id.login_pwd_et)
    EditText loginPwdEt;
    @BindView(R.id.zh_login_send_messagebtn)
    TextView zhLoginSendMessagebtn;
    @BindView(R.id.zh_login_sms_validate)
    EditText zhLoginSmsValidate;
    @BindView(R.id.login_numlogin_root)
    RelativeLayout login_numlogin_root;
    @BindView(R.id.zh_login_sms)
    RelativeLayout zhLoginSms;
    @BindView(R.id.login_forget_tv)
    TextView loginForgetTv;
    private ViewStub login_vs;

    private String fromPage;
    private String countryCode = "86";

    @Override
    public int getLayoutId() {
        fromPage = getIntent().getStringExtra(KConstant.Key.fromPagerKey);
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        regBroadcastRecv(Actions.ACT_REGISTER_SUCCESS);
        mToolbar.title.setText(getString(R.string.welcome_kmt));
        mToolbar.right.setText(getString(R.string.register));
        mToolbar.right.setOnClickListener(v -> ActivityHelper.register(this, fromPage, false));
        loginGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.login_number_rb:
                    if (login_vs != null) {
                        login_vs.setVisibility(View.GONE);
                    }
                    login_numlogin_root.setVisibility(View.VISIBLE);
                    break;
                case R.id.login_phone_rb:
                    login_numlogin_root.setVisibility(View.GONE);
                    if (login_vs != null) {
                        login_vs.setVisibility(View.VISIBLE);
                    } else {
                        initViewStub();
                    }
                    break;
            }
        });
    }

    private RelativeLayout countryCodeRl;//
    private TextView countryCodeTv, smsLoginGetSmsCodeTv;
    private EditText smsLoginPhoneEt, smsLoginSmsEt;

    private void initViewStub() {
        login_vs = findViewById(R.id.login_vs);
        View view = login_vs.inflate();
        countryCodeRl = view.findViewById(R.id.select_country_relative);
        countryCodeTv = view.findViewById(R.id.login_phone_code);
        smsLoginPhoneEt = view.findViewById(R.id.ed_phone2);
        smsLoginSmsEt = view.findViewById(R.id.fast_login_sms_validate);
        smsLoginGetSmsCodeTv = view.findViewById(R.id.fast_login_send_messagebtn);
        smsLoginGetSmsCodeTv.setOnClickListener(this);
        countryCodeRl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fast_login_send_messagebtn:
                if (!checkFastPhone(smsLoginPhoneEt)) {
                    return;
                }
                String phone = smsLoginPhoneEt.getText().toString().trim();
                smsLoginSmsEt.setFocusable(true);
                smsLoginSmsEt.requestFocus();
                presenter.sendMSG("9", countryCode, phone);
                break;
            case R.id.select_country_relative:
                ActivityHelper.toCountryCodeActivity(getApplicationContext());
                break;
        }
    }

    @OnClick({R.id.zh_login_send_messagebtn, R.id.login_forget_tv, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.zh_login_send_messagebtn:
                if (!checkFastPhone(loginNumEt)) {
                    return;
                }
                String numPhone = loginNumEt.getText().toString().trim();
                zhLoginSmsValidate.setFocusable(true);
                zhLoginSmsValidate.requestFocus();
                presenter.sendMSG("12", countryCode, numPhone);
                break;
            case R.id.login_forget_tv:
                ActivityHelper.register(this, fromPage, true);
                break;
            case R.id.login_btn:
                if (loginNumberRb.isChecked()) {
                    //账号登录
                    if (!checkFastPhone(loginNumEt) || !presenter.checkPassword(loginPwdEt)) {
                        return;
                    }
                    String smsCode = "";
                    if (zhLoginSms.getVisibility() == View.VISIBLE) {
                        if (presenter.checkEditText(zhLoginSmsValidate)) {
                            Tools.showToast("请输入验证码");
                            return;
                        }
                        if (zhLoginSmsValidate.getText().toString().trim().length() < 6) {
                            Tools.showToast("验证码最少6位");
                            return;
                        }
                        smsCode = zhLoginSmsValidate.getText().toString().trim();
                    }
                    presenter.numLogin(loginNumEt.getText().toString().trim(), loginPwdEt.getText().toString().trim(), smsCode, DefLoad.use(this));
                } else {
                    //验证码登录
                    if (!checkFastPhone(smsLoginPhoneEt)) {
                        return;
                    }
                    if (presenter.checkEditText(smsLoginSmsEt)) {
                        Tools.showToast("请输入验证码");
                        return;
                    }
                    String phone = smsLoginPhoneEt.getText().toString().trim();
                    String sms = smsLoginSmsEt.getText().toString().trim();
                    presenter.fastLogin(phone, sms);
                }
                break;
        }
    }

    @Override
    public void loginSuccess() {
        SendRecvHelper.send(BaseApp.getInstance(), Actions.ACT_LOGIN_SUCCESS);
        ActivityHelper.toPageActivity(this, fromPage);
        finish();
    }

    private boolean checkFastPhone(EditText editText) {
        if (presenter.checkEditText(editText)) {
            Tools.showToast("请输入手机号");
            return false;
        }
        if (!presenter.checkPhoneLength(countryCode, editText)) {
            return false;
        }
        return true;
    }

    @Override
    public void initData() {
        mToolbar.bottomLine(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCountryCodeEvent(CountryCodeEvent event) {
        Logger.e("===登录eventbus:" + event.getCountryName() + "," + event.getCountryCode());
        countryCode = event.getCountryCode();
        if (countryCodeTv != null) {
            countryCodeTv.setText(event.getCountryName() + "+" + countryCode);
        }
    }

    @Override
    public void onSafeReceive(Intent intent, String action) {
        super.onSafeReceive(intent, action);
        if (action.equals(Actions.ACT_REGISTER_SUCCESS)) {
            finish();
        }
    }

    @Override
    public TextView getFastSmsCodeTv() {
        return smsLoginGetSmsCodeTv;
    }

    @Override
    public TextView getNumberSmsCodeTv() {
        return zhLoginSendMessagebtn;
    }

    @Override
    public RelativeLayout getNumberSmsRL() {
        return zhLoginSms;
    }

    @Override
    public EditText getNumberSmsET() {
        return zhLoginSmsValidate;
    }

    @Override
    protected RegisterContract.LoginPresenter getPresenter() {
        return new LoginPresenterImpl();
    }
}
