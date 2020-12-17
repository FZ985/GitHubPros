package com.kmt.pro.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.kmt.pro.base.BaseApp;
import com.kmtlibs.app.utils.Logger;
import com.kmtlibs.app.utils.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.ColorInt;

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
        if (TextUtils.isEmpty(text)) return;
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
            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, Utils.dip2px(context, 50));
            toast.show();
        } catch (Exception e) {
            Logger.e("ToastUtil", e.getMessage());
        }
    }

    public static void showToastCenter(String text) {
        showToastCenter(text, Gravity.CENTER);
    }

    public static void showToastCenter(String text, int gravity) {
        showToastCenter(BaseApp.getInstance(), text, gravity);
    }

    public static void showToastCenter(Context context, String text, int gravity) {
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
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
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
    @SuppressLint("NewApi")
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

    public static void openNotification(Activity activity) {
        try {
            Intent localIntent = new Intent();
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);
                localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
            }
            activity.startActivity(localIntent);
        } catch (Exception e) {
            showToast("请手动开启通知");
        }
    }

    //手机号正则
    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|16[0123456789]|17[0123456789]|18[0-9]|14[0-9]|19[0-9])[0-9]{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

}
