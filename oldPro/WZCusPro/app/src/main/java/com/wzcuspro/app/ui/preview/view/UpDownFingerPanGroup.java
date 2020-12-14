package com.wzcuspro.app.ui.preview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.nineoldandroids.view.ViewHelper;
import com.wzcuspro.R;
import com.wzcuspro.app.ui.preview.PreviewImageView;
import com.wzcuspro.app.utils.Logger;


/**
 * Created by sdj on 2018/1/18.
 */

public class UpDownFingerPanGroup extends FrameLayout {
    private PreviewImageView view3;
    private View otherView;
    private float mDownY;
    private float mTranslationY;
    private float mLastTranslationY;
    private static int MAX_TRANSLATE_Y = 500;
    private int MAX_EXIT_Y = 400;
    private final static long DURATION = 150;
    private boolean isAnimate = false;
    private int fadeIn = R.anim.wechat_act_enter;
    private int fadeOut = R.anim.wechat_act_exit;
    private int mTouchslop;
    private onAlphaChangedListener mOnAlphaChangedListener;
    private boolean isEnable;

    private enum UpDwonType {ALL, TOP_TO_BOTTOM, BOTTOM_TO_TOP}

    private UpDwonType type = UpDwonType.ALL;

    public UpDownFingerPanGroup(Context context) {
        this(context, null);
    }

    public UpDownFingerPanGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UpDownFingerPanGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        mTouchslop = ViewConfiguration.getTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(0) != null) {
            if (getChildAt(0) instanceof PreviewImageView) {
                view3 = (PreviewImageView) getChildAt(0);
                otherView = null;
            } else {
                otherView = getChildAt(0);
                view3 = null;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int action = ev.getAction() & ev.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (view3 != null) {
                    isIntercept = view3.getCurrentZoom() <= view3.getMinZoom() && (view3.getMaxTouchCount() == 0 || view3.getMaxTouchCount() == 1) && Math.abs(ev.getRawY() - mDownY) > 2 * mTouchslop;
                }
                break;
        }
        return isIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction() & event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getRawY();
            case MotionEvent.ACTION_MOVE:
                if (view3 != null && isEnable) {
                    onOneFingerPanActionMove(event);
                }
                if (otherView != null && isEnable) {
                    onOneFingerPanActionMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
        }
        return true;
    }

    private void onOneFingerPanActionMove(MotionEvent event) {
        float moveY = event.getRawY();
        mTranslationY = moveY - mDownY + mLastTranslationY;
        if (type == UpDwonType.ALL) {
            ViewHelper.setScrollY(this, -(int) mTranslationY);
        } else if (type == UpDwonType.TOP_TO_BOTTOM) {
            if ((-(int) mTranslationY) > 0) {
                ViewHelper.setScrollY(this, -(int) mTranslationY);
            }

        } else if (type == UpDwonType.BOTTOM_TO_TOP) {
            ViewHelper.setScrollY(this, -(int) mTranslationY);
        }
        float percent = Math.abs(mTranslationY / (MAX_TRANSLATE_Y + (view3 == null ? otherView.getHeight() : view3.getHeight())));
        float mAlpha = (1 - percent);
        if (mAlpha > 1) {
            mAlpha = 1;
        } else if (mAlpha < 0) {
            mAlpha = 0;
        }
        ViewGroup linearLayout = (ViewGroup) getParent();
        if (null != linearLayout) {
            linearLayout.getBackground().mutate().setAlpha((int) (mAlpha * 255));
        }
        //触发回调 根据距离处理其他控件的透明度 显示或者隐藏角标，文字信息等
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onTranslationYChanged(mTranslationY);
        }
    }

    private void onActionUp() {
        if (Math.abs(mTranslationY) > MAX_EXIT_Y) {
            exitWithTranslation(mTranslationY);
        } else {
            resetCallBackAnimation();
        }
    }

    public void exitWithTranslation(float currentY) {
        Logger.e("exitWithTranslation_currentY:" + currentY);
        if (currentY > 0) {
            //Top to Bottom
            ValueAnimator animDown = ValueAnimator.ofFloat(mTranslationY, getHeight());
            animDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    ViewHelper.setScrollY(UpDownFingerPanGroup.this, -(int) fraction);
                }
            });
            animDown.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reset();
                    if (mOnAlphaChangedListener != null) {
                        mOnAlphaChangedListener.finish();
                    }
                    Activity activity = ((Activity) getContext());
                    activity.finish();
                    activity.overridePendingTransition(fadeIn, fadeOut);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animDown.setDuration(DURATION);
            animDown.setInterpolator(new LinearInterpolator());
            animDown.start();
        } else if (currentY < 0) {
            //Bottom to Top
            ValueAnimator animUp = ValueAnimator.ofFloat(mTranslationY, -getHeight());
            animUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float fraction = (float) animation.getAnimatedValue();
                    ViewHelper.setScrollY(UpDownFingerPanGroup.this, -(int) fraction);
                }
            });
            animUp.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    reset();
                    if (mOnAlphaChangedListener != null) {
                        mOnAlphaChangedListener.finish();
                    }
                    ((Activity) getContext()).finish();
                    ((Activity) getContext()).overridePendingTransition(fadeIn, fadeOut);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animUp.setDuration(DURATION);
            animUp.setInterpolator(new LinearInterpolator());
            animUp.start();
        }
    }

    private void resetCallBackAnimation() {
        ValueAnimator animatorY = ValueAnimator.ofFloat(mTranslationY, 0);
        animatorY.setDuration(DURATION);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (isAnimate) {
                    mTranslationY = (float) valueAnimator.getAnimatedValue();
                    mLastTranslationY = mTranslationY;
                    ViewHelper.setScrollY(UpDownFingerPanGroup.this, -(int) mTranslationY);
                }
            }
        });
        animatorY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimate = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAnimate) {
                    mTranslationY = 0;
                    ViewGroup linearLayout = (ViewGroup) getParent();
                    if (null != linearLayout) {
                        linearLayout.getBackground().mutate().setAlpha(255);
                    }
                    invalidate();
                    reset();
                }
                isAnimate = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorY.start();
    }

    public void setEnable(boolean enable) {
        this.isEnable = enable;
    }

    public void setExitOffest(int offest) {
        this.MAX_EXIT_Y = offest;
    }

    public interface onAlphaChangedListener {
        void onAlphaChanged(float alpha);

        void onTranslationYChanged(float translationY);

        void finish();
    }

    //暴露的回调方法（可根据位移距离或者alpha来改变主UI控件的透明度等
    public void setOnAlphaChangeListener(onAlphaChangedListener alphaChangeListener) {
        mOnAlphaChangedListener = alphaChangeListener;
    }

    private void reset() {
        if (null != mOnAlphaChangedListener) {
            mOnAlphaChangedListener.onTranslationYChanged(mTranslationY);
            mOnAlphaChangedListener.onAlphaChanged(1);
        }
    }
}
