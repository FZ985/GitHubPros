
package com.kmt.pro.chart.mine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.PieHighlighter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * View that represents a pie chart. Draws cake like slices.
 *
 * @author Philipp Jahoda
 */
public class MinePieChart extends PieChart {
    public MinePieChart(Context context) {
        super(context);
    }

    public MinePieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MinePieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        mRenderer = new PieChartRenderer(this, mAnimator, mViewPortHandler) {
            @Override
            protected void drawDataSet(Canvas c, IPieDataSet dataSet) {

                float angle = 0;
                float rotationAngle = getRotationAngle();

                float phaseX = mAnimator.getPhaseX();
                float phaseY = mAnimator.getPhaseY();

                final RectF circleBox = getCircleBox();

                final int entryCount = dataSet.getEntryCount();
                final float[] drawAngles = getDrawAngles();
                final MPPointF center = getCenterCircleBox();
                final float radius = getRadius();
                final boolean drawInnerArc = isDrawHoleEnabled() && !isDrawSlicesUnderHoleEnabled();
                final float userInnerRadius = drawInnerArc
                        ? radius * (getHoleRadius() / 100.f)
                        : 0.f;
                final float roundedRadius = (radius - (radius * getHoleRadius() / 100f)) / 2f;
                final RectF roundedCircleBox = new RectF();
                final boolean drawRoundedSlices = drawInnerArc && isDrawRoundedSlicesEnabled();

                int visibleAngleCount = 0;
                for (int j = 0; j < entryCount; j++) {
                    // draw only if the value is greater than zero
                    if ((Math.abs(dataSet.getEntryForIndex(j).getY()) > Utils.FLOAT_EPSILON)) {
                        visibleAngleCount++;
                    }
                }

                final float sliceSpace = visibleAngleCount <= 1 ? 0.f : getSliceSpace(dataSet);

                for (int j = 0; j < entryCount; j++) {

                    float sliceAngle = drawAngles[j];
                    float innerRadius = userInnerRadius;

                    Entry e = dataSet.getEntryForIndex(j);

                    // draw only if the value is greater than zero
                    if (!(Math.abs(e.getY()) > Utils.FLOAT_EPSILON)) {
                        angle += sliceAngle * phaseX;
                        continue;
                    }

                    // Don't draw if it's highlighted, unless the chart uses rounded slices
                    if (dataSet.isHighlightEnabled() && needsHighlight(j) && !drawRoundedSlices) {
                        angle += sliceAngle * phaseX;
                        continue;
                    }

                    final boolean accountForSliceSpacing = sliceSpace > 0.f && sliceAngle <= 180.f;


//                    if (e.getData() != null && e.getData() instanceof MinePieFill) {
//                        MinePieFill fill = (MinePieFill) e.getData();
//                        int[] colors = new int[2];
//                        colors[0] = fill.startColor;
//                        colors[1] = fill.endColor;
//                        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
//                        mRenderPaint.setShader(sweepGradient);
//                    } else {
//                        mRenderPaint.setColor(dataSet.getColor(j));
//                    }


                    final float sliceSpaceAngleOuter = visibleAngleCount == 1 ?
                            0.f :
                            sliceSpace / (Utils.FDEG2RAD * radius);
                    final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.f) * phaseY;
                    float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
                    if (sweepAngleOuter < 0.f) {
                        sweepAngleOuter = 0.f;
                    }

                    mPathBuffer.reset();

                    if (drawRoundedSlices) {
                        float x = center.x + (radius - roundedRadius) * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD);
                        float y = center.y + (radius - roundedRadius) * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD);
                        roundedCircleBox.set(x - roundedRadius, y - roundedRadius, x + roundedRadius, y + roundedRadius);
                    }

