package com.totcy.salelibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.totcy.salelibrary.util.DensityUtil;
import com.totcy.salelibrary.util.DrawUtils;


/**
 * Description 水平滑动标尺
 * Author: tu
 * Date: 2016-08-25
 * Time: 18:38
 */
public class HorizontalScaleScrollView extends BaseScaleScrollView {

    private ScaleView scaleView;

    public HorizontalScaleScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScaleScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScaleScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            width = Math.max(width, childView.getMeasuredWidth());
            height = Math.max(height, childView.getMeasuredHeight());
        }
        //高度需要加上指针的高度 （height + 一个最大刻度高度 + 斜边的高度）
        height = (int) (height + scaleMaxHeight + DrawUtils.getCoordinateY(30, scaleMaxHeight));

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth
                : width, (heightMode == MeasureSpec.EXACTLY) ? sizeHeight
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //这里的话第一个childview是尺子 因此只需要设置宽度全屏 高度自适应(尺子)
        int count = getChildCount();
        int marginTop = (int) (scaleMaxHeight + DrawUtils.getCoordinateY(30, scaleMaxHeight));
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (i == 0) { //
                childView.layout(0, marginTop, childView.getMeasuredWidth(), childView.getMeasuredHeight() + marginTop);
            } else if (i == 1) {//指针
                int childWidth = childView.getMeasuredWidth();
                int childHeight = childView.getMeasuredHeight();
                childView.layout(getWidth() / 2 - childWidth / 2, cardViewPandding, getWidth() / 2 + childWidth / 2, childHeight + cardViewPandding);
            }
        }
    }

    /**
     * 设置最大和最小值
     *
     * @param min
     * @param max
     */
    public void setMinAndMaxScale(float min, float max) {
        scaleView.setMinAndMaxScale(min, max);
    }

    public void setIsIntegarl(boolean isIntegarl) {
        scaleView.setIntegar(isIntegarl);
    }

    private void init() {

        scaleView = new ScaleView(getContext());
        LinearLayout.LayoutParams lScale = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160);//LinearLayout.LayoutParams.WRAP_CONTENT
        scaleView.setLayoutParams(lScale);
        //初始化属性
        setMinAndMaxScale(minScale, maxScale);
        scaleView.setScaleWidth(scaleWidth);
        scaleView.setScaleMaxHeight(scaleMaxHeight);
        scaleView.setTextSize(scaleTextSize);
        scaleView.setTextColor(scaleColor);
        scaleView.setIntegar(isIntegar);
        scaleView.setShowUp(scaleShowUp);
        scaleView.setScaleShowShortUp(scaleShowShortUp);
//        Log.e("sjl","总宽度："+(5*(scaleWidth-20)*10)+"---每格的宽度："+scaleView.getScaleWidth()+"---"+scaleWidth);
        scaleView.setScrollListener(new ScaleView.OnScrollListener() {
            @Override
            public void onScaleScroll(float scale) {
                if (mScrollListener != null) {
                    if (scaleView.isIntegar())
                        mScrollListener.onScaleScroll((int) scale);
                    else
                        mScrollListener.onScaleScroll(scale);
                }
            }
        });
        int screenWidth = DensityUtil.getScreenWidth(getContext());
        int sc = 0 - (scaleWidth * 3) + (screenWidth / 2) + cardViewPandding * 2 + (DensityUtil.dp2px(getContext(), 3));
        scaleView.smoothScrollBy(-sc, 0, 0);
        //圆角
        FrameLayout.LayoutParams lpReflect = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ReflectItemView reflectItemView = new ReflectItemView(getContext());
        reflectItemView.setLayoutParams(lpReflect);
        reflectItemView.addView(scaleView);
        reflectItemView.setRadius(DensityUtil.dp2px(getContext(), 10));
        //边框
        FrameLayout.LayoutParams lpCardView = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        ReflectItemView cv = new ReflectItemView(getContext());
        cv.setLayoutParams(lpCardView);
        cv.addView(reflectItemView);
        cv.setBackgroundColor(Color.WHITE);
        cv.setRadius(DensityUtil.dp2px(getContext(), 10));
        cv.setPadding(cardViewPandding, cardViewPandding, cardViewPandding, cardViewPandding);

        addView(cv);

        //指针
        LinearLayout.LayoutParams lpPointer = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        PointerView pointerView = new PointerView(getContext());
        pointerView.setLayoutParams(lpPointer);
        pointerView.setScaleMaxHeight(scaleMaxHeight);
        pointerView.setVisibility(INVISIBLE);
        addView(pointerView);
        PointerView pointerView1 = new PointerView(getContext());
        pointerView1.setLayoutParams(lpPointer);
        pointerView1.setScaleMaxHeight(scaleMaxHeight);
        pointerView1.setShowUp(true);

        addView(pointerView1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Path mPath = new Path();
//        float rxY = getResources().getDimension(R.dimen.dim_10);
//        mPath.addRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),rxY,rxY,Path.Direction.CCW);//Path.Direction.CCW 顺时针方向
//        canvas.clipPath(mPath);

        super.draw(canvas);

    }

    private OnScrollListener mScrollListener;

    public interface OnScrollListener {
        void onScaleScroll(float scale);

        void onScaleScroll(int scale);
    }

    public void setScrollListener(OnScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public void smoothBy(int x, int y) {
        scaleView.smoothScrollBy((147 - 10) * scaleWidth, 0);
    }

    public void smoothScrollBy(int x, int y) {
        scaleView.smoothScrollBy(x, y);
        scaleView.invalidate();
    }

    public void scroll(String data, boolean isSugar) {
        scroll(data, 5.0f, isSugar);
    }

    /**
     * @param data   指定值
     * @param defVal 默认值
     */
    public void scroll(final String data, final float defVal, final boolean isSugar) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(data)) {
                    toScroll(defVal, isSugar);
                } else {
                    try {
                        float num = Float.parseFloat(data);
                        toScroll(num, isSugar);
                    } catch (NumberFormatException e) {
                        toScroll(defVal, isSugar);
                    }
                }
            }
        }, 300);
    }

    private void toScroll(float num, boolean isSugar) {
        float finalN;
        if (scaleView == null) return;
        System.out.println("num:" + num + "-----" + maxScale);
        if (num > maxScale) num = maxScale;
        float oldNum = scaleView.getThisScale();
        System.out.println("当前数为:" + oldNum);
        float index = num - oldNum;
        if (isSugar == true) {
            finalN = scaleWidth * index * 10;
        } else {
            finalN = scaleWidth * index;
        }
        System.out.println("最终滑动:" + finalN);
        smoothScrollBy((int) (finalN), 0);
    }
}
