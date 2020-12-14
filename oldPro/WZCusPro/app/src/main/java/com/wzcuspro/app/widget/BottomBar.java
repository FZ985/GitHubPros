package com.wzcuspro.app.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wzcuspro.R;
import com.wzcuspro.app.callback.BottomBarCallback;
import com.wzcuspro.app.utils.Logger;
import com.wzcuspro.app.utils.Tools;


public class BottomBar extends FrameLayout implements View.OnClickListener {
    private BottomBarCallback callback;
    private int totalIndex = 4;
    private int touchIndex = 0;
    private boolean refreshOK;
    private int oldIndex;
    //根导航
    private RelativeLayout[] indexRoots = new RelativeLayout[totalIndex];
    private int[] indexRootIds = {R.id.bottom_index_0, R.id.bottom_index_1, R.id.bottom_index_2, R.id.bottom_index_3};
    //图片
    private ImageView[] icons = new ImageView[totalIndex];
    private int[] iconIds = {R.id.bottom_img0, R.id.bottom_img1, R.id.bottom_img2, R.id.bottom_img3};
    //文字
    private TextView[] texts = new TextView[totalIndex];
    private int[] textIds = {R.id.bottom_text0, R.id.bottom_text1, R.id.bottom_text2, R.id.bottom_text3};
    private TextView[] msgs = new TextView[totalIndex];
    private int[] msgIds = {R.id.bottom_circle0, R.id.bottom_circle1, R.id.bottom_circle2, R.id.bottom_circle3};
    //中心
    private View center;

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.base_layout_bottombar, this);
        center = view.findViewById(R.id.bottom_index_center);
        for (int i = 0; i < totalIndex; i++) {
            indexRoots[i] = view.findViewById(indexRootIds[i]);
            indexRoots[i].setOnClickListener(this);
            icons[i] = view.findViewById(iconIds[i]);
            texts[i] = view.findViewById(textIds[i]);
            msgs[i] = view.findViewById(msgIds[i]);
        }
        touchIndex = oldIndex = 0;
        selectIndex(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_index_0:
            case R.id.bottom_index_1:
            case R.id.bottom_index_2:
            case R.id.bottom_index_3:
                if (v.getTag() != null) {
                    String tag = (String) v.getTag();
                    int index = Integer.parseInt(tag);
                    switchIndex(index);
                }
                break;
        }
    }

    public int getIndex() {
        return touchIndex;
    }

    //显示中心占位
    public void showCenter() {
        if (center != null) center.setVisibility(VISIBLE);
    }

    //隐藏中心占位
    public void hideCenter() {
        if (center != null) center.setVisibility(GONE);
    }

    //导航切换
    public void switchIndex(int index) {
        if (index >= totalIndex) return;
        if (callback == null) return;
        if (touchIndex == index) {
            callback.bottomReleaseClick(index, oldIndex);
        } else {
            oldIndex = touchIndex;
            selectIndex(index);
            callback.bottomClick(index, oldIndex);
        }
        touchIndex = index;
    }

    private void selectIndex(int index) {
        for (int i = 0; i < totalIndex; i++) {
            if (index == i) {
                icons[i].setEnabled(true);
                texts[i].setEnabled(true);
                startShowAnim(icons[i]);
            } else {
                texts[i].setEnabled(false);
                icons[i].setEnabled(false);
            }
        }
    }

    private void startShowAnim(View view) {
        AnimatorSet animSet = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0.80f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0.80f, 1f);
        animSet.play(animatorX).with(animatorY);
        animSet.setDuration(150);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.start();
    }

    public void showMsg(int index, String msg) {
        if (index >= totalIndex || TextUtils.isEmpty(msg)) {
            return;
        }

        if (msgs[index].getVisibility() == View.VISIBLE) {
            return;
        }
        TextView tv = msgs[index];
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        if (params == null) {
            Logger.e("LayoutParams == null");
            return;
        }
        int len = msg.length();
        if (len == 1) {
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(-Tools.dip2px(getContext(), 7), Tools.dip2px(getContext(), 5), 0, 0);
            tv.setBackgroundResource(R.drawable.bottom_radius);
            tv.setPadding(Tools.dip2px(getContext(), 4), 0, Tools.dip2px(getContext(), 4), 0);
        } else if (len == 2) {
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(-Tools.dip2px(getContext(), 10), Tools.dip2px(getContext(), 5), 0, 0);
            tv.setBackgroundResource(R.drawable.bottom_radius);
            tv.setPadding(Tools.dip2px(getContext(), 4), 0, Tools.dip2px(getContext(), 4), 0);
        } else if (len >= 3) {
            params.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(-Tools.dip2px(getContext(), 12), Tools.dip2px(getContext(), 5), 0, 0);
            tv.setBackgroundResource(R.drawable.bottom_radius);
            tv.setPadding(Tools.dip2px(getContext(), 4), 0, Tools.dip2px(getContext(), 4), 0);
            msg = "99+";
        }
        tv.setText(msg);
        tv.setLayoutParams(params);
        tv.setVisibility(View.VISIBLE);
        circleAnim(tv, true);
    }

    public void showPoint(int index) {
        if (index >= totalIndex) return;
        if (msgs[index].getVisibility() == View.VISIBLE) {
            return;
        }
        TextView tv = msgs[index];
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        params.width = Tools.dip2px(getContext(), 8);
        params.height = Tools.dip2px(getContext(), 8);
        params.setMargins(-Tools.dip2px(getContext(), 3), Tools.dip2px(getContext(), 5), 0, 0);
        tv.setBackgroundResource(R.drawable.bottom_circle);
        tv.setText("");
        tv.setPadding(0, 0, 0, 0);
        tv.setLayoutParams(params);
        tv.setVisibility(View.VISIBLE);
        circleAnim(tv, true);
    }

    public void hideMsg(int index) {
        if (index >= totalIndex) return;
        msgs[index].setVisibility(View.GONE);
        circleAnim(msgs[index], false);
    }

    private void circleAnim(final TextView view, final boolean isShow) {
        ObjectAnimator animatorX = null;
        ObjectAnimator animatorY = null;
        AnimatorSet animSet = new AnimatorSet();
        if (isShow) {
            animatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
            animatorY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        } else {
            animatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
            animatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        }
        animSet.play(animatorX).with(animatorY);
        animSet.setDuration(200);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.start();
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void setCallback(BottomBarCallback callback) {
        this.callback = callback;
    }
}
