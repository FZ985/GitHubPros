package screenshots;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ScreenShotsManager {
    private Map<String, ShotsImage> caches;
    private boolean isRunning, isHomeKey;
    private ScreenShotsCall callback;
    private long exitTime = 0;

    public ScreenShotsManager() {
        caches = new HashMap<>();
    }

    public void destroyLoader(FragmentActivity activity) {
        caches.clear();
        caches = null;
        callback = null;
        isRunning = false;
        isHomeKey = false;
        FragmentActivity act = new WeakReference<>(activity).get();
        LoaderManager.getInstance(act).destroyLoader(act.getLocalClassName().hashCode());
    }

    private void initLoader(FragmentActivity activity, ScreenShotsCall callback) {
        FragmentActivity act = new WeakReference<>(activity).get();
        int id = act.getLocalClassName().hashCode();
        caches = new HashMap<>();
        PhotoLoaderCallbacks callbacks = new PhotoLoaderCallbacks(act, callback, caches);
        if (LoaderManager.getInstance(act).getLoader(id) == null) {
            LoaderManager.getInstance(act).initLoader(id, null, callbacks);
        }
    }

    public void startLoader(FragmentActivity activity, ScreenShotsCall callback) {
        isRunning = true;
        isHomeKey = false;
        this.callback = callback;
        initLoader(activity, this.callback);
    }

    public void onResume(FragmentActivity activity) {
        if (isRunning && isHomeKey) {
            isHomeKey = false;
            initLoader(activity, new ScreenShotsCall() {
                @Override
                public void onResult(ShotsImage image) {
                    long nowTime = System.currentTimeMillis();
                    if ((nowTime - exitTime) >= Util.duration) {
                        exitTime = 0;
                        //App外部的截屏后回到主App必须在duration 之后
                        if (ScreenShotsManager.this.callback != null) {
                            ScreenShotsManager.this.callback.onResult(image);
                        }
                    } else exitTime = 0;
                }
            });
        }
    }

    //用户切换到其他App或桌面
    public void onStop(FragmentActivity activity) {
        isHomeKey = true;
        exitTime = System.currentTimeMillis();
        FragmentActivity act = new WeakReference<>(activity).get();
        int id = act.getLocalClassName().hashCode();
        if (LoaderManager.getInstance(act).getLoader(id) != null) {
            LoaderManager.getInstance(act).destroyLoader(id);
        }
    }

    public interface ScreenShotsCall {
        void onResult(ShotsImage image);
    }
}
