package com.lxbuytimes.kmtapp.retrofit.tool;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.lxbuytimes.kmtapp.retrofit.BaseRequestParams;
import com.lxbuytimes.kmtapp.retrofit.RetrofitManager;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestBody;
import com.lxbuytimes.kmtapp.retrofit.def.upload.ProgressRequestListener;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Create by JFZ
 * date: 2019-10-16 14:01
 **/
public class HttpUtils {

    public static final int GET = 1;
    public static final int POST = 2;
    public static final int JSON = 3;
    public static final int FORM = 4;

    public static class MediaTypes {
        public static final MediaType APPLICATION_ATOM_XML_TYPE = MediaType.parse("application/atom+xml;charset=utf-8");
        public static final MediaType APPLICATION_FORM_URLENCODED_TYPE = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
        public static final MediaType APPLICATION_JSON_TYPE = MediaType.parse("application/json;charset=utf-8");
        public static final MediaType APPLICATION_OCTET_STREAM_TYPE = MediaType.parse("application/octet-stream");
        public static final MediaType APPLICATION_SVG_XML_TYPE = MediaType.parse("application/svg+xml;charset=utf-8");
        public static final MediaType APPLICATION_XHTML_XML_TYPE = MediaType.parse("application/xhtml+xml;charset=utf-8");
        public static final MediaType APPLICATION_XML_TYPE = MediaType.parse("application/xml;charset=utf-8");
        public static final MediaType MULTIPART_FORM_DATA_TYPE = MediaType.parse("multipart/form-data;charset=utf-8");
        public static final MediaType TEXT_HTML_TYPE = MediaType.parse("text/html;charset=utf-8");
        public static final MediaType TEXT_XML_TYPE = MediaType.parse("text/xml;charset=utf-8");
        public static final MediaType TEXT_PLAIN_TYPE = MediaType.parse("text/plain;charset=utf-8");
        public static final MediaType IMAGE_TYPE = MediaType.parse("image/*");
        public static final MediaType WILDCARD_TYPE = MediaType.parse("*/*");
    }

    public static String reqParams(Object src) {
        String json = "";
        if (src != null && src instanceof String) {
            json = (String) src;
        } else {
            if (src == null) {
            } else {
                json = RetrofitManager.GSON.toJson(src);
            }
        }
        return json;
    }

    public static void log(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void log(String msg) {
        log("RetrofitHttp", msg);
    }

    public static void v(String msg) {
        System.out.println("v_RetrofitHttp:" + msg);
    }

    public static String obtainFilePath(Context c) {
        //最后一位不带反斜杠
        return SdCard.obtainPhoneMemoryPath(c);
    }

    public static void createNewDir(String dir) {
        SdCard.createNewDir(dir);
    }

    private static class SdCard {
        //获取可用内存的路径
        private static String obtainPhoneMemoryPath(Context c) {
            String sdStatus = Environment.getExternalStorageState();
            boolean sdCardExist = sdStatus
                    .equals(Environment.MEDIA_MOUNTED);

            if (TextUtils.isEmpty(sdStatus)) {
                return c.getFilesDir().getAbsolutePath();
            }

            if (!sdCardExist) {
                return c.getFilesDir().getAbsolutePath();
            }
            try {
                long sdcardSpace = 0;
                try {
                    sdcardSpace = getSDcardAvailableSpace();
                } catch (Exception e) {
                    HttpUtils.log("error1:" + e.getMessage());
                }
                if (sdcardSpace >= 5) {
                    return getSDCardPath(c);
                }
                long phoneSpace = getDataStorageAvailableSpace();
                if (phoneSpace >= 5) {
                    return c.getFilesDir().getAbsolutePath();
                }
                HttpUtils.log(String.format("get storage space, phone: %d, sdcard: %d",
                        (int) (phoneSpace / 1024 / 1024),
                        (int) (sdcardSpace / 1024 / 1024)));
            } catch (Exception e) {
                HttpUtils.log("error3:" + e.getMessage());
            }

            return c.getFilesDir().getAbsolutePath();
        }


        @SuppressWarnings("deprecation")
        private static long getSDcardAvailableSpace() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                File path = Environment.getExternalStorageDirectory();
                if (path == null) {
                    return 0;
                }
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return availableBlocks * blockSize; // "Byte"
            } else {
                return 0;
            }
        }

