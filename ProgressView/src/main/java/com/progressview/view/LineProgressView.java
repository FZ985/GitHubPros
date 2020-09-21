package com.progressview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.progressview.R;


/**
 * Created by JFZ
 * on 2018/7/14.
 */

public class LineProgressView extends View {
    private static final String TAG = "LineProgressView";
    private int width, height;//控件 宽高 定死
    private int LR_MARGIN, TB_MARGIN;//上下左右边距
    private int lineWidth;//线的宽度
    private Paint bgPanint;//底线画笔
    private Paint linePaint;//进度画笔
    private Bitmap carBitmap;//小汽车
    private Bitmap qiziBitmap;//旗子
    private int maxProgress = 100;//最大进度
    private int progress;//当前进度
    private int offest;

    private Rect qiziSrc, qiziDst;
    private Rect carSrc;

    //底线颜色
    private int bgLineColor = Color.parseColor("#000000");
    //进度线颜色
    private int progressLineColor = Color.parseColor("#EF4E4F");

    public LineProgressView(Context context) {
        this(context, null);
    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        width = getMetrics().widthPixels;
        height = dip2px(40);
        LR_MARGIN = dip2px(5);
        TB_MARGIN = dip2px(5);
        lineWidth = dip2px(5);
        offest = dip2px(7);
        bgPanint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPanint.setColor(bgLineColor);
        bgPanint.setStrokeWidth(lineWidth);
        bgPanint.setStrokeCap(Paint.Cap.ROUND);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(progressLineColor);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        carBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.car_three);
        qiziBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.qizi_);
        qiziSrc = new Rect(0, 0, qiziBitmap.getWidth(), qiziBitmap.getHeight());
        qiziDst = new Rect(width - qiziBitmap.getWidth(), height - qiziBitmap.getHeight() - lineWidth, width, height - lineWidth);
        carSrc = new Rect(0, 0, carBitmap.getWidth(), carBitmap.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制小旗子图片
        canvas.drawBitmap(qiziBitmap, qiziSrc, qiziDst, null);
        //绘制背景进度线
        RectF bgRect = new RectF();
        bgRect.left = carBitmap.getWidth() / 2;
        bgRect.top = height - lineWidth;
        bgRect.right = width - qiziBitmap.getWidth() + offest;
        bgRect.bottom = height;
        canvas.drawRoundRect(bgRect, lineWidth / 2, lineWidth / 2, bgPanint);
        int curr;
        int carOffest = 0;
        if (progress == maxProgress) {
            curr = carBitmap.getWidth() / 2;
            linePaint.setColor(bgPanint.getColor());
            carOffest = offest;
        } else {
            linePaint.setColor(progressLineColor);
            curr = carBitmap.getWidth() / 2;
        }
        //绘制小汽车
        Rect carDst = new Rect();
        carDst.left = (int) ((float) progress / maxProgress * (width - (carBitmap.getWidth() / 2) - (qiziBitmap.getWidth() + offest))) + carOffest;
        carDst.top = height - lineWidth - carBitmap.getHeight();
        carDst.right = carDst.left + carBitmap.getWidth();
        carDst.bottom = height - lineWidth;
        canvas.drawBitmap(carBitmap, carSrc, carDst, null);
        //绘制 进度线
        RectF proRect = new RectF();
        proRect.left = carDst.left + curr;
        proRect.top = height - lineWidth;
        proRect.right = width - qiziBitmap.getWidth() + offest;
        proRect.bottom = height;
        canvas.drawRoundRect(proRect, lineWidth / 2, lineWidth / 2, linePaint);

    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(0, 0, 0, 0);
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress > maxProgress ? maxProgress : progress;
        invalidate();
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private DisplayMetrics getMetrics() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }
}
