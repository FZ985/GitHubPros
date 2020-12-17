package jiang.photo.picker.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

import androidx.core.graphics.ColorUtils;

/**
 * Create by JFZ
 * date: 2020-01-06 10:45
 **/
public class PhotoProgressBar extends View {

    private Context mContext;

    private static final float INDETERMINANT_MIN_SWEEP = 15f;

    private Paint paint;
    private int size = 0;
    private RectF bounds;

    private boolean isIndeterminate, autostartAnimation;
    private float currentProgress, maxProgress, indeterminateSweep, indeterminateRotateOffset;
    private int thickness, color, animDuration, animSwoopDuration, animSyncDuration, animSteps;

    private List<UIProgressListener> listeners;
    // Animation related stuff
    private float startAngle;
    private float actualProgress;
    private ValueAnimator startAngleRotate;
    private ValueAnimator progressAnimator;
    private AnimatorSet indeterminateAnimator;
    private float initialStartAngle;

    public PhotoProgressBar(Context context) {
        this(context, null);
    }

    public PhotoProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * Listener interface to provide different callbacks for Circular Progress Views.
     */
    public interface UIProgressListener {
        /**
         * Called when setProgress(float currentProgress) is called (determinate only)
         *
         * @param currentProgress The progress that was set.
         */
        void onProgressUpdate(float currentProgress);

        /**
         * Called when this view finishes animating to the updated progress. (Determinate only)
         *
         * @param currentProgress The progress that was set and this view has reached in its animation.
         */
        void onProgressUpdateEnd(float currentProgress);

        /**
         * Called when resetAnimation() is called.
         */
        void onAnimationReset();

        /**
         * Called when you switch between indeterminate and determinate modes.
         *
         * @param isIndeterminate true if mode was set to indeterminate, false otherwise.
         */
        void onModeChanged(boolean isIndeterminate);
    }

    private void init() {
        listeners = new ArrayList<>();
        initAttributes();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        updatePaint();
        bounds = new RectF();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initAttributes() {

        // Initialize attributes from styleable attributes
        currentProgress = 0;
        maxProgress = 100;
        thickness = dip2px(mContext, 4);
        isIndeterminate = true;
        autostartAnimation = false;
        initialStartAngle = -90;
        startAngle = initialStartAngle;

        // If color explicitly provided
        color = ColorUtils.setAlphaComponent(Color.WHITE, 220);

        animDuration = 4000;
        animSwoopDuration = 5000;
        animSyncDuration = 500;
        animSteps = 3;
    }

    private void updatePaint() {
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(thickness);
//        paint.setStrokeCap(Paint.Cap.BUTT);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int xPad = getPaddingLeft() + getPaddingRight();
        int yPad = getPaddingTop() + getPaddingBottom();
        int width = getMeasuredWidth() - xPad;
        int height = getMeasuredHeight() - yPad;
        size = (width < height) ? width : height;
        setMeasuredDimension(size + xPad, size + yPad);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = (w < h) ? w : h;
        updateBounds();
    }

    private void updateBounds() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        bounds.set(paddingLeft + thickness, paddingTop + thickness, size - paddingLeft - thickness, size - paddingTop - thickness);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the arc
        float sweepAngle = (isInEditMode()) ? currentProgress / maxProgress * 360 : actualProgress / maxProgress * 360;
        if (!isIndeterminate)
            canvas.drawArc(bounds, startAngle, sweepAngle, false, paint);
        else
            canvas.drawArc(bounds, startAngle + indeterminateRotateOffset, indeterminateSweep, false, paint);
    }


    /**
     * Returns the mode of this view (determinate or indeterminate).
     *
     * @return true if this view is in indeterminate mode.
     */
    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    /**
     * Sets whether this CircularProgressView is indeterminate or not.
     * It will reset the animation if the mode has changed.
     *
     * @param isIndeterminate True if indeterminate.
     */
    public void setIndeterminate(boolean isIndeterminate) {
        boolean old = this.isIndeterminate;
        boolean reset = this.isIndeterminate == isIndeterminate;
        this.isIndeterminate = isIndeterminate;
        if (reset)
            resetAnimation();
        if (old != isIndeterminate) {
            for (UIProgressListener listener : listeners) {
                listener.onModeChanged(isIndeterminate);
            }
        }
    }

