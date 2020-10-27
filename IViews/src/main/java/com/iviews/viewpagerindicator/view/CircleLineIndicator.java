package com.iviews.viewpagerindicator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CircleLineIndicator extends View {
    private Paint paintStroke;
    private int mNum;//个数
    private float mRadius;//半径
    private float mOffset;//偏移量
    private float mDistance;//间隔距离
    private int mPosition;//第几个
    private boolean mIsLeft;
    private boolean mIsInfiniteCircle;
    private boolean mAnimation = true;
    private int color;

    public CircleLineIndicator(Context context) {
        this(context, null);
    }

    public CircleLineIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRadius != 0) {
            setMeasuredDimension(widthMeasureSpec, (int) (2 * mRadius));
        } else setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    private void initPaint() {
        paintStroke = new Paint();
        paintStroke.setStyle(Paint.Style.FILL);
        paintStroke.setColor(color);
        paintStroke.setAntiAlias(true);
        paintStroke.setStrokeWidth(3);
        if (mRadius != 0 && mDistance == 0) {
            mDistance = 3 * mRadius;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mNum <= 0)) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.translate(width / 2, height / 2);
        //初始化画笔
        initPaint();
        if (mPosition == mNum - 1) {//最后一个 右滑
            //第一个 线 选中 消失
            float leftClose = -(mNum) * 0.5f * mDistance - mRadius;
            float rightClose = leftClose + 2 * mRadius + mOffset;
            float topClose = -mRadius;
            float bottomClose = mRadius;
            RectF rectClose = new RectF(leftClose, topClose, rightClose, bottomClose);// 设置个新的长方形
            canvas.drawRoundRect(rectClose, mRadius, mRadius, paintStroke);
            //最后一个 线  显示
            float rightOpen = -(mNum) * 0.5f * mDistance + mNum * mDistance + mRadius;
            float leftOpen = rightOpen - 2 * mRadius - mDistance + mOffset;
            float topOpen = -mRadius;
            float bottomOpen = mRadius;
            RectF rectOpen = new RectF(leftOpen, topOpen, rightOpen, bottomOpen);// 设置个新的长方形
            canvas.drawRoundRect(rectOpen, mRadius, mRadius, paintStroke);
            //圆
            for (int i = 1; i < mNum; i++) {
                canvas.drawCircle(rightClose - mRadius + i * mDistance, 0, mRadius, paintStroke);
            }
        } else {
            //第一个 线 选中 消失
            float leftClose = -(mNum) * 0.5f * mDistance + mPosition * mDistance - mRadius;
            float rightClose = leftClose + 2 * mRadius + mDistance - mOffset;
            float topClose = -mRadius;
            float bottomClose = mRadius;
            RectF rectClose = new RectF(leftClose, topClose, rightClose, bottomClose);// 设置个新的长方形
            canvas.drawRoundRect(rectClose, mRadius, mRadius, paintStroke);
            //第二个 线  显示
            if (mPosition < mNum - 1) {
                float rightOpen = -(mNum) * 0.5f * mDistance + (mPosition + 2) * mDistance + mRadius;
                float leftOpen = rightOpen - 2 * mRadius - mOffset;
                float topOpen = -mRadius;
                float bottomOpen = mRadius;
                RectF rectOpen = new RectF(leftOpen, topOpen, rightOpen, bottomOpen);// 设置个新的长方形
                canvas.drawRoundRect(rectOpen, mRadius, mRadius, paintStroke);
            }
            //圆
            for (int i = mPosition + 3; i <= mNum; i++) {
                canvas.drawCircle(-(mNum) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
            }
            for (int i = mPosition - 1; i >= 0; i--) {
                canvas.drawCircle(-(mNum) * 0.5f * mDistance + i * mDistance, 0, mRadius, paintStroke);
            }
        }
    }

    public void move(float percent, int position, boolean isLeft) {
        mPosition = position;
        mIsLeft = isLeft;
        if (mPosition == mNum - 1 && !isLeft) {//第一个 右滑
            mOffset = percent * mDistance;
        }
        if (mPosition == mNum - 1 && isLeft) {//最后一个 左滑
            mOffset = percent * mDistance;
        } else {//中间
            mOffset = percent * mDistance;
        }
        invalidate();
    }

    public CircleLineIndicator setRadius(float radius) {
        this.mRadius = radius;
        requestLayout();
        return this;
    }

    public CircleLineIndicator setViewPager(ViewPager viewPager) {
        setViewPager(viewPager, viewPager.getAdapter().getCount(), false);
        return this;
    }

    public CircleLineIndicator setColor(int color) {
        this.color = color;
        invalidate();
        return this;
    }

    private CircleLineIndicator setViewPager(ViewPager viewpager, int CycleNumber, boolean isInfiniteCircle) {
        mNum = CycleNumber;
        mIsInfiniteCircle = isInfiniteCircle;
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //记录上一次滑动的positionOffsetPixels值
            private int lastValue = -1;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!mAnimation) {
                    //不需要动画
                    return;
                }
                boolean isLeft = mIsLeft;
                if (lastValue / 10 > positionOffsetPixels / 10) {
                    //右滑
                    isLeft = false;
                } else if (lastValue / 10 < positionOffsetPixels / 10) {
                    //左滑
                    isLeft = true;
                }
                if (mNum > 0 && !mIsInfiniteCircle) {
                    move(positionOffset, position % mNum, isLeft);
                } else if (mNum > 0 && mIsInfiniteCircle) {
                    if (position == 0) {
                        position = mNum - 1;
                    } else if (position == mNum + 1) {
                        position = 0;
                    } else {
                        position--;
                    }
                    move(positionOffset, position, isLeft);
                }
                lastValue = positionOffsetPixels;
            }

            @Override
            public void onPageSelected(int position) {
                if (mAnimation) {
                    //需要动画
                    return;
                }
                if (mNum > 0 && !mIsInfiniteCircle) {
                    move(0, position % mNum, false);
                } else if (mNum > 0 && mIsInfiniteCircle) {
                    if (position == 0) {
                        position = mNum - 1;
                    } else if (position == mNum + 1) {
                        position = 0;
                    } else {
                        position--;
                    }
                    move(0, position, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return this;
    }

    public void setDistance(float mDistance) {
        this.mDistance = mDistance;
        invalidate();
    }
}
