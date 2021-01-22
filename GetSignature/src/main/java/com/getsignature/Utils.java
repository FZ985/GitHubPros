package com.getsignature;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;

import java.security.MessageDigest;

/**
 * Description: 获取应用签名
 * Author: jfz
 * Date: 2021-01-21 15:44
 */
public class Utils {

    @SuppressLint("WrongConstant")
    public static String getSign(Context context, String packageName) {
        if ((packageName == null) || (packageName.length() == 0)) {
            return "getSignature, packageName is null";
        }
        PackageManager manager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = manager.getPackageInfo(packageName, 64);
            if (packageInfo == null) {
                return "info is null, packageName = " + packageName;
            }
            Signature[] signatures = packageInfo.signatures;
            if ((signatures == null) || (signatures.length == 0)) {
                return "signs is null";
            }
            StringBuilder builder = new StringBuilder();
            int j = signatures.length;
            int i = 0;
            while (i < j) {
                builder.append(getMessageDigest(signatures[i].toByteArray()));
                i += 1;
            }
            return builder.toString();
        } catch (PackageManager.NameNotFoundException paramContext) {
            return "NameNotFoundException";
        }
    }

    public static final String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar = new char[16];
        char[] tmp8_6 = arrayOfChar;
        tmp8_6[0] = 48;
        char[] tmp13_8 = tmp8_6;
        tmp13_8[1] = 49;
        char[] tmp18_13 = tmp13_8;
        tmp18_13[2] = 50;
        char[] tmp23_18 = tmp18_13;
        tmp23_18[3] = 51;
        char[] tmp28_23 = tmp23_18;
        tmp28_23[4] = 52;
        char[] tmp33_28 = tmp28_23;
        tmp33_28[5] = 53;
        char[] tmp38_33 = tmp33_28;
        tmp38_33[6] = 54;
        char[] tmp44_38 = tmp38_33;
        tmp44_38[7] = 55;
        char[] tmp50_44 = tmp44_38;
        tmp50_44[8] = 56;
        char[] tmp56_50 = tmp50_44;
        tmp56_50[9] = 57;
        char[] tmp62_56 = tmp56_50;
        tmp62_56[10] = 97;
        char[] tmp68_62 = tmp62_56;
        tmp68_62[11] = 98;
        char[] tmp74_68 = tmp68_62;
        tmp74_68[12] = 99;
        char[] tmp80_74 = tmp74_68;
        tmp80_74[13] = 100;
        char[] tmp86_80 = tmp80_74;
        tmp86_80[14] = 101;
        char[] tmp92_86 = tmp86_80;
        tmp92_86[15] = 102;
        try {
            MessageDigest localObject = MessageDigest.getInstance("MD5");
            (localObject).update(paramArrayOfByte);
            paramArrayOfByte = (localObject).digest();
            int k = paramArrayOfByte.length;
            char[] charArr = new char[k * 2];
            int i = 0;
            int j = 0;
            while (true) {
                if (i >= k)
                    return new String(charArr);
                int m = paramArrayOfByte[i];
                int n = j + 1;
                charArr[j] = arrayOfChar[(m >>> 4 & 0xF)];
                j = n + 1;
                charArr[n] = arrayOfChar[(m & 0xF)];
                i += 1;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static final byte[] getRawDigest(byte[] paramArrayOfByte) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            paramArrayOfByte = localMessageDigest.digest();
            return paramArrayOfByte;
        } catch (Exception e) {
        }
        return null;
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
}