    /**
     * Get the thickness of the progress bar arc.
     *
     * @return the thickness of the progress bar arc
     */
    public int getThickness() {
        return thickness;
    }

    /**
     * Sets the thickness of the progress bar arc.
     *
     * @param thickness the thickness of the progress bar arc
     */
    public void setThickness(int thickness) {
        this.thickness = thickness;
        updatePaint();
        updateBounds();
        invalidate();
    }

    /**
     * @return the color of the progress bar
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of the progress bar.
     *
     * @param color the color of the progress bar
     */
    public void setColor(int color) {
        this.color = color;
        updatePaint();
        invalidate();
    }

    /**
     * Gets the progress value considered to be 100% of the progress bar.
     *
     * @return the maximum progress
     */
    public float getMaxProgress() {
        return maxProgress;
    }

    /**
     * Sets the progress value considered to be 100% of the progress bar.
     *
     * @param maxProgress the maximum progress
     */
    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    /**
     * @return current progress
     */
    public float getProgress() {
        return currentProgress;
    }

    /**
     * Sets the progress of the progress bar.
     *
     * @param currentProgress the new progress.
     */
    public void setProgress(final float currentProgress) {
        this.currentProgress = currentProgress;
        // Reset the determinate animation to approach the new currentProgress
        if (!isIndeterminate) {
            if (progressAnimator != null && progressAnimator.isRunning())
                progressAnimator.cancel();
            progressAnimator = ValueAnimator.ofFloat(actualProgress, currentProgress);
            progressAnimator.setDuration(animSyncDuration);
            progressAnimator.setInterpolator(new LinearInterpolator());
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    actualProgress = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            progressAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    for (UIProgressListener listener : listeners) {
                        listener.onProgressUpdateEnd(currentProgress);
                    }
                }
            });

            progressAnimator.start();
        }
        invalidate();
        for (UIProgressListener listener : listeners) {
            listener.onProgressUpdate(currentProgress);
        }
    }

    /**
     * Register a CircularProgressViewListener with this View
     *
     * @param listener The listener to register
     */
    public void addListener(UIProgressListener listener) {
        if (listener != null)
            listeners.add(listener);
    }

    /**
     * Unregister a CircularProgressViewListener with this View
     *
     * @param listener The listener to unregister
     */
    public void removeListener(UIProgressListener listener) {
        listeners.remove(listener);
    }

    /**
     * Starts the progress bar animation.
     * (This is an alias of resetAnimation() so it does the same thing.)
     */
    public void startAnimation() {
        resetAnimation();
    }

    /**
     * Resets the animation.
     */
    public void resetAnimation() {
        // Cancel all the old animators
        if (startAngleRotate != null && startAngleRotate.isRunning())
            startAngleRotate.cancel();
        if (progressAnimator != null && progressAnimator.isRunning())
            progressAnimator.cancel();
        if (indeterminateAnimator != null && indeterminateAnimator.isRunning())
            indeterminateAnimator.cancel();
        // Determinate animation
        if (!isIndeterminate) {
            // The cool 360 swoop animation at the start of the animation
            startAngle = initialStartAngle;
            startAngleRotate = ValueAnimator.ofFloat(startAngle, startAngle + 360);
            startAngleRotate.setDuration(animSwoopDuration);
            startAngleRotate.setInterpolator(new DecelerateInterpolator(2));
            startAngleRotate.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    startAngle = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            startAngleRotate.start();

            // The linear animation shown when progress is updated
            actualProgress = 0f;
            progressAnimator = ValueAnimator.ofFloat(actualProgress, currentProgress);
            progressAnimator.setDuration(animSyncDuration);
            progressAnimator.setInterpolator(new LinearInterpolator());
            progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    actualProgress = (Float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            progressAnimator.start();
        }
        // Indeterminate animation
        else {
            indeterminateSweep = INDETERMINANT_MIN_SWEEP;
            // Build the whole AnimatorSet
            indeterminateAnimator = new AnimatorSet();
            AnimatorSet prevSet = null, nextSet;
            for (int k = 0; k < animSteps; k++) {
                nextSet = createIndeterminateAnimator(k);
                AnimatorSet.Builder builder = indeterminateAnimator.play(nextSet);
                if (prevSet != null)
                    builder.after(prevSet);
                prevSet = nextSet;
            }

            // Listen to end of animation so we can infinitely loop
            indeterminateAnimator.addListener(new AnimatorListenerAdapter() {
                boolean wasCancelled = false;

                @Override
                public void onAnimationCancel(Animator animation) {
                    wasCancelled = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!wasCancelled)
                        resetAnimation();
                }
            });
            indeterminateAnimator.start();
            for (UIProgressListener listener : listeners) {
                listener.onAnimationReset();
            }
        }


    }

    /**
     * Stops the animation
     */
    public void stopAnimation() {
        if (startAngleRotate != null) {
            startAngleRotate.cancel();
            startAngleRotate = null;
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
            progressAnimator = null;
        }
        if (indeterminateAnimator != null) {
            indeterminateAnimator.cancel();
            indeterminateAnimator = null;
        }
    }

    // Creates the animators for one step of the animation
    private AnimatorSet createIndeterminateAnimator(float step) {
        final float maxSweep = 360f * (animSteps - 1) / animSteps + INDETERMINANT_MIN_SWEEP;
        final float start = -90f + step * (maxSweep - INDETERMINANT_MIN_SWEEP);

        // Extending the front of the arc
        ValueAnimator frontEndExtend = ValueAnimator.ofFloat(INDETERMINANT_MIN_SWEEP, maxSweep);
        frontEndExtend.setDuration(animDuration / animSteps / 2);
        frontEndExtend.setInterpolator(new DecelerateInterpolator(1));
        frontEndExtend.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateSweep = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });

        // Overall rotation
        ValueAnimator rotateAnimator1 = ValueAnimator.ofFloat(step * 720f / animSteps, (step + .5f) * 720f / animSteps);
        rotateAnimator1.setDuration(animDuration / animSteps / 2);
        rotateAnimator1.setInterpolator(new LinearInterpolator());
        rotateAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateRotateOffset = (Float) animation.getAnimatedValue();
            }
        });

        // Followed by...

        // Retracting the back end of the arc
        ValueAnimator backEndRetract = ValueAnimator.ofFloat(start, start + maxSweep - INDETERMINANT_MIN_SWEEP);
        backEndRetract.setDuration(animDuration / animSteps / 2);
        backEndRetract.setInterpolator(new DecelerateInterpolator(1));
        backEndRetract.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (Float) animation.getAnimatedValue();
                indeterminateSweep = maxSweep - startAngle + start;
                invalidate();
            }
        });

        // More overall rotation
        ValueAnimator rotateAnimator2 = ValueAnimator.ofFloat((step + .5f) * 720f / animSteps, (step + 1) * 720f / animSteps);
        rotateAnimator2.setDuration(animDuration / animSteps / 2);
        rotateAnimator2.setInterpolator(new LinearInterpolator());
        rotateAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                indeterminateRotateOffset = (Float) animation.getAnimatedValue();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(frontEndExtend).with(rotateAnimator1);
        set.play(backEndRetract).with(rotateAnimator2).after(rotateAnimator1);
        return set;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (autostartAnimation)
            startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

    @Override
    public void setVisibility(int visibility) {
        int currentVisibility = getVisibility();
        super.setVisibility(visibility);
        if (visibility != currentVisibility) {
            if (visibility == View.VISIBLE) {
                resetAnimation();
            } else if (visibility == View.GONE || visibility == View.INVISIBLE) {
                stopAnimation();
            }
        }
    }

}
