package com.whatsapp.share.natives;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

public class WhatsNative {
    private static final String whatsPakcage = "com.whatsapp";
    private static final String whatsShareActivity = "com.whatsapp.ContactPicker";
    //    android.intent.action.SEND_MULTIPLE
    //    android.intent.action.SEND

    /**
     * 分享文本
     *
     * @param activity
     * @param text
     */
    public static boolean shareText(Activity activity, String text) {
        WhatsShareContent content = new WhatsShareContent();
        content.setText(text);
        return shareTo(activity, content);
    }

    /**
     * 分享网络图片
     * message
     *
     * @param activity
     * @param imageUrl
     */
    public static boolean shareUrlImage(Activity activity, String imageUrl) {
        WhatsShareContent content = new WhatsShareContent(imageUrl);
        content.setImage(imageUrl);
        return shareTo(activity, content);
    }

    public static boolean shareFileImage(Activity activity, File file) {
        WhatsShareContent content = new WhatsShareContent(file);
        content.setFile(file);
        return shareTo(activity, content);
    }

    private static boolean shareTo(final Activity context, final WhatsShareContent content) {
        if (content == null) return false;
        if (isInstalled(context)) {
            final Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.SUBJECT", "subject");
            if (content.isAsImage()) {
                intent.setType("image/*");
                File file = content.getFile();
                if (file != null) {
                    Uri imgUri = getUri(context, file);
                    intent.putExtra("android.intent.extra.STREAM", imgUri);
                    return startShare(context, intent);
                } else {
                    //下载网络图片
                    new DownImageTask(context.getApplicationContext(), new DownImageCall() {
                        @Override
                        public void downSuc(final File file) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (file != null) {
                                        Uri imgUri = getUri(context, file);
                                        intent.putExtra("android.intent.extra.STREAM", imgUri);
                                        startShare(context, intent);
                                    }
                                }
                            });
                        }

                        @Override
                        public void downError(final String errorMsg) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }, content.getImage()).executeOnExecutor(Executors.newCachedThreadPool());
                }
            } else {
                intent.setType("text/*");
                intent.putExtra("android.intent.extra.TEXT", content.getText());
                return startShare(context, intent);
            }
            return false;
        } else {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private static boolean startShare(Activity context, Intent intent) {
        boolean found = false;
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        if (resInfo == null) {
            return false;
        } else {
            System.out.println("dddd111111111");
            Iterator var15 = resInfo.iterator();
            while (var15.hasNext()) {
                ResolveInfo info = (ResolveInfo) var15.next();
                if (info.activityInfo.packageName.toLowerCase().contains("com.whatsapp") || info.activityInfo.name.toLowerCase().contains("com.whatsapp")) {
                    intent.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            System.out.println("dddd111111111" + found);
            if (!found) {
                return false;
            } else {
                try {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    System.out.println("ddddd_start");
                    //listener —— success
                } catch (Exception var13) {
                    //listener —— error
                    System.out.println("dddd:" + var13.getMessage());
                }
                return true;
            }
        }
    }

    private static Uri getUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            String uriImg = insertImageToSystem(context, file.getPath());
//            return Uri.parse(uriImg);
            return FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".fileprovider",
                    file);
//             给目标应用一个临时授权
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else
            return Uri.fromFile(file);
    }

    private static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "", "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static class DownImageTask extends AsyncTask<Void, Void, Bitmap> {
        private Context context;
        private DownImageCall call;
        private String imageUrl;

        public DownImageTask(Context context, DownImageCall call, String imageUrl) {
            this.context = context;
            this.call = call;
            this.imageUrl = imageUrl;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                URL fileUrl = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoInput(true);
                connection.setDoOutput(false);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    return BitmapFactory.decodeStream(is);
                } else return null;
            } catch (Exception e) {
                Log.e("share", "download exception:" + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                Log.e("share", "download success,start save.");
                File file = new File(context.getExternalFilesDir("share"), "whatsapp_img.jpg");
                if (file.exists()) {
                    Log.e("share", "The file exists , need delete.");
                    file.delete();
                }
                Log.e("share", "下载路径为:" + file.getPath());
                Bitmap outB = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(outB);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(bitmap, 0, 0, null);
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    if (outB.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                        out.flush();
                        out.close();
                    }
                    call.downSuc(file);
                } catch (Exception e) {
                    call.downError("save faild:" + e.getMessage());
                }
            } else call.downError("download faild bitmap == null");
        }
    }


    private static boolean isInstalled(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(whatsPakcage, 0);
            return (info != null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    interface DownImageCall {

        void downSuc(File file);

        void downError(String errorMsg);
    }
}
