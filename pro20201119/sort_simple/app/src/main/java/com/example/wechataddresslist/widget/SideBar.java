package com.example.wechataddresslist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SideBar extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private int choose = -1;
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    private int height;
    private int parentHeight;
    private int top, bottom;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (height > 0) {
            setMeasuredDimension(widthMeasureSpec, height);
        } else {
            ViewParent parent = getParent().getParent();
            if (parent != null && parent instanceof ViewGroup && parentHeight == 0) {
                parentHeight = ((ViewGroup) parent).getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params != null && params instanceof FrameLayout.LayoutParams && (top == 0 || bottom == 0)) {
                top = ((FrameLayout.LayoutParams) params).topMargin;
                bottom = ((FrameLayout.LayoutParams) params).bottomMargin;
            } else if (params != null && params instanceof LinearLayout.LayoutParams && (top == 0 || bottom == 0)) {
                top = ((LinearLayout.LayoutParams) params).topMargin;
                bottom = ((LinearLayout.LayoutParams) params).bottomMargin;
            } else if (params != null && params instanceof RelativeLayout.LayoutParams && (top == 0 || bottom == 0)) {
                top = ((RelativeLayout.LayoutParams) params).topMargin;
                bottom = ((RelativeLayout.LayoutParams) params).bottomMargin;
            }
            height = parentHeight - top - bottom;
            if (height > 0) {
                setMeasuredDimension(widthMeasureSpec, height);
            } else
                setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;

        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.rgb(51, 51, 51));
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                // setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}
