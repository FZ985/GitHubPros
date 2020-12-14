package okhttp.file.download;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

import okhttp.utils.OkUtil;

/**
 * Created by JFZ .
 * on 2018/1/17.
 */

public class DownLoadFileUtil {

    public static String defualtDownloadDir(Context context) {
        String path = obtainPhoneMemoryPath(context) + File.separator
                + getApplicationName(context) + File.separator + "cache" + File.separator + "download" + File.separator;
        createNewDir(path);
        return path;
    }

    public static String getApplicationName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (Exception e) {
            applicationInfo = null;
            return "";
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    /**
     * 获取手机可用的存储路径
     *
     */
    public static String obtainPhoneMemoryPath(Context c) {
        String sdStatus = Environment.getExternalStorageState();
        boolean sdCardExist = sdStatus
                .equals(Environment.MEDIA_MOUNTED);

        if (TextUtils.isEmpty(sdStatus)) {
            return c.getFilesDir().getAbsolutePath();
        }

        if (!sdCardExist) {
            return c.getFilesDir().getAbsolutePath();
        }
        try {
            long sdcardSpace = 0;
            try {
                sdcardSpace = getSDcardAvailableSpace();
            } catch (Exception e) {
                OkUtil.log("error1:" + e.getMessage());
            }
            if (sdcardSpace >= 5) {
                return getSDCardPath(c);
            }
            long phoneSpace = getDataStorageAvailableSpace();
            if (phoneSpace >= 5) {
                return c.getFilesDir().getAbsolutePath();
            }
            OkUtil.d(String.format("get storage space, phone: %d, sdcard: %d",
                    (int) (phoneSpace / 1024 / 1024),
                    (int) (sdcardSpace / 1024 / 1024)));
        } catch (Exception e) {
            OkUtil.d("error3:" + e.getMessage());
        }

        return c.getFilesDir().getAbsolutePath();
    }

    public static void createNewDir(String dir) {
        if (!checkSDCard()) {
            return;
        }
        if (null == dir) {
            return;
        }
        File f = new File(dir);
        if (!f.exists()) {
            String[] pathSeg = dir.split(File.separator);
            String path = "";
            for (String temp : pathSeg) {
                if (TextUtils.isEmpty(temp)) {
                    path += File.separator;
                    continue;
                } else {
                    path += temp + File.separator;
                }
                File tempPath = new File(path);
                if (tempPath.exists() && !tempPath.isDirectory()) {
                    tempPath.delete();
                }
                tempPath.mkdirs();
            }
        } else {
            if (!f.isDirectory()) {
                f.delete();
                f.mkdirs();
            }
        }
    }

    /**
     * 判断SD卡是否存在
     *
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取手机内部可用空间大小
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getDataStorageAvailableSpace() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内置SD卡路径 或者文件储存路径
     */
    public static String getSDCardPath(Context c) {
        File sdDir = null;
        String sdStatus = Environment.getExternalStorageState();
        boolean sdCardExist = sdStatus.equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.getPath();
        }
        return c.getFilesDir().getPath();
    }

    /**
     * 获取手机内置SD卡可用空间大小
     *
     */
    @SuppressWarnings("deprecation")
    public static long getSDcardAvailableSpace() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File path = Environment.getExternalStorageDirectory();
            if (path == null) {
                return 0;
            }
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize; // "Byte"
        } else {
            return 0;
        }
    }
}
