package com.getsignature.theme;

import android.app.Activity;
import android.app.ProgressDialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

import com.getsignature.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-21 10:48
 */
public class SystemThemeDialog {

    public static AlertDialog.Builder AlertBuilder(Activity activity) {
//        int mode = AppCompatDelegate.getDefaultNightMode();
//        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
//            return new AlertDialog.Builder(activity, R.style.Theme_AppCompat_Light_Dialog_Alert);
//        }
        return new MaterialAlertDialogBuilder(activity, R.style.ThemeDialog);
    }


    public static ProgressDialog getProgressDialog(Activity activity) {
        int mode = AppCompatDelegate.getDefaultNightMode();
        if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
            return new ProgressDialog(activity, android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        }
        return new ProgressDialog(activity);
    }
}