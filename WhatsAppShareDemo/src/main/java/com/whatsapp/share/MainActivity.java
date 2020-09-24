package com.whatsapp.share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.whatsapp.share.natives.NativeAvtivity;
import com.whatsapp.share.umeng.UmengActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toUmeng(View view) {
        startActivity(new Intent(this, UmengActivity.class));
    }

    public void toNative(View view) {
        startActivity(new Intent(this, NativeAvtivity.class));
    }
}
