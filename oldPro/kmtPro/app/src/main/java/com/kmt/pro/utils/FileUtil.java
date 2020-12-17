package com.kmt.pro.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import com.kmt.pro.base.BaseApp;
import com.kmtlibs.app.utils.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Create by JFZ
 * date: 2020-07-20 18:07
 **/
public class FileUtil {
    // SD卡路径
    public final static String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public final static String download = "download";//下载App的文件夹

    //创建下载App文件
    public static String createDownloadAppFile() {
        File fileDir = BaseApp.getInstance().getExternalFilesDir(download);
        return fileDir.getPath() + File.separator;
    }











    public static File saveFile(File file, Bitmap bm) {
        Logger.log("saveBitmap", "保存图片:" + file.getPath());
        File f = file;
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            // path = f.getPath();
            Logger.log("saveBitmap", "已经保存:" + f.getPath());
            // updatePicture(context, f, f.getPath());
            return f;
        } catch (FileNotFoundException e) {
            Logger.e("保存失败:" + e.getMessage());
            return f;
        } catch (IOException e) {
            Logger.e("保存失败1:" + e.getMessage());
            return f;
        }
    }
}
