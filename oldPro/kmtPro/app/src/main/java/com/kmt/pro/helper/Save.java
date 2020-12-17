package com.kmt.pro.helper;

import android.graphics.Bitmap;
import android.os.Environment;

import com.kmt.pro.utils.FileUtil;
import com.kmt.pro.utils.SdCard;
import com.kmtlibs.app.utils.Logger;

import java.io.File;

import renrenkan.Md5Utils;

/**
 * Create by JFZ
 * date: 2020-07-23 11:38
 **/
public class Save {
    final static String imagePath = File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;

    //保存充值地址图片
    public static File saveSymbolImage(Bitmap bitmap,String address) {
        //创建保存充值地址图片路径
        String path = SdCard.createDir(imagePath).getPath();
        Logger.e("数字图片地址路径:"+path);
        String fileName = Md5Utils.MD5("symbol_id_" + address) + ".jpg";
        return FileUtil.saveFile(new File(path, fileName), bitmap);
    }
}
