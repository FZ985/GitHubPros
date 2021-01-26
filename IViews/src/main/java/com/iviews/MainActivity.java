package com.iviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.iviews.viewpagerindicator.IndicatorActivity;
import com.totcy.salelibrary.HorizontalScaleScrollView;

public class MainActivity extends AppCompatActivity {
    private HorizontalScaleScrollView horizontalScaleScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        horizontalScaleScrollView = findViewById(R.id.hsScale2);
        initData();
    }

    private void initData() {

    }

    public void vpIndicator(View view) {
        startActivity(new Intent(this, IndicatorActivity.class));
    }
}
