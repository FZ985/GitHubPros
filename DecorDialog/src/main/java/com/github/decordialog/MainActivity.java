package com.github.decordialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.decordialog.dialog.DecorDialog;
import com.github.decordialog.tst.TestDialog1;

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
