package com.v2sign.apkreadwrite;

import android.content.Context;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import apkzip117.payload_reader.PayloadReader;
import apkzip117.payload_reader.SignatureNotFoundException;
import apkzip117.payload_writer.PayloadWriter;


/**
 * Description:
 * Author: jfz
 * Date: 2021-01-18 10:05
 */
public class ApkInfo {

    private static final int ID = 10000;

    public static void write(File file) {
//        try {
//            ChannelWriter.putRaw(file, "asdfghjkl" + System.currentTimeMillis() + "asdsdddhfjfjf==33##");
//        } catch (IOException e) {
//            System.out.println("写入apk信息失败1:" + e.getMessage());
//            e.printStackTrace();
//        } catch (SignatureNotFoundException e) {
//            System.out.println("写入apk信息失败2:" + e.getMessage());
//            e.printStackTrace();
//        }

        try {
            String name = "哈哈";
            String id = "FD94JG76JGBHKSD3285TNV85Y93M";
            String invite = "1009086";
            String md5 = MD5Utils.md5(name + "&" + id + "&" + invite);
            String content = new Gson().toJson(new UserBean(name, id, invite, md5));
            PayloadWriter.put(file, ID, content);
        } catch (IOException e) {
            System.out.println("写入apk信息失败1:" + e.getMessage());
            e.printStackTrace();
        } catch (SignatureNotFoundException e) {
            System.out.println("写入apk信息失败2:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String read(File file) {
//        return PayloadReader.getString(file, ApkUtil.APK_CHANNEL_BLOCK_ID);
        return PayloadReader.getString(file, ID);
    }

    public static String readSelf(Context context) {
        try {
            String path = context.getApplicationContext().getPackageResourcePath();
            File file = new File(path);
//            return PayloadReader.getString(file, ApkUtil.APK_CHANNEL_BLOCK_ID);
            return PayloadReader.getString(file, ID);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取本地apk信息失败:" + e.getMessage());
            return null;
        }
    }

}