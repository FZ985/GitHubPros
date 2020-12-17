package com.kmt.pro.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.AppVersionBean;
import com.kmt.pro.callback.HttpCallback;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.Login;
import com.kmt.pro.helper.UrlJumpHelper;
import com.kmt.pro.mvp.contract.SettingContract;
import com.kmt.pro.mvp.presenter.SettingPresenterImpl;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.ui.dialog.AppUpdateDialog;
import com.kmt.pro.utils.DeviceInfoUtils;
import com.kmt.pro.utils.Tools;
import com.kmt.pro.web.WebJump;
import com.kmtlibs.app.dialog.NativeDlg;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;
import com.lxbuytimes.kmtapp.retrofit.def.DefLoad;
import com.lxbuytimes.kmtapp.span.Span;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-13 18:28
 **/
public class SettingActivity extends BaseToolBarActivity<SettingContract.SetPresenter> implements SettingContract.SetView, HttpCallback.AppVersionCallback {
    @BindView(R.id.set_login_rl)
    RelativeLayout setLoginRl;
    @BindView(R.id.set_pwd_tv)
    TextView setPwdTv;
    @BindView(R.id.set_inviteTv)
    TextView setInviteTv;
    @BindView(R.id.set_versionTv)
    TextView setVersionTv;
    @BindView(R.id.set_cacheTv)
    TextView setCacheTv;
    @BindView(R.id.set_login)
    Button setLogin;

    @Override
    public int getLayoutId() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        regBroadcastRecv(Actions.ACT_REGISTER_SUCCESS, Actions.ACT_LOGIN_SUCCESS, Actions.BIND_FRIEND_SUCCESS);
        mToolbar.title.setText(getString(R.string.set));
    }

    @Override
    public void initData() {
        setVersionTv.setText("v" + DeviceInfoUtils.getVersionName(this));
        if (Login.get().isLogin()) {
            setLoginRl.setVisibility(View.VISIBLE);
            setLogin.setText("退出登录");
            inviteCode();
            if (Login.get().getData() != null && !TextUtils.isEmpty(Login.get().getData().userPasswordFlag)) {
                if ("0".equals(Login.get().getData().userPasswordFlag)) {
                    setPwdTv.setText("修改登录密码");
                } else {
                    setPwdTv.setText("设置登录密码");
                }
            }
        } else {
            setLoginRl.setVisibility(View.GONE);
            setLogin.setText("登录");
        }
    }

    @OnClick({R.id.set_updatePwd_rl, R.id.set_paySet_rl, R.id.set_deviceManager_rl, R.id.set_rule_rl, R.id.set_xiyi_rl, R.id.set_checkVersion_rl, R.id.set_clearCache_rl, R.id.set_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_updatePwd_rl:
                ActivityHelper.toSetLoginPwd(this);
                break;
            case R.id.set_paySet_rl:
                String userpaypassword = Login.get().getData().userPayStats;
                if (userpaypassword.equals("1")) {
                    ActivityHelper.toPaySet(this);
                } else {
                    ActivityHelper.toSetPayPwdActivity(this, "", KConstant.BackPage.normal);
                }
                break;
            case R.id.set_deviceManager_rl:
                ActivityHelper.toDeviceManager(this);
                break;
            case R.id.set_rule_rl:
                WebJump.toRule(this);
                break;
            case R.id.set_xiyi_rl:
                WebJump.toRegisterWeb(this);
                break;
            case R.id.set_checkVersion_rl:
                presenter.checkAppVersion(DefLoad.use(this), this);
                break;
            case R.id.set_clearCache_rl:
                break;
            case R.id.set_login:
                if (Login.get().isLogin()) {
                    //退出登录
                    NativeDlg.create(this)
                            .canceledOnTouchOutside(false)
                            .msg("确定退出登录?")
                            .msgTextSize(19)
                            .cancelClickListener("取消", (dialog, v) -> dialog.dismiss())
                            .okClickListener("确定", ContextCompat.getColor(mContext, R.color.colorPrimary), (dialog, v) -> {
                                presenter.exitApp();
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    //登录
                    ActivityHelper.toLogin(this, KConstant.BackPage.normal);
                }
                break;
        }
    }

    @Override
    public void exitSuccess() {
        UserSp.get().clear();
        Login.get().setLogin(false).setData(null);
        ActivityHelper.toLogin(this, KConstant.BackPage.normal);
        finish();
    }

    @Override
    public void onSafeReceive(Intent intent, String action) {
        super.onSafeReceive(intent, action);
        if (action.equals(Actions.ACT_LOGIN_SUCCESS) || action.equals(Actions.ACT_REGISTER_SUCCESS)) {
            initData();
        } else if (action.equals(Actions.BIND_FRIEND_SUCCESS)) {
            Logger.e("绑定成功");
            inviteCode();
        }
    }

    private void inviteCode() {
        String parentCode = UserSp.get().getInviteCode();
        if (!TextUtils.isEmpty(parentCode)) {
            Span.impl().append(Span.builder(parentCode).underLine()
                    .textColor(ContextCompat.getColor(BaseApp.getInstance(), R.color.black33))
                    .click((text, widget) -> {
                        Utils.copyString(UserSp.get().getInviteCode(), BaseApp.getInstance());
                        Tools.showToast("复制成功");
                    })).into(setInviteTv);
        } else {
            Span.impl().append(Span.builder("绑定上级邀请码").underLine()
                    .textColor(ContextCompat.getColor(BaseApp.getInstance(), R.color.colorAccent))
                    .click((text, widget) -> UrlJumpHelper.jump(SettingActivity.this, KConstant.UrlContants.bindParent))).into(setInviteTv);
        }
    }

    @Override
    public void onVersion(AppVersionBean data) {
        if (!isFinishing() && data != null) {
            // 0没有更新1弱更2强更新
            if ("0".equals(data.stratery)) {
                Tools.showToast(getString(R.string.no_update));
            } else if ("1".equals(data.stratery)) {
                update(data);
            } else if ("2".equals(data.stratery)) {
                update(data);
            }
        }
    }

    private void update(AppVersionBean data) {
        new AppUpdateDialog(this, data, (dialog, current) -> {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
        }).show();
    }

    @Override
    protected SettingContract.SetPresenter getPresenter() {
        return new SettingPresenterImpl();
    }
}
