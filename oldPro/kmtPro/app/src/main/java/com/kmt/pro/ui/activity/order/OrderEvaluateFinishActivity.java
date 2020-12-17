package com.kmt.pro.ui.activity.order;

import android.view.View;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseToolBarActivity;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.KConstant;
import com.kmt.pro.helper.UrlJumpHelper;
import com.kmt.pro.sp.DetailSp;
import com.kmtlibs.app.ActivityManager;

import butterknife.OnClick;

/**
 * Create by JFZ
 * date: 2020-08-05 14:07
 **/
public class OrderEvaluateFinishActivity extends BaseToolBarActivity {
    private String mBookId, investorsCode, houseName;

    @Override
    public int getLayoutId() {
        mBookId = getIntent().getStringExtra(KConstant.Key.order_id);
        investorsCode = getIntent().getStringExtra(KConstant.Key.investorsCode);
        houseName = getIntent().getStringExtra(KConstant.Key.houseName);
        return R.layout.activity_order_evaluate_finish;
    }

    @Override
    public void initView() {
        mToolbar.title.setText("评价完成");
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.evaluate_look, R.id.back_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.evaluate_look:
                DetailSp.get().setCode(investorsCode).setName(houseName);
                ActivityHelper.toDetailsActivity(this);
                ActivityManager.getAppInstance().finishActivity(OrderEvaluateActivity.class);
                ActivityManager.getAppInstance().finishActivity(ExchangeOrderActivity.class);
                ActivityManager.getAppInstance().finishActivity(OrderDetailActivity.class);
                UrlJumpHelper.jump(this, KConstant.UrlContants.bottomSwitchNoFinish + 0);
                finish();
                break;
            case R.id.back_main:
                UrlJumpHelper.jump(this, KConstant.UrlContants.bottomSwitch + 0);
                break;
        }
    }
}