        /**
         * 获取手机内置SD卡路径 或者文件储存路径
         */
        private static String getSDCardPath(Context c) {
            File sdDir = null;
            String sdStatus = Environment.getExternalStorageState();
            boolean sdCardExist = sdStatus.equals(Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();
                return sdDir.getPath();
            }
            return c.getFilesDir().getPath();
        }

        /**
         * 获取手机内部可用空间大小
         *
         * @return
         */
        @SuppressWarnings("deprecation")
        private static long getDataStorageAvailableSpace() {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        }

        /**
         * 判断SD卡是否存在
         *
         * @return boolean
         */
        private static boolean checkSDCard() {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }

        private static void createNewDir(String dir) {
            if (!checkSDCard()) {
                return;
            }
            if (null == dir) {
                return;
            }
            File f = new File(dir);
            if (!f.exists()) {
                String[] pathSeg = dir.split(File.separator);
                String path = "";
                for (String temp : pathSeg) {
                    if (TextUtils.isEmpty(temp)) {
                        path += File.separator;
                        continue;
                    } else {
                        path += temp + File.separator;
                    }
                    File tempPath = new File(path);
                    if (tempPath.exists() && !tempPath.isDirectory()) {
                        tempPath.delete();
                    }
                    tempPath.mkdirs();
                }
            } else {
                if (!f.isDirectory()) {
                    f.delete();
                    f.mkdirs();
                }
            }
        }
    }

    public static class Upload {

        /**
         * 包装请求体用于上传文件的回调
         *
         * @param requestBody             请求体RequestBody
         * @param progressRequestListener 进度回调接口
         * @return 包装后的进度回调请求体
         */
        public static ProgressRequestBody addProgressRequestListener(RequestBody requestBody, ProgressRequestListener progressRequestListener) {
            //包装请求体
            return new ProgressRequestBody(requestBody, progressRequestListener);
        }


        public static RequestBody createRequestBody(BaseRequestParams params) {
            MultipartBody.Builder builder = new MultipartBody.Builder("AaB03x");
            builder.setType(MultipartBody.FORM);
            if (params != null) {
                if (params.requestParams != null) {
                    for (Map.Entry<String, Object> stringObjectEntry : params.requestParams.entrySet()) {
                        Object key = ((Map.Entry) stringObjectEntry).getKey();
                        Object val = ((Map.Entry) stringObjectEntry).getValue();
                        if (val == null) {
                            val = "";
                        }
                        HttpUtils.log("##请求数据##key:" + key + ",value:" + val);
                        builder.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\""
                                        + key + "\""),
                                RequestBody.create(null, val.toString()));
                    }
                }

                if (params.files != null) {
                    RequestBody fileBody = null;
                    for (Map.Entry<String, File> fileEntry : params.files.entrySet()) {
                        Object key = ((Map.Entry) fileEntry).getKey();
                        File file = (File) ((Map.Entry) fileEntry).getValue();
                        String fileName = file.getName();
                        fileBody = RequestBody.create(
                                MediaType.parse(guessMimeType(fileName)), file);
                        // TODO 根据文件名设置contentType
                        builder.addPart(
                                Headers.of("Content-Disposition", "form-data; name=\""
                                        + key.toString() + "\"; filename=\"" + fileName
                                        + "\""), fileBody);
                    }
                }
            }
            return builder.build();
        }

        private static String guessMimeType(String path) {
            FileNameMap fileNameMap = URLConnection.getFileNameMap();
            String contentTypeFor = fileNameMap.getContentTypeFor(path);
            if (contentTypeFor == null) {
                contentTypeFor = "application/octet-stream";
            }
            return contentTypeFor;
        }
    }
}
