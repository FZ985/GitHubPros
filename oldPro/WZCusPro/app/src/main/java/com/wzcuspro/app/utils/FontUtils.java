package com.wzcuspro.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontUtils {

    public static void setTypeface(Context context, TextView textView){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/DINMITTE.TTF"));
    }
}
