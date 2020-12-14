package com.wzcuspro.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by jfz
 * on 2018/6/6.
 */

public class FileUtils {

    public static final String POSTFIX = ".jpeg";
    public static final String GIFFIX = ".gif";
    private static final String SHAREPIC = "sharepic";
    public static final String APP_NAME = "系统文件";
    public static final String APP_DONWLOAD_APKS = "download";
    public static final String WEB = "web";
    public static final String CACHE = "cache";

    public static final String CACHE_PATH = File.separator + APP_NAME
            + File.separator + CACHE + File.separator + SHAREPIC
            + File.separator;
    public static final String SAVE_IMG_PATH = File.separator + APP_NAME
            + File.separator + "save" + File.separator;
    public static final String PREVIEW_PATH = File.separator + APP_NAME
            + File.separator + "preview" + File.separator;

    public static final String APK_DOWNLOAD_PATH = File.separator + APP_NAME
            + File.separator + CACHE + File.separator + APP_DONWLOAD_APKS
            + File.separator;

    public static final String WEB_CACHE_PATH = File.separator + APP_NAME
            + File.separator + CACHE + File.separator + WEB
            + File.separator;

    public static File createFile(Context context, String path, boolean isGif) {
        return createMediaFile(context, path, isGif);
    }

    public static File createDownloadApkFile(Context context) {
        return createFile(context, APK_DOWNLOAD_PATH, false);
    }

    //创建缓存文件
    public static File createCacheFile(Context context) {
        return createMediaFile(context, CACHE_PATH, false);
    }

    //创建图片文件 (jpg,png,jpeg)
    public static File createImgFile(Context context) {
        return createMediaFile(context, SAVE_IMG_PATH, false);
    }

    //创建图片文件 (gif)
    public static File createGifFile(Context context) {
        return createMediaFile(context, SAVE_IMG_PATH, true);
    }

    //创建gif 预览文件
    public static File createPreviewGIFFile(Context context) {
        return createMediaFile(context, PREVIEW_PATH, true);
    }

    //创建非 gif预览文件
    public static File createPreviewFile(Context context) {
        return createMediaFile(context, PREVIEW_PATH, false);
    }

    private static File createMediaFile(Context context, String parentPath, boolean isgif) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment
                .getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()) {

        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
                .format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = new File(folderDir, fileName + (isgif ? GIFFIX : POSTFIX));
        return tmpFile;
    }

    public static File createPath(Context context, String path) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment
                .getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + path);
        if (!folderDir.exists() && folderDir.mkdirs()) {

        }
        Logger.e("folderDir:"+folderDir.getPath());
        return folderDir;
    }

    public static File saveFile(Context context, Bitmap bm) {
        Logger.log("saveBitmap", "保存图片");
        File f = createImgFile(context);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            // path = f.getPath();
            Logger.log("", "已经保存:" + f.getPath());
            // updatePicture(context, f, f.getPath());
            return f;
        } catch (FileNotFoundException e) {
            return f;
        } catch (IOException e) {
            return f;
        }
    }

    public static void savePreImg(Context context, byte[] bytes, boolean isgif) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = isgif ? createPreviewGIFFile(context) : createPreviewFile(context);
            String filePath = file.getPath();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filePath);
                fos.write(bytes);
                Toast.makeText(context, "图片已保存到" + filePath, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                }
            }
        } else {
            Toast.makeText(context, "请检查SD卡是否可用", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean fileIsExists(String path) {
        if (path == null || path.trim().length() <= 0) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        return deleteFile(new File(fileName));
    }

    public static boolean deleteFile(File file) {
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        String fileName = file.getPath();
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
}
