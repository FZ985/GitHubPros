package com.github.decordialog.tst;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.decordialog.R;
import com.github.decordialog.dialog.DecorDialog;

/**
 * Create by JFZ
 * date: 2020-08-19 11:37
 **/
public class TestDialog1 extends DecorDialog {
    private TextView test1;

    public TestDialog1(Activity activity) {
        super(activity);
        setContentView(R.layout.dialog_decor1);
    }

    @Override
    protected void initView() {
        test1 = findViewById(R.id.test1);
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "tv click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
