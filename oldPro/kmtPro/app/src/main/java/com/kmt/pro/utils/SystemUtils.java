package com.kmt.pro.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.UUID;

/**
 * Create by JFZ
 * date: 2020-06-29 17:51
 **/
public class SystemUtils {
    /**
     * <p>
     * 获取设备UUID，由设备信息产生
     * </p>
     *
     * @return 设备UUID。
     */
    public static final String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        if (PermissionCheck.checkPhoneReadPermission(context) && tm != null) {
            tmDevice = "" + tm.getDeviceId();
        } else tmDevice = "";
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    /**
     * <p>
     * 获取手机品牌。
     * </p>
     *
     * @return 手机品牌
     */
    public static final String getDeviceName() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * <p>
     * 当前系统平台。
     * </p>
     *
     * @return "Android"
     */
    public static final String getPlatform() {
        return "android-" + getVersion() + "/";
    }

    /**
     * <p>
     * 获取手机操作系统版本号。
     * </p>
     *
     * @return 手机操作系统版本号
     */
    public static final String getVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
}
