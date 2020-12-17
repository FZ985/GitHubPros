package com.example.app2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import aaeess.AAEESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void hello(View v) {
        ((TextView) v).setText(AAEESS.getInstance().pass() + "\n" + AAEESS.getInstance().passIv());
    }
}