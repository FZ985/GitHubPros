package com.wzcuspro.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.ColorInt;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.wzcuspro.app.base.BaseApp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JFZ on 2017/4/7 10:49.
 */

public class Tools {

    public static final String TAG = "Tools";
    public static Toast toast;
    public static View view = null;

    /**
     * 获取手机安装应用的包名信息
     */
    public static boolean AppsInfo(Context mContext, String packname) {
        List<ApplicationInfo> list = mContext.getPackageManager()
                .getInstalledApplications(0);
        String pname = "";
        for (ApplicationInfo l : list) {
            pname = l.packageName;
            Log.e("packageName", pname);
            if (packname.equals(pname)) {
                return true;
            }
        }
        return false;
    }

    /***
     * 是否安装了应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }


    /***
     * 开启另一个应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int runApp(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        try {
            intent = packageManager.getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        } catch (Exception e) {
            showToast(context, "开启应用失败");
            return -1;
        }
        return 0;
    }

    /**
     * 获取控件
     *
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T getWidget(View view, int id) {
        SparseArray<View> hashMap = (SparseArray<View>) view.getTag();
        if (hashMap == null) {
            hashMap = new SparseArray<View>();
            view.setTag(hashMap);
        }
        View childView = hashMap.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            hashMap.put(id, childView);
        }
        return (T) childView;
    }

    /**
     * 获取手机联网的类型
     *
     * @param context
     */
    public static String getConnectionType(Context context) {
        boolean connection = isConnection(context);
        if (connection) {
            ConnectivityManager manager = getConnectivityManager(context);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            String typeName = networkInfo.getTypeName();
            Log.i("ConnectionVerdict", typeName);
            return typeName;
        } else {
            return null;
        }
    }

    /**
     * 判断WiFi开关是否打开
     *
     * @return
     */
    public boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo info = mgrConn.getActiveNetworkInfo();
        return ((info != null && info.getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }


    /**
     * 判断当前使用的网络是否WiFi
     *
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        NetworkInfo networkINfo = manager.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前链接的网络是否是手机流量网络
     *
     * @return
     */
    public static boolean isMobile(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        NetworkInfo networkINfo = manager.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 获取手机是否链接网络
     *
     * @param context
     * @return
     */
    public static boolean isConnection(Context context) {
        ConnectivityManager manager = getConnectivityManager(context);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable;
        if (networkInfo != null) {
            isAvailable = networkInfo.isAvailable();
        } else {
            isAvailable = false;
        }
        Log.i("ConnectionVerdict", isAvailable + "");
        return isAvailable;
    }

    /**
     * 获取联网的Manager
     *
     * @param context
     * @return
     */
    private static ConnectivityManager getConnectivityManager(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return null;
        }
        return mConnectivityManager;
    }

    /**
     * @return 当前系统秒钟
     */
    public static String currentSecond() {
        long time = System.currentTimeMillis();
        String formatTime = "00";
        SimpleDateFormat format = new SimpleDateFormat("ss",
                Locale.getDefault());
        formatTime = format.format(time);
        return formatTime;
    }

    /**
     * @return 当前系统时间
     */
    public static String getCurrentTime() {
        long time = System.currentTimeMillis();
        String formatTime = "00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        formatTime = format.format(time);
        return formatTime;
    }

    /**
     * @return 当前系统时间
     */
    public static String getToday() {
        long time = System.currentTimeMillis();
        String formatTime = "00";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault());
        formatTime = format.format(time);
        return formatTime;
    }


    /**
     * @return 当前系统天——日子
     */
    public static String currentDay() {
        long time = System.currentTimeMillis();
        String formatTime = "00";
        SimpleDateFormat format = new SimpleDateFormat("dd",
                Locale.getDefault());
        formatTime = format.format(time);
        return formatTime;
    }

