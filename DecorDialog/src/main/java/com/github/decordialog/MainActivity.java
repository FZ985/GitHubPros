package com.github.decordialog;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.decordialog.dialog.DecorDialog;
import com.github.decordialog.tst.TestDialog1;

import java.io.File;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RadioButton rb1, rb2, rb3;
    RadioButton backrb1, backrb2;
    RadioButton touchrb1, touchrb2;
    RadioButton rooteventrb1, rooteventrb2;
    SeekBar alphasb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        backrb1 = findViewById(R.id.backrb1);
        backrb2 = findViewById(R.id.backrb2);
        touchrb1 = findViewById(R.id.touchrb1);
        touchrb2 = findViewById(R.id.touchrb2);
        rooteventrb1 = findViewById(R.id.rooteventrb1);
        rooteventrb2 = findViewById(R.id.rooteventrb2);
        alphasb = findViewById(R.id.alphasb);
        alphasb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (dialog != null) {
                    dialog.dimAmount(((float) progress) / 10.0f);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isExternalStorageManager(@NonNull File path) {
        Context context = this;
        String packageName = Objects.requireNonNull(context.getPackageName());
        int uid = context.getApplicationInfo().uid;

        final AppOpsManager appOps = context.getSystemService(AppOpsManager.class);
        final int opMode = appOps.checkOpNoThrow(AppOpsManager.OP_MANAGE_EXTERNAL_STORAGE, uid, packageName);

        switch (opMode) {
            case AppOpsManager.MODE_DEFAULT:
                return PackageManager.PERMISSION_GRANTED == context.checkPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE, Process.myPid(), uid);
            case AppOpsManager.MODE_ALLOWED:
                return true;
            case AppOpsManager.MODE_ERRORED:
            case AppOpsManager.MODE_IGNORED:
                return false;
            default:
                throw new IllegalStateException("Unknown AppOpsManager mode " + opMode);
        }
    }


    TestDialog1 dialog;

    public void open(View view) {
        if (dialog == null) {
            dialog = new TestDialog1(this);
        }
        if (rb1.isChecked()) {
            dialog.setGravity(Gravity.TOP);
        }
        if (rb2.isChecked()) {
            dialog.setGravity(Gravity.CENTER);
        }
        if (rb3.isChecked()) {
            dialog.setGravity(Gravity.BOTTOM);
        }
        if (touchrb1.isChecked()) {
            dialog.setCanceledOnTouchOutside(false);
        }
        if (touchrb2.isChecked()) {
            dialog.setCanceledOnTouchOutside(true);
        }
        if (backrb1.isChecked()) {
            dialog.setCancelable(false);
        }
        if (backrb2.isChecked()) {
            dialog.setCancelable(true);
        }
        if (rooteventrb1.isChecked()) {
            dialog.setRootTouchEvent(true);
        }
        if (rooteventrb2.isChecked()) {
            dialog.setRootTouchEvent(false);
        }
        int progress = alphasb.getProgress();
        dialog.dimAmount(((float) progress) / 10.0f);

        dialog.setOnDismissListener(new DecorDialog.OnDismissListener() {
            @Override
            public void onDismiss(DecorDialog dialog) {
                Toast.makeText(MainActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
                MainActivity.this.dialog = null;
            }
        });
        dialog.show();

    }
}
