package com.kmt.pro.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.kmt.pro.BuildConfig;
import com.kmt.pro.base.BaseApp;
import com.kmtlibs.app.utils.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.UUID;

import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class DeviceInfoUtils {
    private static final String TAG = "DeviceInfoUtils";

    /**
     * 获取清单文件 metadata 字段值
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
//        Logger.e(TAG, "metadata:" + channelNumber);
        return channelNumber;
    }

    public static String getChannel(Context context) {
        if (context == null) {
            return "guanfang";
        }
        String channelNumber = "guanfang";
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null && applicationInfo.metaData != null) {
                    channelNumber = applicationInfo.metaData.getString("UMENG_CHANNEL");
                }
            }
        } catch (Exception e) {
            channelNumber = "guanfang";
        }
        return channelNumber;
    }

    /**
     * 获取App 版本name
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager != null) {
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
                if (packageInfo != null) {
                    return packageInfo.versionName;
                }
            } catch (PackageManager.NameNotFoundException e) {
                return BuildConfig.VERSION_NAME;
            }
        }
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取 版本code
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
     */
    public static boolean isNotificationEnabled(Context context) {
        boolean isOpen = false;
        try {
            isOpen = NotificationManagerCompat.from(context).areNotificationsEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    /**
     * 获取当前手机系统语言。
     * <p>
     * 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前手机系统版本号
     * RELEASE
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取sdk版本
     * SDK
     */
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取设备品牌
     * BRAND
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    /**
     * 获取手机型号
     * MODEL
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * 设备版本号
     * ID
     */
    public static String getDeviceVersionId() {
        return Build.ID;
    }

    /**
     * 获取设备显示的版本包
     * DISPLAY
     */
    public static String getDeviceDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 获取产品名字
     * PRODUCT
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * 获取手机制作商
     * MANUFACTURER
     */
    public static String getSeviceVendor() {
        return Build.MANUFACTURER;
    }


    /**
     * 设备驱动名称
     * DEVICE
     */
    public static String getDeviceQuDongName() {
        return Build.DEVICE;
    }

    /**
     * 设备硬件
     * HARDWARE
     */
    public static String getDeviceHardware() {
        return Build.HARDWARE;
    }

    /**
     * 指纹
     * 设备的唯一标识。由设备的多个信息拼接合成
     * <p>
     * FINGERPRINT
     */
    public static String getDeviceZhiWen() {
        return Build.FINGERPRINT;
    }

    /**
     * 串口序列号
     * SERIAL
     */
    public static String getDeviceSerial(Context context) {
        String serial = Build.SERIAL;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    serial = Build.getSerial();
                } catch (Exception e) {
                    System.out.println("---serial-exception:" + serial);
                }
            }
        }
        return serial;
    }

    /**
     * 设备版本类型，主要为user 或eng.
     * TYPE
     */
    public static String getDeviceType() {
        return Build.TYPE;
    }

    /**
     * 设备标签。如release-keys 或测试的 test-keys
     * TAGS
     */
    public static String getDeviceTag() {
        return Build.TAGS;
    }

    /**
     * 设备主机地址
     * HOST
     */
    public static String getDeviceHost() {
        return Build.HOST;
    }

    /**
     * 设备用户名
     * USER
     */
    public static String getDeviceUserName() {
        return Build.USER;
    }

    /**
     * 固件开发版本号
     * CODENAME
     */
    public static String getDeviceCodeName() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 主板
     * board
     */
    public static String getDeviceBoard() {
        return Build.BOARD;
    }

    /**
     * 系统api 级别
     * SDK_INT
     */
    public static int getSystemSDK_INT() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * cpu 指令集1
     * CPU_ABI
     */
    public static String getCpuABI1() {
        return Build.CPU_ABI;
    }

    /**
     * cpu 指令集2
     * CPU_ABI2
     */
    public static String getCpuABI2() {
        return Build.CPU_ABI2;
    }

    /**
     * 获取设备信息
     */
    @SuppressLint("MissingPermission")
    public static String[] getDeviceParams(Context ctx) {
        String[] params = new String[11];
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                params[0] = tm.getDeviceId() + "";//imei
                params[1] = tm.getLine1Number() + "";//手机号
                params[2] = tm.getSimSerialNumber() + "";//手机卡序列号
                params[3] = tm.getSubscriberId() + "";//imsi
                params[4] = tm.getSimCountryIso() + "";//手机卡国家
                params[5] = tm.getSimOperator() + "";//运营商
                params[6] = tm.getSimOperatorName() + "";//运营商名字
                params[7] = tm.getNetworkCountryIso() + "";//国家iso代码
                params[8] = tm.getNetworkOperator() + "";//返回MCC+MNC代码
                params[9] = tm.getNetworkOperatorName() + "";//返回移动网络运营商的名字(SPN)
                params[10] = tm.getNetworkType() + "";//网络类型
                return params;
            }
        } catch (Exception e) {
            System.out.println("设备参数获取失败:" + e.getMessage());
        }
        return params;
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
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
        return "0";
    }

    /**
     * 检测手机是否root
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
        final DeviceInfoUtils.ScriptRunner runner = new DeviceInfoUtils.ScriptRunner(file, script, res);
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

    private static String getLocalIpAddress() {
//        IPV6的地址形式
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()) {
//                        return inetAddress.getHostAddress().toString();
//                    }
//                }
//            }
//        } catch (SocketException ex) {
//            Log.e("LOG_TAG", ex.toString());
//        }
//        return "";

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }

        return "";

//        String ipv4 = "";
//        try {
//            List<NetworkInterface> nilist = Collections.list(NetworkInterface.getNetworkInterfaces());
//            for (NetworkInterface ni : nilist) {
//                List<InetAddress> ialist = Collections.list(ni.getInetAddresses());
//                for (InetAddress address : ialist) {
////                    if (!address.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ipv4 = address.getHostAddress())) {
//                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
//                        ipv4 = address.getHostAddress();
//                        return ipv4;
//                    }
//                }
//            }
//
//        } catch (SocketException ex) {
//            Log.e("LOG_TAG", ex.toString());
//        }
//        return ipv4;
    }

    public static String getIp(final Context context) {
        String ip = null;
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // mobile 3G Data Network
        android.net.NetworkInfo.State mobile = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).getState();
        // wifi
        android.net.NetworkInfo.State wifi = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).getState();

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == android.net.NetworkInfo.State.CONNECTED
                || mobile == android.net.NetworkInfo.State.CONNECTING) {
            ip = getLocalIpAddress();
        }
        if (wifi == android.net.NetworkInfo.State.CONNECTED
                || wifi == android.net.NetworkInfo.State.CONNECTING) {
            //获取wifi服务
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            //判断wifi是否开启
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF);
        }
        return ip;

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


    /**
     * 获取CPU型号
     *
     * @return
     */
    public static String getCpuName() {

        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return "";
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */

    public static String getBaseband_Ver() {
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            // System.out.println(">>>>>>><<<<<<<" +(String)result);
            Version = (String) result;
        } catch (Exception e) {
        }
        return Version;
    }

    /**
     * CORE-VER
     * 内核版本
     * return String
     */

    public static String getLinuxCore_Ver() {
        Process process = null;
        String kernelVersion = "";
        try {
            process = Runtime.getRuntime().exec("cat /proc/version");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // get the output line
        InputStream outs = process.getInputStream();
        InputStreamReader isrout = new InputStreamReader(outs);
        BufferedReader brout = new BufferedReader(isrout, 8 * 1024);


        String result = "";
        String line;
        // get the whole standard output string
        try {
            while ((line = brout.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (result != "") {
                String Keyword = "version ";
                int index = result.indexOf(Keyword);
                line = result.substring(index + Keyword.length());
                index = line.indexOf(" ");
                kernelVersion = line.substring(0, index);
            }
        } catch (Exception e) {
            System.out.println(TAG + ">>>内核版本e:" + e.getMessage());
        }
        return kernelVersion;
    }

    /**
     * INNER-VER
     * 内部版本
     * return String
     */
    public static String getInner_Ver() {
        String ver = "";

        if (Build.DISPLAY.contains(Build.VERSION.INCREMENTAL)) {
            ver = Build.DISPLAY;
        } else {
            ver = Build.VERSION.INCREMENTAL;
        }
        return ver;
    }

    public static void main() {
        Context context = BaseApp.getInstance();
        System.out.println(TAG + ">>>App渠道:" + DeviceInfoUtils.getChannel(context));
        System.out.println(TAG + ">>>App版本名字:" + DeviceInfoUtils.getVersionName(context));
        System.out.println(TAG + ">>>App版本Code:" + DeviceInfoUtils.getVersionCode(context));
        System.out.println(TAG + ">>>系统版本:" + DeviceInfoUtils.getSystemVersion());
        System.out.println(TAG + ">>>SDK版本:" + DeviceInfoUtils.getSDKVersion());
        System.out.println(TAG + ">>>设备品牌:" + DeviceInfoUtils.getDeviceBrand());
        System.out.println(TAG + ">>>设备型号:" + DeviceInfoUtils.getDeviceModel());
        System.out.println(TAG + ">>>设备版本号:" + DeviceInfoUtils.getDeviceVersionId());
        System.out.println(TAG + ">>>设备版本包:" + DeviceInfoUtils.getDeviceDisplay());
        System.out.println(TAG + ">>>产品名:" + DeviceInfoUtils.getProduct());
        System.out.println(TAG + ">>>制造商:" + DeviceInfoUtils.getSeviceVendor());
        System.out.println(TAG + ">>>设备驱动名称:" + DeviceInfoUtils.getDeviceQuDongName());
        System.out.println(TAG + ">>>设备硬件:" + DeviceInfoUtils.getDeviceHardware());
        System.out.println(TAG + ">>>指纹:" + DeviceInfoUtils.getDeviceZhiWen());
        System.out.println(TAG + ">>>串口序列号:" + DeviceInfoUtils.getDeviceSerial(context));
        System.out.println(TAG + ">>>设备版本类型:" + DeviceInfoUtils.getDeviceType());
        System.out.println(TAG + ">>>设备标签:" + DeviceInfoUtils.getDeviceTag());
        System.out.println(TAG + ">>>设备主机地址:" + DeviceInfoUtils.getDeviceHost());
        System.out.println(TAG + ">>>设备用户名:" + DeviceInfoUtils.getDeviceUserName());
        System.out.println(TAG + ">>>设备固件开发版本号:" + DeviceInfoUtils.getDeviceCodeName());
        System.out.println(TAG + ">>>主板:" + DeviceInfoUtils.getDeviceBoard());
        System.out.println(TAG + ">>>系统api级别:" + DeviceInfoUtils.getSystemSDK_INT());
        System.out.println(TAG + ">>>Cpu指令集1:" + DeviceInfoUtils.getCpuABI1());
        System.out.println(TAG + ">>>Cpu指令集2:" + DeviceInfoUtils.getCpuABI2());
        String[] devInfo = DeviceInfoUtils.getDeviceParams(context);
        System.out.println(TAG + ">>>Imei:" + devInfo[0]);
        System.out.println(TAG + ">>>手机号:" + devInfo[1]);
        System.out.println(TAG + ">>>手机卡序列号:" + devInfo[2]);
        System.out.println(TAG + ">>>Imsi:" + devInfo[3]);
        System.out.println(TAG + ">>>手机卡国家:" + devInfo[4]);
        System.out.println(TAG + ">>>手机运营商:" + devInfo[5]);
        System.out.println(TAG + ">>>手机运营商名字:" + devInfo[6]);
        System.out.println(TAG + ">>>国家iso代码:" + devInfo[7]);
        System.out.println(TAG + ">>>MCC+MNC代码:" + devInfo[8]);
        System.out.println(TAG + ">>>返回移动网络运营商的名字(SPN):" + devInfo[9]);
        System.out.println(TAG + ">>>网络类型:" + devInfo[10]);

        String[] wifiInfo = DeviceInfoUtils.getMobileWifiInfo(context);
        for (int i = 0; i < wifiInfo.length; i++) {
            System.out.println(TAG + ">>>:wifiInfo[" + i + "]" + wifiInfo[i]);
        }

        System.out.println(TAG + ">>>ip:" + DeviceInfoUtils.getIp(context));
        System.out.println(TAG + ">>>是否root:" + DeviceInfoUtils.hasRootAccess(context));
        System.out.println(TAG + ">>>是否开启通知:" + DeviceInfoUtils.isNotificationEnabled(context));
        System.out.println(TAG + ">>>基带版本:" + DeviceInfoUtils.getBaseband_Ver());
        System.out.println(TAG + ">>>内核版本:" + DeviceInfoUtils.getLinuxCore_Ver());
        System.out.println(TAG + ">>>内部版本:" + DeviceInfoUtils.getInner_Ver());
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
    @SuppressLint({"MissingPermission"})
    public static String getIMEI(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
            if (tm != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String imei = tm.getImei();
                    if (Build.VERSION.SDK_INT >= 29) {
                        String i = imei.toLowerCase();
                        if (TextUtils.isEmpty(i) || i.equals("null") || i.equals("unknown"))
                            return Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                    return tm.getImei();
                } else {
                    return tm.getDeviceId();
                }
            }
        } catch (Exception e) {
            Logger.e("imeiError:" + e.getMessage());
        }
        return readAndroidId(context);
    }

    public static String getFinalClientId(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            //有权限
            String imei = getIMEI(context);
            Logger.e("final_clientId_imei:" + imei);
            return imei;
        }
        //无权限， androidId
        String androidId = getOnlyId(context);
        Logger.e("final_clientId_androidId:" + androidId);
        return androidId;
    }

    public static String getOnlyId(Context context) {
        String androidId = readAndroidId(context);
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String SERIAL = getDeviceSerial(context);
        try {
            if (androidId.equals("9774d56d682e549c")) {
                return SERIAL + m_szDevIDShort;
            } else {
                return androidId;
            }
        } catch (Exception e) {
            return new UUID(m_szDevIDShort.hashCode(), SERIAL.hashCode()).toString();
        }
    }
}
