package com.intent2chooseimage.choose;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.Nullable;

public class FileChooser {

    public static final int FILE_CHOOSER_REQUEST_CODE = 0x1024;
    public static String acceptType = "*/*";
    private static FileChooser chooser;
    private ChooseCall call;

    private FileChooser() {
    }

    public static synchronized FileChooser getInstance() {
        if (chooser == null) {
            chooser = new FileChooser();
        }
        return chooser;
    }

    public FileChooser setCall(ChooseCall call) {
        this.call = call;
        return this;
    }

    private Intent getChooseIntent(String acceptType) {
        Intent i = new Intent();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            i.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        } else {
        i.setAction(Intent.ACTION_GET_CONTENT);
//        }
        i.addCategory(Intent.CATEGORY_OPENABLE);
        if (TextUtils.isEmpty(acceptType)) {
            i.setType(this.acceptType);
        } else {
            i.setType(acceptType);
        }
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return Intent.createChooser(i, "");
    }

    public void chooseImage(Activity activity) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            activity.startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            Intent wrapperIntent = Intent.createChooser(intent, null);
            activity.startActivityForResult(wrapperIntent, FILE_CHOOSER_REQUEST_CODE);
        }
    }

    public void chooseFile(Activity activity, String acceptType) {
        Intent intent = getChooseIntent(acceptType);
        activity.startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED || data == null) return;
            if (resultCode != Activity.RESULT_OK) return;
            //5.0以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (call != null) {
                    call.onCall(processData(data));
                }
            } else {
                //5.0以下
                Uri mUri = data.getData();
                if (call != null) {
                    call.onCall(new Uri[]{mUri});
                }
            }
        }
        call = null;
    }

    private Uri[] processData(Intent data) {
        Uri[] datas = null;
        if (data == null) {
            return datas;
        }
        String target = data.getDataString();
        if (!TextUtils.isEmpty(target)) {
            return datas = new Uri[]{Uri.parse(target)};
        }
        ClipData mClipData = data.getClipData();
        if (mClipData != null && mClipData.getItemCount() > 0) {
            datas = new Uri[mClipData.getItemCount()];
            for (int i = 0; i < mClipData.getItemCount(); i++) {
                ClipData.Item mItem = mClipData.getItemAt(i);
                datas[i] = mItem.getUri();
            }
        }
        return datas;
    }

    public interface ChooseCall {
        void onCall(Uri[] uris);
    }
}