    /**
     * @return 当前系统天——日子
     */
    public static String currentMonthDay() {
        long time = System.currentTimeMillis();
        String formatTime = "00";
        SimpleDateFormat format = new SimpleDateFormat("MM-dd",
                Locale.getDefault());
        formatTime = format.format(time);
        return formatTime;
    }

    /**
     * 更新图库
     *
     * @param mContext
     * @param path
     */
    public static void updatePictrueLib(Context mContext, String path) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        // Uri uri = Uri.fromFile(new File("/sdcard/image.jpg"));
        Uri uri = Uri.fromFile(new File(path));
        intent.setData(uri);
        mContext.sendBroadcast(intent);
    }

    /**
     * 插入图片到图库_bitmap
     *
     * @param bitmap
     * @param context
     * @return
     */
    public static String insertIntoPictureLib(Bitmap bitmap, Context context) {
        try {
            String insertImage = "";
            insertImage = MediaStore.Images.Media.insertImage(
                    context.getContentResolver(), bitmap, "", "");
            updatePictrueLib(context, insertImage);
            return insertImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 屏幕dp输入目标值
     */
    public static float destinationValue(Context context, int goalValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                goalValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取指定dip值
     */
    public static int getDipValue(Context context, int des) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                des, metrics);
    }


    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, int newWidth, int newHeight) {
        // 获取这个图片的宽和高
        int width = 480;
        int height = 800;
        if (bgimage != null) {
            width = bgimage.getWidth();
            height = bgimage.getHeight();
        }
        // 执行该缩放方法的条件有，有一边小于屏幕的大小或者图片的高度大于长度
        double x = width * newHeight;
        double y = height * newWidth;

        if (x > y) {
            newHeight = (int) (y / (double) width);
        } else if (x < y) {
            newWidth = (int) (x / (double) height);
        }

        if (newWidth > width && newHeight > height) {
            newWidth = width;
            newHeight = height;
        }
        Matrix matrix = new Matrix();
        matrix.reset();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / (float) width;
        float scaleHeight = ((float) newHeight) / (float) height;
        matrix.postScale(scaleWidth, scaleHeight);
        bgimage = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height,
                matrix, true);
        return bgimage;
    }

    /**
     * 将图片路径转换bitmap
     *
     * @param srcPath
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getImage2Bmp(String srcPath, int newWidth,
                                      int newHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = false;
        // 此时返回bm为空
        Bitmap bitmap = Tools.getLoacalBitmap(srcPath);
        Log.e("============", (bitmap == null) + "");
        bitmap = Tools.zoomImage(bitmap, newWidth, newHeight);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }


    public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        //压缩好比例大小后再进行质量压缩
//      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
        return bitmap;
    }


    /***
     * 加载本地图片
     * 本地图片路径
     * @param file
     * @return Bitmap 本地图片不存在时返回null
     */
    @SuppressWarnings("deprecation")
    public static Bitmap getLoacalBitmap(String file) {
        String buff = file.replace("file://", "");
        // 进一步判断文件是否存在
        File check = new File(buff);

        Log.e("buff", buff);
        // 本地图片路径不存在，返回null
        Log.e("check.exists()", check.exists() + "");
        if (!check.exists()) {
            Log.e("1", "1");
            return null;
        }
        // 读取图片
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = false;
            // 表示16位位图,565代表对应三原色占的位数
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            newOpts.inInputShareable = true;
            newOpts.inPurgeable = true;// 设置图片可以被回收
            Log.e("====buff=======", buff + "");
            Log.e("====++++++======", BitmapFactory.decodeFile(buff, newOpts)
                    .toString());

            return BitmapFactory.decodeFile(buff, newOpts);
        } catch (Exception e) {
            e.printStackTrace();
            // 读取图片出错时返回null
            Log.e("1", "2");
            return null;
        }
    }

    /**
     * @description :获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getMetrics(context).widthPixels;
    }

    /**
     * @description :获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getMetrics(context).heightPixels;
    }

    /**
     * @description :获取屏幕密度
     */
    public static int getScreenDensity(Context context) {
        float density = getMetrics(context).density;
        return (int) density;
    }

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }


    /**
     * 查看手机CPU?
     *
     * @return
     */
    @SuppressWarnings({"deprecation", "resource"})
    public static boolean hasCompatibleCPU() {
        // If already checked return cached result
        String CPU_ABI = Build.CPU_ABI;
        String CPU_ABI2 = "none";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) { // CPU_ABI2
            // since
            // 2.2
            try {
                CPU_ABI2 = (String) Build.class.getDeclaredField(
                        "CPU_ABI2").get(null);
            } catch (Exception e) {
                return false;
            }
        }

        Log.e("", "+++++++CPU_ABI:" + CPU_ABI);
        Log.e("", "+++++++CPU_ABI2:" + CPU_ABI2);
        if (CPU_ABI.equals("armeabi-v7a") || CPU_ABI2.equals("armeabi-v7a")) {
            return true;
        }

        try {
            FileReader fileReader = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                Log.e("while", "while_line:" + line);
                if (line.contains("ARMv7")) {
                    return true;
                }

            }
            fileReader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断是否为中文
     *
     * @return
     */
    public static boolean isContainChinese(String string) {
        boolean flag = false;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if ((c >= 0x4e00) && (c <= 0x9FA5)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取手机所有应用名
     *
     * @param context
     * @return
     */
    public static ArrayList<String> getAppName(Context context) {
        ArrayList<String> appList = new ArrayList<String>();

        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(0);

        for (int i = 0; i < packages.size(); i++) {

            PackageInfo packageInfo = packages.get(i);

            String appname = packageInfo.applicationInfo.loadLabel(
                    context.getPackageManager()).toString();
            appList.add(appname);
//            Logger.e("packageInfo_appname:"+appname);
//            Logger.e("packageInfo:"+packageInfo.packageName);
//            Logger.e("packageInfo:------------------------------------------------");
        }
        return appList;
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(View v, Activity context) {
        try {
            if (context != null) {
                InputMethodManager inputManager = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager.isActive()) {
                    inputManager.hideSoftInputFromWindow(context
                            .getCurrentFocus().getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开或关闭软键盘
     */
    public static void openOrClosedSoftKeyboard(Context mContext) {
        InputMethodManager inputManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // inputManager.showSoftInput(v, 0); //
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打开软键盘
     */
    public static void openSoftKeyboard(View v) {
        InputMethodManager inputManager = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(v, 0);
    }

    /**
     * 正则表达式 字符串包含 英文、中文、下划线
     *
     * @param nickname
     * @return
     */
    public static boolean checkNickName(String nickname) {
        // ^[A-Za-z0-9_\u4e00-\u9fa5]+$ 英文、数字、下划线、中文
        // ^[A-Za-z_\u4e00-\u9fa5]+$ 英文、下划线、中文
        Pattern p = Pattern.compile("^[A-Za-z_\u4e00-\u9fa5]+$");
        Matcher m = p.matcher(nickname);
        Log.e("checkNickName", m.matches() + "---");
        return m.matches();
    }

    /**
     * 将文本复制到剪贴板
     *
     * @param content 复制内容
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static void copyString(String content, Context context) {
        // 得到剪贴板管理器
        try {
            if (Build.VERSION.SDK_INT > 11) {
                ClipboardManager c = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                c.setPrimaryClip(ClipData.newPlainText("data", content));
            } else {
                android.text.ClipboardManager c = (android.text.ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                c.setText(content);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 把一个view转化成bitmap对象
     */
    public static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("Folder", "failed getViewBitmap(" + v + ")",
                    new RuntimeException());
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    /**
     * 取屏幕高度包含状态栏高度
     *
     * @return
     */
    public static int getScreenHeightWithStatusBar(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 取导航栏高度
     *
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 非空判断
     */
    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }


    /**
     * 关闭输入法
     *
     * @param act
     */
    public static void closeInputMethod(Activity act) {
        View view = act.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 经纬度测距
     *
     * @param jingdu1
     * @param weidu1
     * @param jingdu2
     * @param weidu2
     * @return
     */
    public static double distance(double jingdu1, double weidu1, double jingdu2, double weidu2) {
        double a, b, R;
        R = 6378137; // 地球半径
        weidu1 = weidu1 * Math.PI / 180.0;
        weidu2 = weidu2 * Math.PI / 180.0;
        a = weidu1 - weidu2;
        b = (jingdu1 - jingdu2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2
                * R
                * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(weidu1)
                * Math.cos(weidu2) * sb2 * sb2));
        return d;
    }

    public static String MD5(byte[] data) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(data);
        byte[] m = md5.digest();//加密
        return Base64.encodeToString(m, Base64.DEFAULT);
    }

    public static long[] getTimes(String cmt) {
        long[] longs = new long[4];
        try {
            // 01:58:20
            if (cmt.length() >= 7) {
                String[] split = cmt.split(":");
                String hourTxt = split[0];
                int hour = Integer.parseInt(hourTxt);
                longs[0] = 0;
                longs[1] = hour;
                String minuteTxt = split[1];
                int min = Integer.parseInt(minuteTxt);
                longs[2] = min;
                int secondTxt = Integer.parseInt(split[2]);
                longs[3] = secondTxt;
            }
        } catch (NumberFormatException e) {
        }
        return longs;
    }

    /**
     * 过去几天
     *
     * @param dayNums
     * @return
     */
    public static String beforeDay(int dayNums) {

        long nowTime = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        long oldTime = (long) (dayNums * 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String newTime = "0000-00-00";
        if (oldTime < nowTime) {
            Date d1 = new Date((nowTime - oldTime));
            newTime = format.format(d1);
        } else {
            beforeDay(1);
        }
//        Logger.e("过去"+dayNums+"天的日期是："+newTime);
        return newTime;
    }

    public static String beforeDayNoY(int dayNums) {

        long nowTime = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        long oldTime = (long) (dayNums * 24 * 60 * 60 * 1000);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String newTime = "00-00";
        if (oldTime < nowTime) {
            Date d1 = new Date((nowTime - oldTime));
            newTime = format.format(d1);
        } else {
            beforeDay(1);
        }
//        Logger.e("过去"+dayNums+"天的日期是："+newTime);
        return newTime;
    }

    /**
     * 现在的时间
     */
    public static String nowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowtime = format.format(new Date());
//        Logger.e("time:" + nowtime);
        return nowtime;
    }

    public static int random(int min, int max) {
        Random random = new Random();
        int num = random.nextInt(max) % (max - min + 1) + min;
        return num;
    }

    public static int randomColor() {
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param cls      是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, Class cls) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        String serviceName = cls.getCanonicalName();
        Log.e("Service Path", "Service Path :" + serviceName);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    public static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }


    public static String getClipboardContent(Context context) {
        //获取手机剪切板的内容
        String comment = "";
        try {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipboardManager.getPrimaryClip();
            if (clipboardManager.hasPrimaryClip()) {
                if (clipData.getItemCount() > 0) {
                    if (clipData.getItemAt(0).getText() != null) {
                        comment = clipData.getItemAt(0).getText().toString();
                    } else {
                        comment = "";
                    }
                } else {
                    comment = "";
                }
            } else {
                comment = "";
            }
        } catch (Exception e) {
            comment = "";
        }
        return comment;
    }

    public static boolean isServiceWork(Context mContext, String serviceName) {
        try {
            boolean isWork = false;
            ActivityManager myAM = (ActivityManager) mContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
            if (myList == null) {
                return false;
            }
            if (myList.size() <= 0) {
                return false;
            }
            for (int i = 0; i < myList.size(); i++) {
                String mName = myList.get(i).service.getClassName().toString();
                if (mName.equals(serviceName)) {
                    isWork = true;
                    break;
                }
            }
            return isWork;
        } catch (SecurityException e) {
            return false;
        }
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    //没有网络连接
    public static final String NETWORN_NONE = "NONE";
    //wifi连接
    public static final String NETWORN_WIFI = "WIFI";
    //手机网络数据连接类型
    public static final String NETWORN_2G = "2G";
    public static final String NETWORN_3G = "3G";
    public static final String NETWORN_4G = "4G";
    public static final String NETWORN_MOBILE = "MOBILE";

    /**
     * 获取当前网络连接类型
     *
     * @param context ctx
     * @return type
     */
    public static String getNetworkState(Context context) {
        //获取系统的网络服务
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //如果当前没有网络
        if (null == connManager)
            return NETWORN_NONE;

        //获取当前网络类型，如果为空，返回无网络
        NetworkInfo activeNetInfo = connManager.getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isAvailable()) {
            return NETWORN_NONE;
        }

        // 判断是不是连接的是不是wifi
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (null != wifiInfo) {
            NetworkInfo.State state = wifiInfo.getState();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    return NETWORN_WIFI;
                }
        }

        // 如果不是wifi，则判断当前连接的是运营商的哪种网络2g、3g、4g等
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (null != networkInfo) {
            NetworkInfo.State state = networkInfo.getState();
            String strSubTypeName = networkInfo.getSubtypeName();
            if (null != state)
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
                    switch (activeNetInfo.getSubtype()) {
                        //如果是2g类型
                        case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            return NETWORN_2G;
                        //如果是3g类型
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            return NETWORN_3G;
                        //如果是4g类型
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            return NETWORN_4G;
                        default:
                            //中国移动 联通 电信 三种3G制式
                            if (strSubTypeName.equalsIgnoreCase("TD-SCDMA") || strSubTypeName.equalsIgnoreCase("WCDMA") || strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                return NETWORN_3G;
                            } else {
                                return NETWORN_MOBILE;
                            }
                    }
                }
        }
        return NETWORN_NONE;
    }

    public static void showToast(String text) {
        showToast(BaseApp.getInstance(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String text, int duration) {

        try {
            if (toast != null) {
                toast.cancel();
                toast = null;
                view = null;
            }
            toast = new Toast(context);
            if (view == null) {
                view = Toast.makeText(context, "", Toast.LENGTH_SHORT).getView();
            }
            toast.setView(view);
            toast.setText(text);
            toast.setDuration(duration);
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, dip2px(context, 50));
            toast.show();
        } catch (Exception e) {
            Logger.e("ToastUtil", e.getMessage());
        }
    }

    public static String getUrlParams(String url, String paramsKey) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String result = "";
        if (url.contains("?")) {
            int index = url.indexOf("?");
            String temp = url.substring(index + 1);
            String[] keyValue = temp.split("&");
            for (String str : keyValue) {
                if (str.contains(paramsKey)) {
                    result = str.replace(paramsKey + "=", "");
                    break;
                }
            }
        }
        return result;

    }

    /**
     * 允许安装未知来源的应用
     */
    public static void allowUnKnowSrc(Context context) {
        try {
            android.provider.Settings.Global.putInt(context.getContentResolver(),
                    android.provider.Settings.Secure.INSTALL_NON_MARKET_APPS, 1);
        } catch (SecurityException e) {
            //LogUtils.getInstance().d(e);
        }
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        Log.i(TAG, w + "---------------" + h);
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
//        float hh = 800f;// 这里设置高度为800f
//        float ww = 480f;// 这里设置宽度为480f
        float hh = 512f;
        float ww = 512f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) { // 如果高度高的话根据高度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩

        //return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 90, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 加入QQ群
     *
     * @param activity
     * @param qqKey
     * @return
     */
    public static boolean joinQQClub(Activity activity, String qqKey) {
        //唤起QQ
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + qqKey));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            showToast("未安装手Q或安装的版本不支持");
            return false;
        }
    }
}
