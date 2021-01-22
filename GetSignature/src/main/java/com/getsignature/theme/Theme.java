package com.getsignature.theme;

import android.content.Context;
import android.content.SharedPreferences;

import com.getsignature.BaseApp;
import com.getsignature.BaseSharedPreferences;


public class Theme extends BaseSharedPreferences {
    private static Theme theme;
    final String mode = "theme_mode";

    private Theme() {

    }

    @Override
    public SharedPreferences getSharedPreference() {
        return BaseApp.getInstance().getSharedPreferences("theme_data", Context.MODE_PRIVATE);
    }

    public static Theme get() {
        if (theme == null) {
            theme = new Theme();
        }
        return theme;
    }

    public Theme putThemeMode(String value) {
        putString(mode, value);
        return this;
    }

    public String getThemeMode() {
        return getString(mode, ThemeHelper.DARK_MODE);
    }

}
