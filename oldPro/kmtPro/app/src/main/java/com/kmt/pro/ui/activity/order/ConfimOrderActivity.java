package com.kmt.pro.ui.activity.order;

import com.kmt.pro.R;
import com.kmt.pro.base.BaseActivity;
import com.kmt.pro.helper.KConstant;

/**
 * Create by JFZ
 * date: 2020-08-04 10:55
 **/
public class ConfimOrderActivity extends BaseActivity {
    private String orderId;//生成预约订单的id
    private int pay_from_which; // 1:是从详情页面进入 2:是从订单列表中进入的 3:是从入住页面进入的

    @Override
    public int getLayoutId() {
        orderId = getIntent().getStringExtra(KConstant.Key.order_id);
        pay_from_which = getIntent().getIntExtra(KConstant.Key.order_from_switch, 0);
        return R.layout.activity_confim_order;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
