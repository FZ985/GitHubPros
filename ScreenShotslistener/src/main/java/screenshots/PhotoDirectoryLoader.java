package screenshots;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

/**
 * Created by JFZ on 2017/6/15 11:04.
 */

public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION = {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.DATE_TAKEN};

    public PhotoDirectoryLoader(Context context, boolean showGif) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        setSortOrder(MediaStore.Images.Media.DATE_ADDED + " DESC");

        setSelection(MediaStore.MediaColumns.MIME_TYPE
                + "=? or " + MediaStore.MediaColumns.MIME_TYPE
                + "=? or " + MediaStore.MediaColumns.MIME_TYPE
//                + "=? or " + MediaStore.Images.Media.DATE_TAKEN
//                + "=? or " + MediaStore.Images.Media.DISPLAY_NAME
                + "=? " + (showGif ? ("or " + MediaStore.MediaColumns.MIME_TYPE + "=?") : ""));

        String[] selectionArgs;
        if (showGif) {
            selectionArgs = new String[]{"image/jpeg", "image/png",
                    "image/jpg", "image/gif"};
        } else {
            selectionArgs = new String[]{"image/jpeg", "image/png",
                    "image/jpg"};
        }
        setSelectionArgs(selectionArgs);
    }

    private PhotoDirectoryLoader(Context context, Uri uri, String[] projection,
                                 String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
