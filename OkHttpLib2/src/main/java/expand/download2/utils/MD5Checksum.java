/*
 * MD5Checksum      2016-01-22
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 * 
 */
package expand.download2.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public abstract class MD5Checksum {

    private static final int BUFFER = 1024;

    /**
     * 获取默认的MD5检测类{@link DefaultMD5Checksum}
     *
     * @return
     */
    public static MD5Checksum getDefault() {
        return new DefaultMD5Checksum();
    }

    public boolean check(String md5, InputStream inputStream) {
        if (TextUtils.isEmpty(md5) || inputStream == null) {
            return false;
        }

        byte[] buffer = new byte[BUFFER];
        int len;
        try {
            MessageDigest digest = messageDigest();
            while ((len = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            String target = makeMD5String(digest.digest()).toLowerCase();
            return md5.toLowerCase().equals(target);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 通过MD5验证文件的完整性
     *
     * @param md5
     * @param toCheckFile
     * @return
     */
    public boolean check(String md5, File toCheckFile) {
        if (TextUtils.isEmpty(md5) || toCheckFile == null) {
            return false;
        }

        try {
            return check(md5, new FileInputStream(toCheckFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 通过MD5验证文本的完整性
     *
     * @param md5
     * @param str
     * @return
     */
    public boolean check(String md5, String str) {
        if (TextUtils.isEmpty(md5) || TextUtils.isEmpty(str)) {
            return false;
        }

        return check(md5, str.getBytes());
    }

    /**
     * 通过MD5验证二进制数据的完整性
     *
     * @param md5
     * @param bytes
     * @return
     */
    public boolean check(String md5, byte[] bytes) {
        if (TextUtils.isEmpty(md5) || bytes == null) {
            return false;
        }

        try {
            MessageDigest digest = messageDigest();
            digest.update(bytes);
            String md5Str = makeMD5String(digest.digest()).toLowerCase();

            return md5.toLowerCase().equals(md5Str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 需要根据业务需要继承实现MD5码生成的逻辑
     *
     * @param md5Bytes md5二进制数据
     * @return
     */
    protected abstract String makeMD5String(byte[] md5Bytes);

    private MessageDigest messageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

    private static class DefaultMD5Checksum extends MD5Checksum {

        @Override
        protected String makeMD5String(byte[] md5Bytes) {
            return MD5Utils.MD5.to32HexString(md5Bytes);
        }
    }
}