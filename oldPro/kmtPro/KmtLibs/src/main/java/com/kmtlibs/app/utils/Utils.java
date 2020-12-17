package com.kmtlibs.app.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Create by JFZ
 * date: 2020-04-30 16:28
 **/
public class Utils {

    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics;
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
        }
        return appList;
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
            return -1;
        }
        return 0;
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
        if (manager != null) {
            NetworkInfo networkINfo = manager.getActiveNetworkInfo();
            if (networkINfo != null
                    && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
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

    public static int getDipValue(Context context, int des) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                des, metrics);
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

    /**
     * 加入QQ群
     * http://qun.qq.com/join.html 要去此网站设置一下的
     * mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D
     * https://jq.qq.com/?_wv=1027&k=5Bs52dM&
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
            return false;
        }
    }

    public static boolean joinQQClubUrl(Activity activity, String qqUrl) {
//        mqqopensdkapi://
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            if (isAppInstalled(activity, "com.tencent.mobileqq")) {
                Intent intent = new Intent();
                intent.setData(Uri.parse(qqUrl));
                activity.startActivity(intent);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean jumpQQFriend(Activity activity, String qqNumber) {
        try {
            if (isAppInstalled(activity, "com.tencent.mobileqq")) {
                String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=" + qqNumber + "&version=1";
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean jumpQQFriendUrl(Activity activity, String qqNumberUrl) {
        try {
            if (isAppInstalled(activity, "com.tencent.mobileqq")) {
                String qqUrl = qqNumberUrl;
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 跳转系统浏览器
     *
     * @param activity
     * @param url
     * @return
     */
    public static boolean jumpSystem(Activity activity, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
            return true;
        } catch (Exception e) {
            Logger.e("e:" + e.getMessage());
            return false;
        }
    }

    /**
     * 判断设备 是否使用代理上网
     */
    public static boolean isWifiProxy(Context context) {
        boolean isWifi = isWifi(context);
        Logger.e("网络代理_是否wifi:" + isWifi);
        if (isWifi) {
            final boolean IS_ICS_OR_LATER = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

            String proxyAddress;

            int proxyPort;

            if (IS_ICS_OR_LATER) {

                proxyAddress = System.getProperty("http.proxyHost");

                String portStr = System.getProperty("http.proxyPort");

                proxyPort = Integer.parseInt((portStr != null ? portStr : "-1"));

            } else {

                proxyAddress = android.net.Proxy.getHost(context);

                proxyPort = android.net.Proxy.getPort(context);

            }
            Logger.e("网络代理_proxyAddress:" + proxyAddress + ",proxyPort:" + proxyPort + "," + ((!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1)));
            return (!TextUtils.isEmpty(proxyAddress)) && (proxyPort != -1);
        }
        return false;
    }

    public static String getMobileNumber(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String te1 = tm.getLine1Number();//获取本机号码
            return te1;
        } catch (Exception e) {
            return "null";
        }
    }


    /**
     * 获取App签名信息
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppSign(Context context, String packageName) {
        String sign = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            if (info != null && info.signatures != null && info.signatures.length > 0) {
                //获取原始签名MD5
                sign = getSignValidString(info.signatures[0].toByteArray());
                Logger.e("app签名:" + sign);
            }
        } catch (Exception e) {
            Logger.e("app签名:" + e.getMessage());
        }

        return sign;
    }

    private static String getSignValidString(byte[] paramArrayOfByte) throws NoSuchAlgorithmException {
        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramArrayOfByte);
        return toHexString(localMessageDigest.digest());
    }

    public static String toHexString(byte[] paramArrayOfByte) {
        if (paramArrayOfByte == null) {
            return null;
        }
        StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
        for (int i = 0; ; i++) {
            if (i >= paramArrayOfByte.length) {
                return localStringBuilder.toString();
            }
            String str = Integer.toString(0xFF & paramArrayOfByte[i], 16);
            if (str.length() == 1) {
                str = "0" + str;
            }
            localStringBuilder.append(str);
        }
    }

    public static boolean isInstallApp(Context context, String pkgName) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = pkgName;
        boolean hasInstallWx;
        try {
//            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_GIDS);
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            hasInstallWx = packageInfo != null;
        } catch (Exception e) {
            hasInstallWx = false;
            Logger.e("获取是否安装应用失败:" + e.getMessage());
        }
        return hasInstallWx;
    }
}
