package api29.libs.app.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.Gravity;
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
import androidx.core.graphics.ColorUtils;
import api29.libs.app.utils.Logger;

public abstract class BaseDecorDialog implements View.OnClickListener {

    private Activity activity;
    private FrameLayout decorView;
    private RelativeLayout rootView;
    private FrameLayout.LayoutParams rootLayoutParams;
    private View contentView;
    private boolean isAddContentView;
    private boolean isShowing;
    private int[] anims;
    private boolean isTouchOutSide = true;
    private int gravity = Gravity.CENTER;
    private OnDecorDismissListener dismissListener;
    private boolean isRootTouchEvent = true;//是否给根布局添加点击事件

    public BaseDecorDialog(Activity activity) {
        this.activity = new WeakReference<Activity>(activity).get();
        configRoot();
    }

    private void configRoot() {
        if (activity != null) {
            decorView = (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
            rootView = new RelativeLayout(activity);
            rootLayoutParams = new FrameLayout.LayoutParams(decorView.getWidth(), decorView.getHeight());
            rootView.setBackgroundDrawable(new ColorDrawable(ColorUtils.setAlphaComponent(Color.BLACK, 255 / 3)));
            rootView.setLayoutParams(rootLayoutParams);
        }
    }

    public void setContentView(View view) {
        if (decorView != null) {
            Logger.e("---baseDecor---setContentView");
            if (rootView != null && contentView != null) {
                rootView.removeAllViews();
                decorView.removeView(rootView);
            }
            isAddContentView = false;
            contentView = view;
            rootView.addView(contentView);
            setGravity(gravity);
            rootView.setVisibility(View.GONE);
            decorView.addView(rootView);
            isAddContentView = true;
            initView();
        }
    }

    public void setContentView(@LayoutRes int layoutId) {
        setContentView(LayoutInflater.from(activity).inflate(layoutId, null));
    }

    public Context getContext() {
        return activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setGravity(int gravity) {
        RelativeLayout.LayoutParams contentParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
        if (contentParams != null) {
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

    public void show() {
        if (decorView != null && rootView != null && contentView != null) {
            rootView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.VISIBLE);
            if (isRootTouchEvent) {
                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isTouchOutSide) {
                            dismiss();
                        }
                    }
                });
            }
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(rootView, "alpha", 0f, 1f);
            alphaAnim.setDuration(200);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.start();
            if (anims != null && anims.length > 0) {
                contentView.startAnimation(AnimationUtils.loadAnimation(activity, anims[0]));
            }
            isShowing = true;
        }
    }

    public void dismiss() {
        if (decorView != null && rootView != null && contentView != null) {
            ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(rootView, "alpha", 1f, 0f);
            alphaAnim.setDuration(300);
            alphaAnim.setInterpolator(new LinearInterpolator());
            alphaAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (rootView != null) {
                        rootView.setVisibility(View.GONE);
                    }
                }
            });
            alphaAnim.start();
            if (anims != null && anims.length > 0) {
                Animation animation = AnimationUtils.loadAnimation(activity, anims[1]);
                if (contentView.getVisibility() == View.VISIBLE) {
                    contentView.startAnimation(animation);
                    isShowing = false;
                    onWindowDetach();
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
                    onWindowDetach();
                }
            } else {
                contentView.setVisibility(View.GONE);
                isShowing = false;
                onWindowDetach();
            }
        }
    }

    public boolean isAddView() {
        return isAddContentView;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public View getContentView() {
        return contentView;
    }

    public void setBackgroundColor(int color) {
        if (rootView != null) {
            rootView.setBackgroundColor(color);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void setCanceledOnTouchOutside(boolean isTouchOutSide) {
        this.isTouchOutSide = isTouchOutSide;
    }

    public void dimAmount(float dimAmount) {
        if (dimAmount > 1f) dimAmount = 1f;
        if (dimAmount < 0f) dimAmount = 0f;
        if (rootView != null) {
            Drawable background = rootView.getBackground();
            if (background != null && background instanceof ColorDrawable) {
                ColorDrawable colordDrawable = (ColorDrawable) background;
                int color = colordDrawable.getColor();
                rootView.setBackgroundColor(ColorUtils.setAlphaComponent(color, (int) (255 * dimAmount)));
            }
        }
    }

    public void setAnimsStyle(@AnimRes int enter, @AnimRes int exit) {
        anims = new int[2];
        anims[0] = enter;
        anims[1] = exit;
    }

    public void setContentViewSize(int width,int height){
        if (contentView != null){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
            params.width = width;
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

    public abstract void initView();

    private void onWindowDetach() {
        if (activity != null) {
            if (dismissListener != null) {
                dismissListener.onDismiss(this);
            }
            if (activity.isFinishing()) {
                if (decorView != null) decorView.removeView(rootView);
                contentView = null;
                rootView = null;
                isAddContentView = false;
                activity = null;
            }
        }
    }

    public void setOnDecorDismissListener(OnDecorDismissListener listener) {
        this.dismissListener = listener;
    }

    public interface OnDecorDismissListener {
        void onDismiss(BaseDecorDialog dialog);
    }

    public interface DecorDialogDismissCallback {
        void dismissCall(BaseDecorDialog dialog, int index);
    }

    public void setRootTouchEvent(boolean isRootTouchEvent) {
        this.isRootTouchEvent = isRootTouchEvent;
    }
}
