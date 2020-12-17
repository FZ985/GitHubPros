package com.kmt.pro.ui.activity;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.Login;
import com.kmt.pro.mvp.contract.RegisterContract;
import com.kmt.pro.mvp.presenter.RegisterPresenterImpl;
import com.kmt.pro.utils.Tools;

import butterknife.BindView;
import butterknife.OnClick;
import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-16 15:06
 **/
public class SetLoginPwdActivity extends BaseToolBarActivity<RegisterContract.RegisterPresenter> implements RegisterContract.RegisterView {
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.edit_yan)
    EditText editYan;
    @BindView(R.id.send_messagebtn)
    TextView sendMessagebtn;
    @BindView(R.id.edit_pwd)
    EditText editPwd;
    private String pwd, code, userTel, mobileCountryCode;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setlogin_pwd;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        if (Login.get().getData() != null) {
            userTel = Login.get().getData().userTel;
            mobileCountryCode = Login.get().getData().mobileCountryCode;
            if ("0".equals(Login.get().getData().userPasswordFlag)) {
                mToolbar.title.setText("修改登录密码");
            } else {
                mToolbar.title.setText("设置登录密码");
            }
            if (!TextUtils.isEmpty(userTel)) {
                try {
                    phone.setText(userTel.substring(0, 3) + "****" + userTel.substring(7, userTel.length()));
                } catch (Exception e) {
                }
            }
        }
    }

    @OnClick({R.id.send_messagebtn, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.send_messagebtn:
                if (!TextUtils.isEmpty(userTel)) {
                    editYan.setFocusable(true);
                    editYan.requestFocus();
                    presenter.sendSms(true, userTel, mobileCountryCode);
                } else {
                    Tools.showToast("手机号码不能为空");
                }
                break;
            case R.id.btn_sure:
                if (checkLoginData()) {
                    //验证码是否正确
                    final String passWord = Md5Utils.MD5(pwd);
                    presenter.forget(userTel, code, passWord, mobileCountryCode);
                }
                break;
        }
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

    @Override
    public void registerSuccess() {
        if ("0".equals(Login.get().getData().userPasswordFlag)) {
            Tools.showToast("修改登录密码成功");
        } else {
            Tools.showToast("设置登录密码成功");
            Login.get().getData().setUserPasswordFlag("0");
        }
        finish();
    }

    // 检测输入数据准确性
    private boolean checkLoginData() {
        code = editYan.getText().toString().trim();
        pwd = editPwd.getText().toString().trim();

        if (TextUtils.isEmpty(userTel)) {
            Tools.showToast("手机号码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(code)) {
            Tools.showToast("验证码不能为空");
            return false;
        }

        if (TextUtils.isEmpty(pwd)) {
            Tools.showToast("密码不能为空");
            return false;
        }

        if (code.length() < 6) {
            Tools.showToast("验证码为6位");
            return false;
        }

        if (pwd.length() < 6) {
            Tools.showToast("密码最少6位");
            return false;
        }
        if (pwd.length() > 16) {
            Tools.showToast("密码不能超过16位");
            return false;
        }
        return true;
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
