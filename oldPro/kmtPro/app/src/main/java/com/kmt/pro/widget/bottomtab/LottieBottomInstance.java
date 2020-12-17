package com.kmt.pro.widget.bottomtab;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.kmt.pro.R;
import com.kmt.pro.base.BaseApp;
import com.kmt.pro.helper.ActivityHelper;
import com.kmt.pro.helper.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-05-27 15:37
 **/
public class LottieBottomInstance implements View.OnClickListener {

    public interface LottieBottomCall {
        void onTabClick(int index, int oldIndex);

        void onTabReleaseClick(int index);
    }

    private static class LottieBottomInstanceHolder {
        private static volatile LottieBottomInstance HOLDER = new LottieBottomInstance();
    }

    private LottieAnimationView lottie1, lottie2, lottie3, lottie4, lottie5;
    private TextView tv1, tv2, tv3, tv4, tv5;
    private List<LottieBottomTab> tabs = new ArrayList<>();
    private List<LottieAnimationView> lotties = new ArrayList<>();
    private List<TextView> tvs = new ArrayList<>();
    private LottieBottomCall call;
    private int oldIndex = 0;

    public static LottieBottomInstance getInstance() {
        return LottieBottomInstanceHolder.HOLDER;
    }

    public LottieBottomInstance setCall(LottieBottomCall call) {
        this.call = call;
        return this;
    }

    private LottieBottomInstance() {
        tabs = new ArrayList<>();
        tabs.add(new LottieBottomTab("navi_hangqing/"));
        tabs.add(new LottieBottomTab("navi_maimai/"));
        tabs.add(new LottieBottomTab("navi_shop/"));
        tabs.add(new LottieBottomTab("navi_invite/"));
        tabs.add(new LottieBottomTab("navi_mine/"));
    }

    public LottieBottomInstance init(View view) {
        lottie1 = view.findViewById(R.id.lottie1);
        lottie2 = view.findViewById(R.id.lottie2);
        lottie3 = view.findViewById(R.id.lottie3);
        lottie4 = view.findViewById(R.id.lottie4);
        lottie5 = view.findViewById(R.id.lottie5);
        tv1 = view.findViewById(R.id.tab_hangqing_tv);
        tv2 = view.findViewById(R.id.tab_maimai_tv);
        tv3 = view.findViewById(R.id.tab_shop_tv);
        tv4 = view.findViewById(R.id.tab_invite_tv);
        tv5 = view.findViewById(R.id.tab_mine_tv);
        lottie1.setOnClickListener(this);
        lottie2.setOnClickListener(this);
        lottie3.setOnClickListener(this);
        lottie4.setOnClickListener(this);
        lottie5.setOnClickListener(this);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        tv4.setOnClickListener(this);
        tv5.setOnClickListener(this);
        if (lotties == null) {
            lotties = new ArrayList<>();
        }
        lotties.clear();
        if (tvs == null) {
            tvs = new ArrayList<>();
        }
        tvs.clear();
        lotties.add(lottie1);
        lotties.add(lottie2);
        lotties.add(lottie3);
        lotties.add(lottie4);
        lotties.add(lottie5);
        tvs.add(tv1);
        tvs.add(tv2);
        tvs.add(tv3);
        tvs.add(tv4);
        tvs.add(tv5);
        showDefault();
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_hangqing_tv:
            case R.id.lottie1:
                switchIndex(0);
                break;
            case R.id.tab_maimai_tv:
            case R.id.lottie2:
                switchIndex(1);
                break;
            case R.id.tab_shop_tv:
            case R.id.lottie3:
                if (Login.get().isLogin()) {
                    switchIndex(2);
                } else {
                    ActivityHelper.toLogin(BaseApp.getInstance());
                }
                break;
            case R.id.tab_invite_tv:
            case R.id.lottie4:
                if (Login.get().isLogin()) {
                    switchIndex(3);
                } else {
                    ActivityHelper.toLogin(BaseApp.getInstance());
                }
                break;
            case R.id.tab_mine_tv:
            case R.id.lottie5:
                switchIndex(4);
                break;
        }
    }

    public void switchIndex(int index) {
        if (call != null) {
            if (tabs.get(index).isSelect()) {
                call.onTabReleaseClick(index);
            } else {
                startChange(index);
            }
        } else {
            startChange(index);
        }
    }

    private void startChange(int index) {
        for (int i = 0; i < tabs.size(); i++) {
            if (i == index) {
                tabs.get(i).setSelect(true);
                LottieResult<LottieComposition> result = LottieCompositionFactory.fromAssetSync(lotties.get(i).getContext(), tabs.get(i).getEnter());
                LottieComposition value = result.getValue();
                lotties.get(i).setImageAssetsFolder(tabs.get(i).getFolder());
                lotties.get(i).setComposition(value);
                lotties.get(i).playAnimation();
                if (call != null) {
                    call.onTabClick(i, oldIndex);
                }
                tvs.get(i).setTextColor(Color.parseColor("#6204f5"));
            } else {
                if (tabs.get(i).isSelect()) {
                    oldIndex = i;
                    tabs.get(i).setSelect(false);
                    LottieResult<LottieComposition> result = LottieCompositionFactory.fromAssetSync(lotties.get(i).getContext(), tabs.get(i).getExit());
                    LottieComposition value = result.getValue();
                    lotties.get(i).setImageAssetsFolder(tabs.get(i).getFolder());
                    lotties.get(i).setComposition(value);
                    lotties.get(i).playAnimation();
                }
                tvs.get(i).setTextColor(Color.parseColor("#8E8EA3"));
            }
        }
    }

    public void showDefault() {
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).setSelect(false);
            LottieResult<LottieComposition> result = LottieCompositionFactory.fromAssetSync(lotties.get(i).getContext(), tabs.get(i).getExit());
            LottieComposition value = result.getValue();
            lotties.get(i).setProgress(1f);
            lotties.get(i).setImageAssetsFolder(tabs.get(i).getFolder());
            lotties.get(i).setComposition(value);
//            lotties.get(i).playAnimation();
        }
    }

    public void hideIndex(int index) {
        if (index < lotties.size()) {
            lotties.get(index).setVisibility(View.GONE);
            tvs.get(index).setVisibility(View.GONE);
        }
    }

    public void showIndex(int index) {
        if (index < lotties.size()) {
            lotties.get(index).setVisibility(View.VISIBLE);
            tvs.get(index).setVisibility(View.VISIBLE);
        }
    }

    public int getIndex() {
        for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).isSelect()) return i;
        }
        return 0;
    }

    public int getOldIndex() {
        return oldIndex;
    }
}