                    float arcStartPointX = center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD);
                    float arcStartPointY = center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD);


                    if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                        // Android is doing "mod 360"
                        mPathBuffer.addCircle(center.x, center.y, radius, Path.Direction.CW);
                    } else {
                        if (drawRoundedSlices) {
                            mPathBuffer.arcTo(roundedCircleBox, startAngleOuter + 180, -180);
                        }

                        mPathBuffer.arcTo(
                                circleBox,
                                startAngleOuter,
                                sweepAngleOuter
                        );
                    }
                    // API < 21 does not receive floats in addArc, but a RectF
                    mInnerRectBuffer.set(
                            center.x - innerRadius,
                            center.y - innerRadius,
                            center.x + innerRadius,
                            center.y + innerRadius);


                    if (drawInnerArc && (innerRadius > 0.f || accountForSliceSpacing)) {

                        if (accountForSliceSpacing) {
                            float minSpacedRadius =
                                    calculateMinimumRadiusForSpacedSlice(
                                            center, radius,
                                            sliceAngle * phaseY,
                                            arcStartPointX, arcStartPointY,
                                            startAngleOuter,
                                            sweepAngleOuter);

                            if (minSpacedRadius < 0.f)
                                minSpacedRadius = -minSpacedRadius;

                            innerRadius = Math.max(innerRadius, minSpacedRadius);
                        }

                        final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0.f ?
                                0.f :
                                sliceSpace / (Utils.FDEG2RAD * innerRadius);
                        final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.f) * phaseY;
                        float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                        if (sweepAngleInner < 0.f) {
                            sweepAngleInner = 0.f;
                        }
                        final float endAngleInner = startAngleInner + sweepAngleInner;

                        if (sweepAngleOuter >= 360.f && sweepAngleOuter % 360f <= Utils.FLOAT_EPSILON) {
                            // Android is doing "mod 360"
                            mPathBuffer.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                        } else {

                            if (drawRoundedSlices) {
                                float x = center.x + (radius - roundedRadius) * (float) Math.cos(endAngleInner * Utils.FDEG2RAD);
                                float y = center.y + (radius - roundedRadius) * (float) Math.sin(endAngleInner * Utils.FDEG2RAD);
                                roundedCircleBox.set(x - roundedRadius, y - roundedRadius, x + roundedRadius, y + roundedRadius);
                                mPathBuffer.arcTo(roundedCircleBox, endAngleInner, 180);
                            } else
                                mPathBuffer.lineTo(
                                        center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                                        center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD));

                            mPathBuffer.arcTo(
                                    mInnerRectBuffer,
                                    endAngleInner,
                                    -sweepAngleInner
                            );
                        }
                    } else {

                        if (sweepAngleOuter % 360f > Utils.FLOAT_EPSILON) {
                            if (accountForSliceSpacing) {

                                float angleMiddle = startAngleOuter + sweepAngleOuter / 2.f;

                                float sliceSpaceOffset =
                                        calculateMinimumRadiusForSpacedSlice(
                                                center,
                                                radius,
                                                sliceAngle * phaseY,
                                                arcStartPointX,
                                                arcStartPointY,
                                                startAngleOuter,
                                                sweepAngleOuter);

                                float arcEndPointX = center.x +
                                        sliceSpaceOffset * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                                float arcEndPointY = center.y +
                                        sliceSpaceOffset * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);

                                mPathBuffer.lineTo(
                                        arcEndPointX,
                                        arcEndPointY);

                            } else {
                                mPathBuffer.lineTo(
                                        center.x,
                                        center.y);
                            }
                        }
                    }

                    mPathBuffer.close();
                    if (e.getData() != null && e.getData() instanceof MinePieFill) {
                        MinePieFill fill = (MinePieFill) e.getData();
                        int[] colors = new int[2];
                        colors[0] = fill.startColor;
                        colors[1] = fill.endColor;
                        SweepGradient sweepGradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, null);
                        mRenderPaint.setShader(sweepGradient);
                    } else {
                        mRenderPaint.setColor(dataSet.getColor(j));
                    }

                    mBitmapCanvas.drawPath(mPathBuffer, mRenderPaint);

                    angle += sliceAngle * phaseX;
                }

                MPPointF.recycleInstance(center);
            }
        };
        mXAxis = null;

        mHighlighter = new PieHighlighter(this);
    }


}
