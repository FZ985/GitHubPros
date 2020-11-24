package com.daynightjava1;

import android.content.Intent;
import android.view.View;

public class NewActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_new;
    }

    public void newActivity(View view) {
        startActivity(new Intent(this, NewActivity.class));
    }
}
