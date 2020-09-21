package com.progressview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.progressview.view.LineProgressView;

public class MainActivity extends AppCompatActivity {
    private LineProgressView lineProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineProgress = findViewById(R.id.lineProgress);
        lineProgress.setMaxProgress(50);
    }

    public void lineProgress(View view) {
        lineProgress.postDelayed(lineRun, 1000);
    }


    private Runnable lineRun = new Runnable() {
        @Override
        public void run() {
            lineProgress.setProgress(lineProgress.getProgress() + 1);
            lineProgress(lineProgress);
//            if (lineProgress.getProgress() == lineProgress.getMaxProgress()) {
//                lineProgress.setProgress(0);
//            }
        }
    };
}
