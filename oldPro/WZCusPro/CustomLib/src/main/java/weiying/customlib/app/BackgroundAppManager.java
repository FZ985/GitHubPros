package weiying.customlib.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class BackgroundAppManager implements Application.ActivityLifecycleCallbacks {

    public static final long CHECK_DELAY = 500;
    public static final String TAG = "BackgroundAppManager";

    public interface Listener {

        public void onBecameForeground(Activity activity);

        public void onBecameBackground(Activity activity);

    }

    private static BackgroundAppManager instance;

    private boolean foreground = false, paused = true;
    private Handler handler = new Handler();
    private List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private Runnable check;


    public static BackgroundAppManager init(Application application) {
        if (instance == null) {
            instance = new BackgroundAppManager();
            application.registerActivityLifecycleCallbacks(instance);
        }
        return instance;
    }

    public static BackgroundAppManager get(Application application) {
        if (instance == null) {
            init(application);
        }
        return instance;
    }

    public static BackgroundAppManager get(Context ctx) {
        if (instance == null) {
            Context appCtx = ctx.getApplicationContext();
            if (appCtx instanceof Application) {
                init((Application) appCtx);
            }
            throw new IllegalStateException(
                    "Foreground is not initialised and " +
                            "cannot obtain the Application object");
        }
        return instance;
    }

    public static BackgroundAppManager get() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Foreground is not initialised - invoke " +
                            "at least once with parameterised init/get");
        }
        return instance;
    }

    public boolean isForeground() {
        return foreground;
    }

    public boolean isBackground() {
        return !foreground;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onActivityResumed(final Activity activity) {
        paused = false;
        boolean wasBackground = !foreground;
        foreground = true;

        if (check != null)
            handler.removeCallbacks(check);

        if (wasBackground) {
            Log.e(TAG, "went foreground");
            for (Listener l : listeners) {
                try {
                    l.onBecameForeground(activity);
                } catch (Exception exc) {
                    Log.e("TAG", "Listener threw exception!", exc);
                }
            }
        } else {
            Log.e("TAG", "still foreground");
        }
    }

    @Override
    public void onActivityPaused(final Activity activity) {

        paused = true;

        if (check != null)
            handler.removeCallbacks(check);

        handler.postDelayed(check = new Runnable() {
            @Override
            public void run() {
                if (foreground && paused) {
                    foreground = false;
                    Log.e(TAG, "went background");
                    for (Listener l : listeners) {
                        try {

                            l.onBecameBackground(activity);
                        } catch (Exception exc) {
                            Log.e("TAG", "Listener threw exception!", exc);
                        }
                    }
                } else {
                    Log.e("TAG", "still foreground");
                }
            }
        }, CHECK_DELAY);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.e(TAG, "--BackgroundAppManager---------------onActivityDestroyed----------------" + (activity == null ? "null" : activity.getClass().getSimpleName()));
    }


}
