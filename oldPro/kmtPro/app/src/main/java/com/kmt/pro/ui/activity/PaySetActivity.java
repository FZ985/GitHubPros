package com.kmt.pro.ui.activity;

import android.view.View;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.ActivityHelper;

import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-07-16 15:46
 **/
public class PaySetActivity extends BaseToolBarActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_payset;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("支付设置");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.pwd_one, R.id.pwd_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pwd_one:
                ActivityHelper.toUpdatePayPwd(this);
                break;
            case R.id.pwd_two:
                ActivityHelper.toPayPwdForget(this);
                break;
        }
    }
}
