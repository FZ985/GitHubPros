package com.daynightjava1;

import android.os.Bundle;

import androidx.annotation.Nullable;

import nightmode.DayNightModeActivity;

public abstract class BaseActivity extends DayNightModeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
    }

    protected abstract int getLayout();
}
