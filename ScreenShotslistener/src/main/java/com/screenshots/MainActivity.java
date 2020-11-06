package com.screenshots;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.screenshots.helper.Permission;

import screenshots.Logs;
import screenshots.ScreenShotsManager;
import screenshots.ShotsImage;

public class MainActivity extends AppCompatActivity {
    private ScreenShotsManager manager;
    private ImageView shortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shortcut = findViewById(R.id.shortcut);
    }

    private int count;

    public void start(View view) {
        count = 0;
        if (Permission.hasReadPermission(this)) {
            if (manager == null) {
                manager = new ScreenShotsManager();
            }
            manager.startLoader(this, new ScreenShotsManager.ScreenShotsCall() {
                @Override
                public void onResult(ShotsImage image) {
                    count++;
                    Logs.log("count:" + count);
                    Glide.with(MainActivity.this)
                            .load(image.getPath())
                            .into(shortcut);
                }
            });
        } else Permission.reqReadPermission(this);
    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (manager != null) {
//            manager.onResume(this);
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (manager != null) {
//            manager.onStop(this);
//        }
//        Logs.log("onStop");
//    }

    @Override
    protected void onDestroy() {
        if (manager != null) {
            manager.destroyLoader(this);
        }
        super.onDestroy();
    }
}
