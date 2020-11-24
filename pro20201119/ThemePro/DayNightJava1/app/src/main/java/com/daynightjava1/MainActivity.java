package com.daynightjava1;

import android.content.Intent;
import android.view.View;

import nightmode.DayNightMode;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    public void no(View view) {
        setNightMode(DayNightMode.MODE_NIGHT_NO);
    }

    public void yes(View view) {
        setNightMode(DayNightMode.MODE_NIGHT_YES);
    }

    public void activity(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void system(View view) {
        setNightMode(DayNightMode.MODE_NIGHT_FOLLOW_SYSTEM);
    }
}