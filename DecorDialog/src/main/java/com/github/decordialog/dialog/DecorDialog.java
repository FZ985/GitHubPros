package com.github.decordialog.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

/**
 * Create by JFZ
 * date: 2020-08-19 11:04
 **/
public abstract class DecorDialog {
    private Activity activity;
    private FrameLayout systomDecorView;
    private Decorlayout decorLayout;
    private View contentView;
    private boolean isShowing = false;
    private boolean isCanceled = true;
    private boolean isTouchOutSide = true;
    private int[] anims;
    private OnDismissListener dismissListener;
    private boolean isRootTouchEvent = true;//是否给根布局添加点击事件

    public interface OnDismissListener {
        void onDismiss(DecorDialog dialog);
    }

    public DecorDialog(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("the activity is null");
        }
        this.activity = new WeakReference<>(activity).get();
        init();
    }

    private void init() {
        System.out.println("DecorDialog...init");
        if (activity != null) {
            systomDecorView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
            decorLayout = new Decorlayout(activity);
            decorLayout.setBackgroundDrawable(new ColorDrawable(ColorUtils.setAlphaComponent(Color.BLACK, 255 / 3)));
            decorLayout.setLayoutParams(new FrameLayout.LayoutParams(systomDecorView.getWidth(), systomDecorView.getHeight()));
        }
    }

    public void setContentView(@LayoutRes int layoutRes) {
        setContentView(LayoutInflater.from(activity).inflate(layoutRes, null));
    }

    public void setContentView(View view) {
        if (systomDecorView != null && decorLayout != null) {
            System.out.println("DecorDialog...setContentView");
            if (decorLayout != null && contentView != null) {
                decorLayout.removeAllViews();
                systomDecorView.removeView(decorLayout);
            }
            this.contentView = view;
            assert decorLayout != null;
            decorLayout.addView(contentView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            decorLayout.setVisibility(View.GONE);
            systomDecorView.addView(decorLayout);
            isShowing = false;
            initView();
        }
    }

    protected abstract void initView();

    public void show() {
        if (isShowing) return;
        if (activity == null) {
            throw new IllegalArgumentException("the activity is detach");
        }
        if (systomDecorView != null && decorLayout != null && contentView != null) {
            decorLayout.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.VISIBLE);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            if (isRootTouchEvent) {
                decorLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isTouchOutSide) {
                            dismiss();
                        }
                    }
                });
            }
            isShowing = true;
            setGravity(Gravity.CENTER);
            decorLayout.setFocusable(true);
            decorLayout.setFocusableInTouchMode(true);
            decorLayout.requestFocus();
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(decorLayout, "alpha", 0f, 1f);
            alphaAnim.setDuration(200);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.start();
            if (anims != null && anims.length > 0) {
                contentView.startAnimation(AnimationUtils.loadAnimation(activity, anims[0]));
            }
        }
    }

    public void dismiss() {
        dismiss(false);
    }

    //关闭弹出框,是否需要将activity置空
    protected void dismiss(boolean needDetach) {
        if (systomDecorView != null && decorLayout != null && contentView != null) {
            decorLayout.setFocusable(false);
            decorLayout.setFocusableInTouchMode(false);
            decorLayout.clearFocus();
            isShowing = false;
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(decorLayout, "alpha", 1f, 0f);
            alphaAnim.setDuration(200);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (decorLayout != null) {
                        decorLayout.setVisibility(View.GONE);
                    }
                }
            });
            alphaAnim.start();
            if (anims != null && anims.length > 1) {
                Animation animation = AnimationUtils.loadAnimation(activity, anims[1]);
                if (contentView.getVisibility() == View.VISIBLE) {
                    contentView.startAnimation(animation);
                    isShowing = false;
                    onWindowDetach(needDetach);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (contentView != null) contentView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                } else {
                    contentView.setVisibility(View.GONE);
                    isShowing = false;
                    onWindowDetach(needDetach);
                }
            } else {
                contentView.setVisibility(View.GONE);
                isShowing = false;
                onWindowDetach(needDetach);
            }
        }
    }

    private void onWindowDetach(boolean needDetach) {
        if (activity != null) {
            if (dismissListener != null) {
                dismissListener.onDismiss(this);
            }
            if (needDetach) {
                if (systomDecorView != null && decorLayout != null) {
                    systomDecorView.removeView(decorLayout);
                }
                contentView = null;
                decorLayout = null;
                activity = null;
            } else {
                if (activity.isFinishing()) {
                    contentView = null;
                    decorLayout = null;
                    activity = null;
                }
            }
        }
    }

    public void setGravity(final int gravity) {
        if (contentView != null) {
            RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            if (contentParams == null) return;
            switch (gravity) {
                case Gravity.TOP:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case Gravity.CENTER:
                    contentParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    break;
                case Gravity.CENTER_HORIZONTAL:
                    contentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    break;
                case Gravity.CENTER_VERTICAL:
                    contentParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    break;
                case Gravity.BOTTOM:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case Gravity.TOP | Gravity.CENTER_HORIZONTAL:
                    contentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case Gravity.TOP | Gravity.RIGHT:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case Gravity.TOP | Gravity.LEFT:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    break;
                case Gravity.BOTTOM | Gravity.RIGHT:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case Gravity.BOTTOM | Gravity.LEFT:
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL:
                    contentParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    break;
                case Gravity.CENTER_VERTICAL | Gravity.LEFT:
                    contentParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    break;
                case Gravity.CENTER_VERTICAL | Gravity.RIGHT:
                    contentParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    contentParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    break;
            }
            contentView.setLayoutParams(contentParams);
        }
    }

    public void setRootTouchEvent(boolean rootTouchEvent) {
        this.isRootTouchEvent = rootTouchEvent;
    }

    public void setCanceledOnTouchOutside(boolean isTouchOutSide) {
        this.isTouchOutSide = isTouchOutSide;
    }

    public void setCancelable(boolean cancelable) {
        this.isCanceled = cancelable;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.dismissListener = listener;
    }

    public void dimAmount(float dimAmount) {
        if (dimAmount > 1f) dimAmount = 1f;
        if (dimAmount < 0f) dimAmount = 0f;
        if (decorLayout != null) {
            Drawable background = decorLayout.getBackground();
            if (background != null && background instanceof ColorDrawable) {
                ColorDrawable colordDrawable = (ColorDrawable) background;
                int color = colordDrawable.getColor();
                decorLayout.setBackgroundColor(ColorUtils.setAlphaComponent(color, (int) (255 * dimAmount)));
            }
        }
    }

    public void setWindowColor(int color) {
        if (decorLayout != null) {
            decorLayout.setBackground(new ColorDrawable(color));
        }
    }

    public void setAnimsStyle(@AnimRes int enter, @AnimRes int exit) {
        anims = new int[2];
        anims[0] = enter;
        anims[1] = exit;
    }

    public Context getContext() {
        if (activity != null) {
            return activity;
        }
        if (contentView != null) {
            return contentView.getContext();
        }
        if (decorLayout != null) {
            return decorLayout.getContext();
        }
        return systomDecorView.getContext();
    }

    public void setWidth(int width) {
        if (contentView != null && contentView.getLayoutParams() != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            params.width = width;
            contentView.setLayoutParams(params);
        }
    }

    public void setHeight(int height) {
        if (contentView != null && contentView.getLayoutParams() != null) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            params.height = height;
            contentView.setLayoutParams(params);
        }
    }

    public <T extends View> T findViewById(@IdRes int id) {
        SparseArray<View> hashMap = (SparseArray<View>) contentView.getTag();
        if (hashMap == null) {
            hashMap = new SparseArray<View>();
            contentView.setTag(hashMap);
        }
        View childView = hashMap.get(id);
        if (childView == null) {
            childView = contentView.findViewById(id);
            hashMap.put(id, childView);
        }
        return (T) childView;
    }

    private class Decorlayout extends RelativeLayout {
        public Decorlayout(@NonNull Context context) {
            super(context);
        }

        public Decorlayout(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public Decorlayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if (getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }

                if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null) {
                        state.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == KeyEvent.ACTION_UP) {
                    final KeyEvent.DispatcherState state = getKeyDispatcherState();
                    if (state != null && state.isTracking(event) && !event.isCanceled()) {
                        if (isCanceled) {
                            dismiss();
                        }
                        return true;
                    }
                }
                return super.dispatchKeyEvent(event);
            } else {
                return super.dispatchKeyEvent(event);
            }
        }
    }
}
