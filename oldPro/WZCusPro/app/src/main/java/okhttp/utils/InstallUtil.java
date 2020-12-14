package okhttp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import com.wzcuspro.app.base.BaseApp;

import java.io.File;

/**
 * 安装工具类<br/>
 */
public class InstallUtil {
    // 下载完成后打开安装apk界面
    public static void installApk(File file, Context context) {
        Intent openFile = getFileIntent(file);
        context.startActivity(openFile);
    }

    public static Intent getFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(BaseApp.getInstance(), BaseApp.getInstance().getPackageName() + ".fileprovider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
//        intent.setDataAndType(data, "application/vnd.android.package-archive");
        String type = getMIMEType(file);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(data, type);
        return intent;
    }

    public static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        // 取得扩展名
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length());
        if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            // /*如果无法直接打开，就跳出软件列表给用户选择 */
            type = "*/*";
        }
        return type;
    }
}
