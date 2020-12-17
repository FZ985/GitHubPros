package com.kmt.pro.utils;

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
 * date: 2020-07-23 11:42
 **/
public class SdCard {

    static final File sdcard = Environment.getExternalStorageDirectory();

    //创建文件
    public static File createFile(String filePath, String filename) {
        System.out.println("==sdcard:" + sdcard.getAbsolutePath());
        File folderDir = createDir(filePath);
        File file = new File(folderDir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("==sdcard:" + file.getAbsolutePath());
        return file;
    }

    //创建文件夹
    public static File createDir(String filepath) {
        File folderDir = new File(sdcard.getAbsolutePath() + filepath);
        if (!folderDir.exists() && folderDir.mkdirs()) {
        }
        return folderDir;
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
}
