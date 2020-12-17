package com.kmt.pro.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.mvp.contract.UpdatePayPasswordContract;
import com.kmt.pro.mvp.presenter.PasswordPresenterImpl;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.widget.IPasswordView2;
import com.kmtlibs.app.dialog.NativeDlg;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-16 16:20
 **/
public class UpdatePayPwdActivity extends BaseToolBarActivity<UpdatePayPasswordContract.PasswordPresenter> implements UpdatePayPasswordContract.UpdatePayPasswordView {
    @BindView(R.id.pay_oldpwd)
    IPasswordView2 payOldpwd;
    @BindView(R.id.pay_newpwd)
    IPasswordView2 payNewpwd;
    @BindView(R.id.pay_newpwd_ll)
    LinearLayout payNewpwdLl;
    @BindView(R.id.pay_btn)
    Button payBtn;
    private boolean isToucheSelf;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_pay_pwd;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("修改支付设置");
        payNewpwd.setPasswordListener(new IPasswordView2.PasswordListener() {
            @Override
            public void passwordChange(IPasswordView2 view, String changeText) {
                payBtn.setText("下一步");
            }

            @Override
            public void passwordComplete(IPasswordView2 view) {
                payBtn.setText("完成");
            }

            @Override
            public void passwordCompleteFinish(IPasswordView2 view, String pwd, boolean isComplete) {

            }
        });
        payOldpwd.setOnClickListener(v -> {
            if (payNewpwdLl.getVisibility() == View.GONE) return;
            if (isToucheSelf) {
                isToucheSelf = false;
                selectAnim(payOldpwd, true);
                selectAnim(payNewpwd, false);
            }
        });
        payNewpwd.setOnClickListener(v -> {
            if (!isToucheSelf) {
                isToucheSelf = true;
                selectAnim(payOldpwd, false);
                selectAnim(payNewpwd, true);
            }
        });
    }

    @Override
    public void initData() {

    }

    @OnClick(R.id.pay_btn)
    public void onViewClicked() {
        if (payNewpwdLl.getVisibility() == View.VISIBLE) {
            //显示
            String newPwd = payNewpwd.getPassword();
            if (checkPwd(newPwd)) {
                NativeDlg.create(this)
                        .msg("确定修改支付密码?")
                        .cancelClickListener("取消", (dialog, v) -> dialog.dismiss())
                        .okClickListener("确定", ContextCompat.getColor(this, R.color.colorPrimary), (dialog, v) -> {
                            presenter.updatePayPwd(oldPwd, Md5Utils.MD5(newPwd), "2");
                            dialog.dismiss();
                        })
                        .show();
            }
        } else {
            //未显示
            String old = payOldpwd.getPassword();
            if (checkPwd(old)) {
                presenter.checkPayPwd(Md5Utils.MD5(old), "4");
            }
        }
    }

    private boolean checkPwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            Tools.showToast("密码不能为空");
            return false;
        }
        if (pwd.length() < 6) {
            Tools.showToast("密码必须是6位");
            return false;
        }
        return true;
    }

    private String oldPwd;

    @Override
    public void checkPayPwdSuccess(String old) {
        oldPwd = old;
        payNewpwdLl.setVisibility(View.VISIBLE);
        isToucheSelf = false;
        selectAnim(payNewpwd, false);
    }

    @Override
    public void updatePwdSuccess() {
        Tools.showToast("修改成功");
        mHandler.postDelayed(() -> finish(), 600);
    }

    @Override
    protected UpdatePayPasswordContract.PasswordPresenter getPresenter() {
        return new PasswordPresenterImpl();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void selectAnim(View view, boolean select) {
        AnimatorSet set = new AnimatorSet();
        float[] vals = select ? new float[]{0.8f, 1f} : new float[]{1f, 0.95f};
        ObjectAnimator x = ObjectAnimator.ofFloat(view, "scaleX", vals);
        ObjectAnimator y = ObjectAnimator.ofFloat(view, "scaleY", vals);
        set.playTogether(x, y);
        set.setDuration(200);
        set.start();
    }

}
