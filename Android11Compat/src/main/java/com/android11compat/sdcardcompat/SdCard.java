package com.android11compat.sdcardcompat;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Create by JFZ
 * date: 2020-07-14 17:38
 **/
public class SdCard {
    //微信目录
    public static String path = File.separator + "MicroMsg" + File.separator;
    //群发目录
    public static String groupPath = path + "wxauto" + File.separator;
    //群发分享文件
    public static String groupName = "group.txt";
    public static String config = "config.txt";
    //群发日志文件
    public static String groupLog = "groupLog.txt";
    public static String qrcodeLog = "qrcodeLog.txt";
    //群名数量
    public static String qunName = "qunName.txt";

    public static String okok = "okok.txt";

    public static File createConfigFile() {
        return createFile(groupPath, config);
    }

    public static File createGroupFile() {
        return createFile(groupPath, groupName);
    }

    public static File createQunNameFile() {
        return createFile(groupPath, qunName);
    }

    //配置文件
    public static boolean config(String json) {
        File file = createConfigFile();
        return writeContent(file, json, false);
    }

    //内容写入群名文件
    public static boolean writeQunName(String content) {
        return writeContent(createQunNameFile(), content, false);
    }

    //内容写入群名文件
    public static boolean writeQunName(String content, boolean append) {
        return writeContent(createQunNameFile(), content, append);
    }

    //内容写入加群log文件
    public static boolean writeQrcodeLog(String content) {
        return writeContent(createQrcodeLog(), content, false);
    }

    //创建加群日志
    public static File createQrcodeLog() {
        return createFile(groupPath, qrcodeLog);
    }

    //内容写入log文件
    public static boolean writeGroupLog(String content) {
        return writeContent(createGroupLog(), content, false);
    }

    //创建群分享日志
    public static File createGroupLog() {
        return createFile(groupPath, groupLog);
    }

    //保存数据
    public static boolean saveGroup(String json) {
        File file = createGroupFile();
        return writeContent(file, json, false);
    }

    //创建文件
    public static File createFile(String filePath, String filename) {
        File rootDir = Environment.getExternalStorageDirectory();
        System.out.println("==sdcard:" + rootDir.getAbsolutePath());
        System.out.println("==sdcard状态:" + Environment.getExternalStorageState());
        File folderDir = new File(rootDir.getAbsolutePath() + filePath);
        if (!folderDir.exists()) {
            System.out.println("创建文件夹");
            folderDir.mkdirs();
        }
        File file = new File(folderDir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建文件异常:" + e.getMessage());
            }
        }
        System.out.println("==sdcard:" + file.getAbsolutePath());
        return file;
    }

    public static boolean writeContent(File file, String content, boolean append) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
            out.newLine();
            out.write(content);
            return true;
        } catch (IOException e) {
            System.out.println("==sdcard:" + e.getMessage());
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String readContent(File file) {
        BufferedReader reader = null;
        FileInputStream fis;
        StringBuilder sbd = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));
            String row;
            while ((row = reader.readLine()) != null) {
                sbd.append(row);
            }
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sbd.toString();
    }

    public static boolean delete(File file) {
        if (file.exists()) return file.delete();
        return true;
    }

    public static boolean delGroup() {
        return delete(createGroupFile());
    }

    public static boolean delConfig() {
        return delete(createConfigFile());
    }

    public static boolean delGroupLog() {
        return delete(createGroupLog());
    }

    public static boolean delQrcodeLog() {
        return delete(createQrcodeLog());
    }

    public static boolean delQunName() {
        return delete(createQunNameFile());
    }

    public static boolean delOkok() {
        return delete(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + groupPath + okok));
    }

    public static File getGroupLogFile() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + groupPath + groupLog);
    }

    public static File getQrCodeLogFile() {
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + groupPath + qrcodeLog);
    }
}
