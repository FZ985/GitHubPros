package com.kmt.pro.ui.fragment.trade;

import com.kmt.pro.R;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.event.DepositoryEvent;
import com.kmt.pro.ui.fragment.ContentFragment;
import com.kmtlibs.app.utils.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Create by JFZ
 * date: 2020-07-28 14:41
 **/
public class DepositoryFragment extends ContentFragment {
    @Override
    public int getLayoutId() {
        return R.layout.fragment_depository;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDepositoryEvtnt(DepositoryEvent event) {
        if (event != null) {
            Logger.e("===Depository===event:" + event.code);
            if (call != null) {
                call.onFragmentPage(3, 0);
            }
        }
    }

    @Override
    public void refresh() {

    }

    public static DepositoryFragment instance(MainFragmentPageCall call) {
        DepositoryFragment fragment = new DepositoryFragment();
        fragment.call = call;
        return fragment;
    }
}
