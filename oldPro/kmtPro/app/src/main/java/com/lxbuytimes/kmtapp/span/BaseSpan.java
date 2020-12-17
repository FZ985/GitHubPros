package com.lxbuytimes.kmtapp.span;

/**
 * Create by JFZ
 * date: 2020-06-22 12:25
 **/
public interface BaseSpan<T> {

    interface ISpan {

    }

    T backgroundColor(int color);

    T textColor(int color);

    T textSize(int dpSize);

    T underLine();

    T deleteLine();

    //允许左边加一条竖线
    T quoteLine(int color, int stripeWidth, int gapWidth);

    T textStyle(int style);

    T click(Span.OnClickSpanListener listener);

    T roundSpan(RoundSpan spans);

    T addSpans(Object... spans);

}
