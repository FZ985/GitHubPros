
package com.kmt.pro.chart.render;

import android.graphics.Canvas;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;


public class MineYAxisRendererHorizontalBarChart extends YAxisRenderer {

    public MineYAxisRendererHorizontalBarChart(ViewPortHandler viewPortHandler, YAxis yAxis,
                                               Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        mLimitLinePaint.setTextAlign(Align.LEFT);
    }

    /**
     * Computes the axis values.
     *
     * @param yMin - the minimum y-value in the data object for this axis
     * @param yMax - the maximum y-value in the data object for this axis
     */
    @Override
    public void computeAxis(float yMin, float yMax, boolean inverted) {
    }

    /**
     * draws the y-axis labels to the screen
     */
    @Override
    public void renderAxisLabels(Canvas c) {
    }

    @Override
    public void renderAxisLine(Canvas c) {
    }

    /**
     * draws the y-labels on the specified x-position
     *
     * @param fixedPosition
     * @param positions
     */
    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
    }

    @Override
    protected float[] getTransformedPositions() {

        if(mGetTransformedPositionsBuffer.length != mYAxis.mEntryCount * 2) {
            mGetTransformedPositionsBuffer = new float[mYAxis.mEntryCount * 2];
        }
        float[] positions = mGetTransformedPositionsBuffer;
        return positions;
    }

    @Override
    public RectF getGridClippingRect() {
        return mGridClippingRect;
    }

    @Override
    protected Path linePath(Path p, int i, float[] positions) {
        return p;
    }

    protected Path mDrawZeroLinePathBuffer = new Path();

    @Override
    protected void drawZeroLine(Canvas c) {
    }

    protected Path mRenderLimitLinesPathBuffer = new Path();
    protected float[] mRenderLimitLinesBuffer = new float[4];
    /**
     * Draws the LimitLines associated with this axis to the screen.
     * This is the standard XAxis renderer using the YAxis limit lines.
     *
     * @param c
     */
    @Override
    public void renderLimitLines(Canvas c) {
    }
}
