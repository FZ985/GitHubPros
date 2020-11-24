package com.example.wechataddresslist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wechataddresslist.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tinypinyinAdapter(View view) {
        startActivity(new Intent(this, NativeAdapterActivity.class)
                .putExtra("py", Utils.PY_For_tinypinyin));
    }

    public void nativeAdapter(View view) {
        startActivity(new Intent(this, NativeAdapterActivity.class)
                .putExtra("py", Utils.PY_For_lib));
    }

    public void pinyin4jAdapter(View view) {
        startActivity(new Intent(this, NativeAdapterActivity.class)
                .putExtra("py", Utils.PY_For_pinyin4j));
    }

}