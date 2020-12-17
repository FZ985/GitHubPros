package com.kmt.pro.helper;

import com.kmt.pro.R;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.ui.fragment.ContentFragment;
import com.kmt.pro.ui.fragment.WebFragment;
import com.kmt.pro.ui.fragment.main.MineFragment;
import com.kmt.pro.ui.fragment.main.PriceFragment;
import com.kmt.pro.ui.fragment.main.TradeFragment;
import com.kmtlibs.immersionbar.ImmersionBar;
import com.kmtlibs.okhttp.base.OkHttpConfig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Create by JFZ
 * date: 2020-06-30 18:28
 * 主页内容页面
 **/
public class MainContentHelper {
    private ContentFragment mPriceFrag, mTradeFrag, mShopFrag, mInviteFrag, mCenterFrag;

    private MainContentHelper() {
    }

    public static MainContentHelper get() {
        return MainContentHelperHolder.INSTANCE;
    }

    private static class MainContentHelperHolder {
        private static volatile MainContentHelper INSTANCE = new MainContentHelper();
    }

    public void switchTab(int index, FragmentTransaction transaction, MainFragmentPageCall call) {
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (mPriceFrag == null) {
                    mPriceFrag = PriceFragment.instance(call);
                    transaction.add(R.id.main_content, mPriceFrag);
                } else {
                    mPriceFrag.refresh();
                }
                transaction.show(mPriceFrag);
                break;
            case 1:
                if (mTradeFrag == null) {
                    mTradeFrag = TradeFragment.instance(call);
                    transaction.add(R.id.main_content, mTradeFrag);
                } else {
                    mTradeFrag.refresh();
                }
                transaction.show(mTradeFrag);
                break;
            case 2:
                if (mShopFrag == null) {
                    mShopFrag = WebFragment.instance("https://www.baidu.com", call);
                    transaction.add(R.id.main_content, mShopFrag);
                } else {
                    mShopFrag.refresh();
                }
                transaction.show(mShopFrag);
                break;
            case 3:
                if (mInviteFrag == null) {
                    mInviteFrag = WebFragment.instance("https://www.baidu.com", call);
                    transaction.add(R.id.main_content, mInviteFrag);
                } else {
                    mInviteFrag.refresh();
                }
                transaction.show(mInviteFrag);
                break;
            case 4:
                if (mCenterFrag == null) {
                    mCenterFrag = MineFragment.instance(call);
                    transaction.add(R.id.main_content, mCenterFrag);
                } else {
                    mCenterFrag.refresh();
                }
                transaction.show(mCenterFrag);
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    public boolean checkFragmentIsInstance(int index) {
        switch (index) {
            case 0:
                return (mPriceFrag == null);
            case 1:
                return (mTradeFrag == null);
            case 2:
                return (mShopFrag == null);
            case 3:
                return (mInviteFrag == null);
            case 4:
                return (mCenterFrag == null);
        }
        return false;
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mPriceFrag != null) {
            transaction.hide(mPriceFrag);
        }
        if (mTradeFrag != null) {
            transaction.hide(mTradeFrag);
        }
        if (mShopFrag != null) {
            transaction.hide(mShopFrag);
        }
        if (mInviteFrag != null) {
            transaction.hide(mInviteFrag);
        }
        if (mCenterFrag != null) {
            transaction.hide(mCenterFrag);
        }
    }

    public void onFragmentPage(AppCompatActivity activity, int page, int type) {
        OkHttpConfig.getInstance().obtainHandler().post(() -> {
            ImmersionBar immersionBar = ImmersionBar.with(activity);
            switch (page) {
                case 0:
                    if (type == 1) {
                        immersionBar.statusBarDarkFont(false);
                    } else if (type == 2) {
                        immersionBar.statusBarDarkFont(true);
                    }
                    break;
                case 1:
                    immersionBar.statusBarDarkFont(false);
                    break;
                case 4:
                    immersionBar.statusBarDarkFont(true);
                    break;
            }
            immersionBar.init();
        });
    }

    public void destry(FragmentTransaction transaction) {
        if (transaction != null) {
            hideFragment(transaction);
            transaction.commitAllowingStateLoss();
        }
        mPriceFrag = null;
        mTradeFrag = null;
        mShopFrag = null;
        mInviteFrag = null;
        mCenterFrag = null;
    }

}
