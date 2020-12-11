package expand.download2.utils;

import android.text.TextUtils;

import java.io.InputStream;
import java.security.MessageDigest;

/**
 * author：duff
 * version：1.0.0
 * date：2017/8/27
 */
public class MD5Utils {

    private static final int ROUND_8BITS = 0x100;
    private static final int ROUND_4BITS = 0x10;

    public static class MD5 {
        private static final String ALGORITHM = "MD5";

        /**
         * 对字符串进行md5加密处理
         *
         * @param string 原始字符串
         * @return 加密后的字符串 16位
         */
        public static String get16MD5String(String string) {
            byte[] md5 = stringToMD5(string);
            return to16HexString(md5);
        }

        /**
         * 对字符串进行md5加密处理
         *
         * @param string 原始字符串
         * @return 加密后的字符串 32位
         */
        public static String get32MD5String(String string) {
            byte[] md5 = stringToMD5(string);
            return to32HexString(md5);
        }

        private static byte[] stringToMD5(String string) {
            if (string == null) {
                return null;
            }
            try {
                MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
                digest.update(string.getBytes());
                return digest.digest();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private static String to16HexString(byte[] md5) {
            if (md5 == null) {
                return null;
            }
            int beginConvertIndex = 8;
            int endConvertIndex = 24;
            return to32HexString(md5).substring(beginConvertIndex, endConvertIndex);
        }

        public static String to32HexString(InputStream inputStream) {
            byte[] buffer = new byte[1024];
            int len;
            try {
                MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
                while ((len = inputStream.read(buffer)) != -1) {
                    digest.update(buffer, 0, len);
                }
                return MD5.to32HexString(digest.digest());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static String to32HexString(byte[] md5) {
            if (md5 == null) {
                return null;
            }

            int val;
            StringBuilder stringBuffer = new StringBuilder("");
            for (byte b : md5) {
                val = b;
                if (val < 0) {
                    val += ROUND_8BITS;
                }
                if (val < ROUND_4BITS) {
                    stringBuffer.append("0");
                }
                stringBuffer.append(Integer.toHexString(val));
            }

            return stringBuffer.toString();
        }
    }

    public static String toHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }

        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xff & bytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String toHexString(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }

        return toHexString(s.getBytes());
    }

}
