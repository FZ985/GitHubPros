package com.v2sign.apkreadwrite;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import apkzip.reader.ApkUtil;
import apkzip.reader.PayloadReader;
import apkzip.reader.SignatureNotFoundException;
import apkzip.writer.ChannelWriter;

/**
 * Description:
 * Author: jfz
 * Date: 2021-01-18 10:05
 */
public class ApkInfo {

    public static void write(File file) {
        try {
            ChannelWriter.putRaw(file, "asdfghjkl" + System.currentTimeMillis() + "asdsdddhfjfjf==33##");
        } catch (IOException e) {
            System.out.println("写入apk信息失败1:" + e.getMessage());
            e.printStackTrace();
        } catch (SignatureNotFoundException e) {
            System.out.println("写入apk信息失败2:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String read(File file) {
        return PayloadReader.getString(file, ApkUtil.APK_CHANNEL_BLOCK_ID);
    }

    public static String readSelf(Context context) {
        try {
            String path = context.getApplicationContext().getPackageResourcePath();
            File file = new File(path);
            return PayloadReader.getString(file, ApkUtil.APK_CHANNEL_BLOCK_ID);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取本地apk信息失败:" + e.getMessage());
            return null;
        }
    }

}