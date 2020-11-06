package screenshots;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_TAKEN;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;

public class PhotoLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;
    private ScreenShotsManager.ScreenShotsCall resultCallback;
    private Map<String, ShotsImage> caches;

    public PhotoLoaderCallbacks(Context context, ScreenShotsManager.ScreenShotsCall resultCallback, Map<String, ShotsImage> caches) {
        this.context = context;
        this.resultCallback = resultCallback;
        this.caches = caches;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new PhotoDirectoryLoader(context, false);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        List<ShotsImage> list = new ArrayList<>();
        while (data.moveToNext()) {
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            String display_name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            long date = data.getLong(data.getColumnIndexOrThrow(DATE_TAKEN));
            long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

            if (size < 1) {
                continue;
            }
            Logs.log("display_name:" + display_name);
            if (Util.hasKeyWords(path) && Util.checkDate(date)) {
                list.add(new ShotsImage(display_name, path, size, date));
            }
        }
        if (resultCallback != null && list.size() > 0) {
            ShotsImage image = list.get(0);
            if (!caches.containsKey(image.getPath())) {
                caches.put(image.getPath(), image);
                resultCallback.onResult(image);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}