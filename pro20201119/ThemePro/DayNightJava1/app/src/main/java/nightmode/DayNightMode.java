package nightmode;

import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Created by Rikka on 2016/3/2.
 */
public class DayNightMode {
    public static final int MODE_NIGHT_AUTO = AppCompatDelegate.MODE_NIGHT_AUTO;
    public static final int MODE_NIGHT_NO = AppCompatDelegate.MODE_NIGHT_NO;
    public static final int MODE_NIGHT_YES = AppCompatDelegate.MODE_NIGHT_YES;
    public static final int MODE_NIGHT_FOLLOW_SYSTEM = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

    public static void setDefaultNightMode(Context context, int mode) {
        context.getApplicationContext()
                .getSharedPreferences("app_daynight", Context.MODE_PRIVATE)
                .edit()
                .putInt("mode", mode)
                .commit();
        AppCompatDelegate.setDefaultNightMode(
                mode);
    }

    public static int getDefaultNightMode(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences("app_daynight", Context.MODE_PRIVATE)
                .getInt("mode", DayNightMode.MODE_NIGHT_FOLLOW_SYSTEM);
    }


    public static void setSystemNightMode(Context context, int mode) {
        ((UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE))
                .setNightMode(mode);
    }
}
