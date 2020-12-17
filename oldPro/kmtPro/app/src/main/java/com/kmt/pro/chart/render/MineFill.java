package com.kmt.pro.chart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;

import com.github.mikephil.charting.utils.Fill;


public class MineFill extends Fill {

    private Paint mRenderPaint;

    public MineFill(int startColor, int endColor) {
        super(startColor, endColor);
        mRenderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRenderPaint.setStyle(Paint.Style.FILL);
        mRenderPaint.setColor(Color.parseColor("#F9F9F9"));
    }

    @Override
    public void fillRect(Canvas c, Paint paint, float left, float top, float right, float bottom, Direction gradientDirection) {
        if (getGradientColors() == null) return;

        LinearGradient gradient = new LinearGradient(
                (int) (gradientDirection == Direction.RIGHT
                        ? right
                        : gradientDirection == Direction.LEFT
                        ? left
                        : left),
                (int) (gradientDirection == Direction.UP
                        ? bottom
                        : gradientDirection == Direction.DOWN
                        ? top
                        : top),
                (int) (gradientDirection == Direction.RIGHT
                        ? left
                        : gradientDirection == Direction.LEFT
                        ? right
                        : left),
                (int) (gradientDirection == Direction.UP
                        ? top
                        : gradientDirection == Direction.DOWN
                        ? bottom
                        : top),
                getGradientColors(),
                getGradientPositions(),
                Shader.TileMode.MIRROR);

        paint.setShader(gradient);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            c.drawRoundRect(left, top, c.getWidth() - left, bottom, (bottom - top) / 2, (bottom - top) / 2, mRenderPaint);
            c.drawRoundRect(left, top, right, bottom, (bottom - top) / 2, (bottom - top) / 2, paint);
        } else {
            c.drawRect(left, top, right, bottom, paint);
        }
    }
}
