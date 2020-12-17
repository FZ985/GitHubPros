package com.lxbuytimes.kmtapp.span;

import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-06-22 10:39
 **/
public class Span {
    private List<Builder> builders;
    private int hightLightColor = Color.TRANSPARENT;
    private OnClickSpanListener totalClickListener;

    private Span() {
        builders = new ArrayList<>();
    }

    public static Span impl() {
        return new Span();
    }

    public Span append(Builder builder) {
        builders.add(builder);
        return this;
    }

    public static Builder builder(String text) {
        return new Builder(text);
    }

    public Span totalClickListener(OnClickSpanListener listener) {
        this.totalClickListener = listener;
        return this;
    }

    public void into(TextView tv) {
        if (tv != null && builders != null && builders.size() > 0) {
            SpannableStringBuilder string = new SpannableStringBuilder("");
            String totalText = getTotalText(builders);
            for (int i = 0; i < builders.size(); i++) {
                SpanBuilder builder = builders.get(i).getSpanBuilder();
                if (!TextUtils.isEmpty(builder.getText())) {
                    SpannableString span = new SpannableString(builder.getText());
                    List<Object> spans = builder.getSpans();
                    for (Object style : spans) {
                        span.setSpan(style, 0, builder.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    for (Object style : builder.getSpansList()) {
                        span.setSpan(style, 0, builder.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (totalClickListener != null) {
                        span.setSpan(new Span.TextClickSpannable(totalText, builder.isUnderLine, totalClickListener), 0, builder.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    string.append(span);
                }
            }

            tv.setText(string);
            tv.setMovementMethod(LinkMovementMethodImpl.getInstance());
            tv.setHighlightColor(hightLightColor);
        }
    }

    private String getTotalText(List<Builder> builders) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < builders.size(); i++) {
            if (!TextUtils.isEmpty(builders.get(i).getSpanBuilder().getText())) {
                stringBuilder.append(builders.get(i).getSpanBuilder().getText());
            }
        }
        return stringBuilder.toString();
    }

    public static class Builder implements BaseSpan<Builder> {
        private SpanBuilder spanBuilder;

        private Builder(String text) {
            spanBuilder = new SpanBuilder(text);
        }

        @Override
        public Builder backgroundColor(int color) {
            spanBuilder.backgroundColor(color);
            return this;
        }

        @Override
        public Builder textColor(int color) {
            spanBuilder.textColor(color);
            return this;
        }

        @Override
        public Builder textSize(int dpSize) {
            spanBuilder.textSize(dpSize);
            return this;
        }

        @Override
        public Builder underLine() {
            spanBuilder.underLine();
            return this;
        }

        @Override
        public Builder deleteLine() {
            spanBuilder.deleteLine();
            return this;
        }

        @Override
        public Builder quoteLine(int color, int stripeWidth, int gapWidth) {
            spanBuilder.quoteLine(color, stripeWidth, gapWidth);
            return this;
        }

        @Override
        public Builder click(OnClickSpanListener listener) {
            spanBuilder.click(listener);
            return this;
        }

        @Override
        public Builder addSpans(Object... spans) {
            spanBuilder.addSpans(spans);
            return this;
        }

        @Override
        public Builder roundSpan(RoundSpan spans) {
            spanBuilder.roundSpan(spans);
            return this;
        }

        @Override
        public Builder textStyle(int style) {
            spanBuilder.textStyle(style);
            return this;
        }

        public SpanBuilder getSpanBuilder() {
            return spanBuilder;
        }
    }

    private static class SpanBuilder implements BaseSpan<SpanBuilder> {
        private SparseArray<Object> spans;
        private List<Object> spanslist;
        private String text;
        public boolean isUnderLine = false;

        private SpanBuilder(String text) {
            if (text == null || text.equals("")) text = "null";
            this.text = text;
            spans = new SparseArray<>();
        }

        @Override
        public SpanBuilder backgroundColor(int color) {
            spans.put(1, new BackgroundColorSpan(color));
            return this;
        }

        @Override
        public SpanBuilder textColor(int color) {
            spans.put(2, new ForegroundColorSpan(color));
            return this;
        }

        @Override
        public SpanBuilder textSize(int dpSize) {
            spans.put(3, new AbsoluteSizeSpan(dpSize, true));
            return this;
        }

        @Override
        public SpanBuilder underLine() {
            this.isUnderLine = true;
            spans.put(4, new UnderlineSpan());
            return this;
        }

        @Override
        public SpanBuilder deleteLine() {
            spans.put(5, new StrikethroughSpan());
            return this;
        }

        @Override
        public SpanBuilder quoteLine(int color, int stripeWidth, int gapWidth) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                spans.put(7, new QuoteSpan(color, stripeWidth, gapWidth));
            }
            return this;
        }

        @Override
        public SpanBuilder roundSpan(RoundSpan round) {
            spans.put(8, round);
            return this;
        }

        /**
         * 默认: Typeface.NORMAL
         * 加粗: Typeface.BOLD
         * 倾斜: Typeface.ITALIC
         * 加粗并倾斜: Typeface.BOLD_ITALIC
         *
         * @param style
         * @return
         */
        @Override
        public SpanBuilder textStyle(int style) {
            spans.put(9, new StyleSpan(style));
            return this;
        }

        public String getText() {
            return text;
        }

        @Override
        public SpanBuilder click(OnClickSpanListener listener) {
            if (listener != null) {
                spans.put(6, new Span.TextClickSpannable(text, isUnderLine, listener));
            }
            return this;
        }

        @Override
        public SpanBuilder addSpans(Object... spans) {
            if (spans != null) {
                spanslist = new ArrayList<>();
                for (int i = 0; i < spans.length; i++) {
                    spanslist.add(spans[i]);
                }
            }
            return this;
        }

        public List<Object> getSpansList() {
            if (spanslist == null) {
                spanslist = new ArrayList<>();
            }
            return spanslist;
        }


        public List<Object> getSpans() {
            if (spans == null) {
                spans = new SparseArray<>();
            }
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < spans.size(); i++) {
                list.add(spans.valueAt(i));
            }
            return list;
        }
    }

    private static class LinkMovementMethodImpl extends LinkMovementMethod {
        private static Span.LinkMovementMethodImpl instance;

        public LinkMovementMethodImpl() {
        }

        public static Span.LinkMovementMethodImpl getInstance() {
            if (instance == null) {
                instance = new Span.LinkMovementMethodImpl();
            }
            return instance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            boolean b = super.onTouchEvent(widget, buffer, event);
            int action = event.getAction();
            if (action == MotionEvent.ACTION_UP) {
                if (!b) {
                    ViewParent parent = widget.getParent();
                    if (parent instanceof ViewGroup) {
                        // 获取被点击控件的父容器，让父容器执行点击；
                        ((ViewGroup) parent).performClick();
                    }
                }
            }
            return b;
        }
    }

    private static class TextClickSpannable extends ClickableSpan {
        private Span.OnClickSpanListener onClickSpanListener;
        private String text;
        private boolean isUnderLine;

        public TextClickSpannable(String text, boolean isUnderLine, Span.OnClickSpanListener onClickSpanListener) {
            this.onClickSpanListener = onClickSpanListener;
            this.text = text;
            this.isUnderLine = isUnderLine;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderLine);
        }

        @Override
        public void onClick(View widget) {
            if (onClickSpanListener != null) {
                onClickSpanListener.onClick(text, widget);
            }
        }
    }

    public interface OnClickSpanListener {
        void onClick(String text, View widget);
    }
}
