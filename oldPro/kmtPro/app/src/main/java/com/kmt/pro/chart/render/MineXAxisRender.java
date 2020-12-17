package com.kmt.pro.chart.render;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Create by JFZ
 * date: 2020-05-21 11:11
 **/
public class MineXAxisRender extends XAxisRenderer {

    protected BarChart mChart;

    public MineXAxisRender(ViewPortHandler viewPortHandler, XAxis xAxis,
                           Transformer trans, BarChart chart) {
        super(viewPortHandler, xAxis, trans);

        this.mChart = chart;
    }

    @Override
    public void computeAxis(float min, float max, boolean inverted) {

    }

    @Override
    protected void computeSize() {

    }

    @Override
    public void renderAxisLabels(Canvas c) {

    }

    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {

    }

    @Override
    public RectF getGridClippingRect() {
        mGridClippingRect.set(mViewPortHandler.getContentRect());
        mGridClippingRect.inset(0.f, -mAxis.getGridLineWidth());
        return mGridClippingRect;
    }

    @Override
    protected void drawGridLine(Canvas c, float x, float y, Path gridLinePath) {
    }

    @Override
    public void renderAxisLine(Canvas c) {
    }

    protected Path mRenderLimitLinesPathBuffer = new Path();

    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard YAxis renderer using the XAxis limit lines.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
    }
}
