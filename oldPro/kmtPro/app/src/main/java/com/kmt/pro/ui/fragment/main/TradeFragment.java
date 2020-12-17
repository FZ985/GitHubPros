package com.kmt.pro.ui.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.kmt.pro.R;
import com.kmt.pro.adapter.TabPage2Adapter;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.base.BaseFragment;
import com.kmt.pro.callback.MainFragmentPageCall;
import com.kmt.pro.ui.fragment.ContentFragment;
import com.kmt.pro.ui.fragment.trade.BuyFragment;
import com.kmt.pro.ui.fragment.trade.DelegateFragment;
import com.kmt.pro.ui.fragment.trade.DepositoryFragment;
import com.kmt.pro.ui.fragment.trade.RevokeFragment;
import com.kmt.pro.ui.fragment.trade.SellFragment;
import com.kmt.pro.widget.ScaleTransitionPagerTitleView;
import com.kmtlibs.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.BindView;
import magicindicator.MagicIndicator;
import magicindicator.ViewPagerHelper;
import magicindicator.buildins.commonnavigator.CommonNavigator;
import magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

/**
 * Create by JFZ
 * date: 2020-06-30 18:38
 **/
public class TradeFragment extends ContentFragment implements MainFragmentPageCall {
    @BindView(R.id.trade_indicator)
    MagicIndicator tradeIndicator;
    @BindView(R.id.trade_vp)
    ViewPager2 tradeVp;
    private String[] tabs = {"购买", "转让", "订单", "已购", "明细"};
    private TabPage2Adapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_trade;
    }

    @Override
    public void initView() {
        try {
            View child = tradeVp.getChildAt(0);
            if (child instanceof RecyclerView) {
                child.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void initData() {
        initTab();
        refresh();
    }

    @Override
    public void refresh() {
        if (call != null) {
            call.onFragmentPage(1, 0);
        }
    }

    private void initTab() {
        List<BaseFragment> list = new ArrayList<>();
        list.add(new BuyFragment());//买入
        list.add(new SellFragment());//卖出
        list.add(new RevokeFragment());//撤单
        list.add(DepositoryFragment.instance(this));//持仓
        list.add(new DelegateFragment());//委托
        //实例化适配器
        adapter = new TabPage2Adapter(this, list);
        tradeVp.setAdapter(adapter);
        tradeVp.setOffscreenPageLimit(list.size());
        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setMinScale(0.85f);
                simplePagerTitleView.setText(tabs[index]);
                simplePagerTitleView.setNormalColor(Color.parseColor("#C2BFE1"));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.white));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setOnClickListener(v -> {
                    tradeVp.setCurrentItem(index);
                    tradeIndicator.onPageSelected(index);
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(Utils.dip2px(BaseApp.getInstance(), 20));
                indicator.setLineHeight(Utils.dip2px(BaseApp.getInstance(), 2));
                indicator.setColors(ContextCompat.getColor(context, R.color.white));
                indicator.setRoundRadius(5f);
                return indicator;
            }
        });
        tradeIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind2(tradeIndicator, tradeVp);
    }

    public static TradeFragment instance(MainFragmentPageCall call) {
        TradeFragment fragment = new TradeFragment();
        fragment.call = call;
        return fragment;
    }

    @Override
    public void onFragmentPage(int page, int type) {
        if (tradeVp != null) {
            tradeVp.setCurrentItem(page);
            tradeIndicator.onPageSelected(page);
        }
    }
}
