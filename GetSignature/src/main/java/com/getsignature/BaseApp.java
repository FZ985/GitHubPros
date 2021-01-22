package com.getsignature;

import android.app.Application;

import com.getsignature.theme.Theme;
import com.getsignature.theme.ThemeHelper;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-21 18:24
 */
public class BaseApp extends Application {
    private static BaseApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        ThemeHelper.applyTheme(Theme.get().getThemeMode());
    }

    public static BaseApp getInstance() {
        return app;
    }
}