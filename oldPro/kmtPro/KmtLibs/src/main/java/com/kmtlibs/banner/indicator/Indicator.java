package com.kmtlibs.banner.indicator;

import android.view.View;

import com.kmtlibs.banner.config.IndicatorConfig;
import com.kmtlibs.banner.listener.OnPageChangeListener;

import androidx.annotation.NonNull;

public interface Indicator extends OnPageChangeListener {
    @NonNull
    View getIndicatorView();

    IndicatorConfig getIndicatorConfig();

    void onPageChanged(int count, int currentPosition);

}
