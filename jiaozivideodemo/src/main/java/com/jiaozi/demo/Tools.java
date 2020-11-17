package com.jiaozi.demo;

import android.view.View;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by JFZ on 2017/4/7 10:49.
 */

public class Tools {

    public static final String TAG = "Tools";
    public static Toast toast;
    public static View view = null;
    public static int random(int min, int max) {
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        return num;
    }

    public static int randomColor() {
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }
}
