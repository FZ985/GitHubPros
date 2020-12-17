package com.lxbuytimes.kmtapp.span;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Create by JFZ
 * date: 2020-06-22 14:23
 **/
public class RoundSpan extends ReplacementSpan implements BaseSpan.ISpan {
    private int borderWidth = 2;//边框线宽度
    private int backgroundColor = Color.TRANSPARENT;//背景色
    private int textColor;
    private boolean isTextColor;
    private int radius;
    private Paint.Style style = Paint.Style.FILL;
    private int size;
    private int marginLeft, marginRight;
    private float offsetLeft, offsetRight;//左右偏移量
    private float textSize;

    public RoundSpan(int backgroundColor, int radius, Paint.Style style, int marginLeft, int marginRight, float offsetLeft, float offsetRight) {
        this.backgroundColor = backgroundColor;
        this.radius = radius;
        this.style = style;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.offsetLeft = offsetLeft;
        this.offsetRight = offsetRight;
    }

    public RoundSpan textSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    public RoundSpan textColor(int textColor) {
        isTextColor = true;
        this.textColor = textColor;
        return this;
    }

    public RoundSpan(int backgroundColor, int radius, Paint.Style style) {
        this(backgroundColor, radius, style, 0, 0, 0f, 0f);
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        size = (int) (offsetRight + offsetRight + marginLeft + marginRight + paint.measureText(text, start, end));
        return size;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int textColor = paint.getColor();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(style);
        paint.setColor(backgroundColor);
        RectF oval = new RectF(x + offsetLeft, y + paint.ascent(), x + size - offsetRight, y + paint.descent());
        canvas.drawRoundRect(oval, radius, radius, paint);
        paint.setColor(isTextColor ? RoundSpan.this.textColor : textColor);
        paint.setTextSize(textSize == 0 ? paint.getTextSize() : textSize);
        paint.setStrokeWidth(0);
        float textX = x + (size / 2 - (paint.measureText(text, start, end)) / 2);
        //做一下双方都设置了文字大小的兼容
        if (textSize == 0) {
            canvas.drawText(text, start, end, textX, y, paint);
        } else {
            canvas.drawText(text, start, end, textX, y - paint.descent() / 2 + borderWidth, paint);
        }
//        canvas.drawText(text, start, end, x + offsetLeft + marginLeft + 2 * borderWidth, y, paint);
    }

    private Rect getTextRect(String text, int start, int end) {
        Paint paint = new Paint();
        Rect rect = new Rect();
        paint.getTextBounds(text, start, end, rect);
        return rect;
    }
}
