package com.jiaozi.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.jiaozi.demo.ijk.JZMediaIjk;
import com.jiaozi.demo.tiktok.TiktokActivity;

import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {
    private JzvdStd jzvdStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jzvdStd = findViewById(R.id.jzvdStd);
        jzvdStd.setUp(Urls.urls[0], "", JzvdStd.SCREEN_NORMAL, JZMediaIjk.class);

    }

    public void play(View view) {
        jzvdStd.startVideo();
    }

    public void douyin(View view) {
        startActivity(new Intent(this, TiktokActivity.class));
    }

}
