package com.kmt.pro.ui.activity;

import android.app.Activity;
import android.text.TextUtils;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.bean.OneKeyBuyBean;
import com.kmt.pro.helper.Actions;
import com.kmt.pro.mvp.contract.BindFriendContract;
import com.kmt.pro.mvp.presenter.BindFriendPresenterImpl;
import com.kmt.pro.sp.UserSp;
import com.kmt.pro.ui.dialog.PrivilegSuccessDialog;
import com.kmt.pro.utils.Tools;
import com.kmtlibs.app.receive.SendRecvHelper;
import com.kmtlibs.immersionbar.ImmersionBar;

import androidx.appcompat.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.OnClick;

public class BindFriendActivity extends BaseToolBarActivity<BindFriendContract.BindFriendPresenter> implements BindFriendContract.BindFriendView {

    @BindView(R.id.bind_friend_et)
    AppCompatEditText bindFriendEt;
    PrivilegSuccessDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bindfriend;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("绑定上级");
        bindFriendEt.setCursorVisible(false);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onBindSuccess(OneKeyBuyBean data, String code) {
        if (data.getStatus().equals("0")) {
            UserSp.get().setInviteCode(code);
            SendRecvHelper.send(BaseApp.getInstance(), Actions.BIND_FRIEND_SUCCESS);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new PrivilegSuccessDialog(BindFriendActivity.this, R.mipmap.v2_invite_buy_success, data.getMsg());
            dialog.show();
            mHandler.postDelayed(() -> {
                if (!isFinishing()) finish();
            }, 2000);
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new PrivilegSuccessDialog(BindFriendActivity.this, R.mipmap.v2_invite_buy_err, data.getMsg());
            dialog.show();
        }
    }

    @Override
    public void styleBar(Activity activity) {
        super.styleBar(activity);
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true).keyboardEnable(true).setOnKeyboardListener((isPopup, keyboardHeight) -> {
            if (bindFriendEt != null) {
                if (isPopup) {
                    bindFriendEt.setCursorVisible(isPopup);
                    bindFriendEt.setHint("");
                } else {
                    if (!TextUtils.isEmpty(bindFriendEt.getText().toString().trim())) {
                        bindFriendEt.setCursorVisible(true);
                    } else {
                        bindFriendEt.setCursorVisible(false);
                        bindFriendEt.setHint("请输入邀请码");
                    }
                }
            }
        }).init();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
        super.onDestroy();

    }

    @OnClick(R.id.bind_friend_submit)
    public void onViewClicked() {
        String inviteCode = bindFriendEt.getText().toString().trim();
        if (TextUtils.isEmpty(inviteCode)) {
            Tools.showToast("请输入邀请码");
            return;
        }
        presenter.bindFriend(inviteCode);
    }

    @Override
    protected BindFriendContract.BindFriendPresenter getPresenter() {
        return new BindFriendPresenterImpl();
    }
}
