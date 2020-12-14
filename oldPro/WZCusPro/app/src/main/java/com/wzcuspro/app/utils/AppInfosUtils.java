package com.wzcuspro.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Created by jfz
 * on 2018/6/4.
 */

public class AppInfosUtils {
    private static final String TAG = "AppInfosUtils";

    /**
     * 获取清单文件 metadata 字段值
     *
     * @param context
     * @param key
     * @return
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return "";
        }
        String channelNumber = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            channelNumber = "";
        }
        Logger.e(TAG, "metadata:" + channelNumber);
        return channelNumber;
    }

    /**
     * 获取App 版本name
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String metadata = getAppMetaData(context, "APP_VERSION_NAME");
        if (!TextUtils.isEmpty(metadata) && metadata.contains("=")) {
            return metadata.split("=")[1];
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null) {
                    return packageInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return "";
            }
        }
        return "";
    }

    /**
     * 获取 版本code
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        String metadata = getAppMetaData(context, "APP_VERSION_CODE");
        if (!TextUtils.isEmpty(metadata) && metadata.contains("=")) {
            return Integer.valueOf(metadata.split("=")[1]);
        }
        PackageManager pm = context.getPackageManager();
        if (pm != null) {
            try {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                if (pi != null) {
                    return pi.versionCode;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return -1;
            } catch (NullPointerException e) {
                return -1;
            }
        }
        return -1;
    }
    /**
     * 获取安卓通知是否开启
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        boolean isOpen = false;
        try {
            isOpen = NotificationManagerCompat.from(context).areNotificationsEnabled();
        }catch (Exception e){
            e.printStackTrace();
        }
        return isOpen;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前手机系统版本号
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机制作商
     * @return 手机制造商
     */
    public static String getSystemVendor(){
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getIMEI(Context ctx) {
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                return tm.getDeviceId();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    /**
     * 读取手机device_id
     *
     * @param context ctx
     * @return dev_id
     */
    public static String readPhoneDeviceId(Context context) {
        TelephonyManager telephonyManager = null;
        try {
            telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint({"HardwareIds", "MissingPermission"}) String deviceId = telephonyManager.getDeviceId();
            if (deviceId != null && !"".equals(deviceId) && deviceId.length() > 0) {
                return deviceId;
            }
            return "0";
        }catch (Exception e){
            e.printStackTrace();
            return "0";
        }finally {
            if(telephonyManager != null){
                telephonyManager = null;
            }
        }
    }

    /**
     * 读取手机安卓id
     *
     * @param context 上下文
     * @return android_id
     */
    public static String readAndroidId(Context context) {
        String androidId = Settings.System.getString(context.getContentResolver(),
                Settings.System.ANDROID_ID);
        if (androidId != null && !"".equals(androidId)) {
            return androidId;
        }
        return "";
    }

    /**
     * 检测手机是否root
     *
     * @param ctx 上下文
     * @return
     */
    public static boolean hasRootAccess(Context ctx) {
        final StringBuilder res = new StringBuilder();
        try {
            if (runScriptAsRoot(ctx, "exit 0", res) == 0)
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    public static int runScriptAsRoot(Context ctx, String script, StringBuilder res) {
        final File file = new File(ctx.getCacheDir(), "secopt.sh");
        final ScriptRunner runner = new ScriptRunner(file, script, res);
        runner.start();
        try {
            runner.join(40000);
            if (runner.isAlive()) {
                runner.interrupt();
                runner.join(150);
                runner.destroy();
                runner.join(50);
            }
        } catch (InterruptedException ex) {
        }
        return runner.exitcode;
    }

    private static final class ScriptRunner extends Thread {
        private final File file;
        private final String script;
        private final StringBuilder res;
        public int exitcode = -1;
        private Process exec;

        public ScriptRunner(File file, String script, StringBuilder res) {
            this.file = file;
            this.script = script;
            this.res = res;
        }

        @Override
        public void run() {
            try {
                file.createNewFile();
                final String abspath = file.getAbsolutePath();
                Runtime.getRuntime().exec("chmod 777 " + abspath).waitFor();
                final OutputStreamWriter out = new OutputStreamWriter(
                        new FileOutputStream(file));
                if (new File("/system/bin/sh").exists()) {
                    out.write("#!/system/bin/sh\n");
                }
                out.write(script);
                if (!script.endsWith("\n"))
                    out.write("\n");
                out.write("exit\n");
                out.flush();
                out.close();

                exec = Runtime.getRuntime().exec("su");
                DataOutputStream os = new DataOutputStream(exec.getOutputStream());
                os.writeBytes(abspath);
                os.flush();
                os.close();

                InputStreamReader r = new InputStreamReader(exec.getInputStream());
                final char buf[] = new char[1024];
                int read = 0;
                while ((read = r.read(buf)) != -1) {
                    if (res != null)
                        res.append(buf, 0, read);
                }

                r = new InputStreamReader(exec.getErrorStream());
                read = 0;
                while ((read = r.read(buf)) != -1) {
                    if (res != null)
                        res.append(buf, 0, read);
                }

                if (exec != null)
                    this.exitcode = exec.waitFor();
            } catch (InterruptedException ex) {
                if (res != null)
                    res.append("\nOperation timed-out");
            } catch (Exception ex) {
                if (res != null)
                    res.append("\n" + ex);
            } finally {
                destroy();
            }
        }

        public synchronized void destroy() {
            if (exec != null)
                exec.destroy();
            exec = null;
        }
    }

    /**
     * 获取手机设备信息
     *
     * @param context ctx
     * @return String[]
     */
    public static String[] getMobileDevicesInfo(Context context) {
        String[] result = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                result = new String[3];
                @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
                @SuppressLint("MissingPermission") String imsi = tm.getSubscriberId();
                @SuppressLint("MissingPermission") String numer = tm.getLine1Number(); // 手机号码

                result[0] = imei;
                result[1] = imsi;
                result[2] = numer;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 获取手机wifi连接信息
     *
     * @param context ctx
     * @return 0: getMacAddress() 1: getBSSID() 2:getIpAddress() 4: getSSID();
     */
    public static String[] getMobileWifiInfo(Context context) {
        String[] wifiInfos = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null) {
            wifiInfos = new String[5];
            @SuppressLint("HardwareIds")
            String netMacAdd = wifiInfo.getMacAddress();
            // 0626 新增判断空的方式 防止出现空指针
            if (netMacAdd != null && !"".equals(netMacAdd) && netMacAdd.equals("02:00:00:00:00:00")) {
                netMacAdd = getWifiInfoMacAddress();
            }
            int netIPAdd = wifiInfo.getIpAddress();
            String netName = wifiInfo.getSSID();
            String netClientMacAdd = wifiInfo.getBSSID();
            String netType = Tools.getNetworkState(context);
            wifiInfos[0] = netMacAdd;
            wifiInfos[1] = netClientMacAdd;
            wifiInfos[2] = netIPAdd + "";
            wifiInfos[3] = netName;
            wifiInfos[4] = netType;

        }

        return wifiInfos;

        //      getBSSID()  获取BSSID属性
//      getDetailedStateOf()  获取客户端的连通性
//      getHiddenSSID()  获取SSID 是否被隐藏
//      getIpAddress()  获取IP 地址
//      getLinkSpeed()  获取连接的速度
//      getMacAddress()  获取Mac 地址
//      getRssi()  获取802.11n 网络的信号
//      getSSID()  获取SSID
//      getSupplicanState()  获取具体客户端状态的信息
    }
    /**
     * M 获取wifi mac
     *
     * @return mac str
     */
    private static String getWifiInfoMacAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                byte[] addrs = networkInterface.getHardwareAddress();
                if (addrs != null && addrs.length > 0) {
                    StringBuilder buf = new StringBuilder();
                    for (byte b : addrs) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    return mac;
                } else {
                    return "00:00:00:00:00:00";
                }

            } else {
                return "00:00:00:00:00:00";
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return "00:00:00:00:00:00";
        }
    }
    /**
     * 将ip的整数形式转换成ip形式
     *
     * @param ipInt
     * @return ""
     */
    public static String int2ip(int ipInt) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(ipInt & 0xFF).append(".");
            sb.append((ipInt >> 8) & 0xFF).append(".");
            sb.append((ipInt >> 16) & 0xFF).append(".");
            sb.append((ipInt >> 24) & 0xFF);
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机信息
     *
     * @param context ctx
     * @return string[]
     */
    public static String[] getMobileParams(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String[] mobileInfos = new String[5];
        String product = Build.PRODUCT;
        String brand = Build.BRAND;// 手机品牌
        String model = Build.MODEL; // 手机型号

        String serviceName = tm.getSimOperatorName(); // 运营商

        mobileInfos[0] = product;
        mobileInfos[1] = brand;
        mobileInfos[2] = model;
        mobileInfos[3] = serviceName;
        mobileInfos[4] = readAndroidId(context);

        return mobileInfos;
    }

    /**
     * @description :获取应用名称
     */
    public static String getAppName(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (Exception e) {
            return "";
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }
}
