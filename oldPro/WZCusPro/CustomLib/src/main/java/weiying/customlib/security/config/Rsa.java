package weiying.customlib.security.config;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class Rsa {
	public static final String RSA_PUBLICE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ4/3VqHevhEUwMvmkY/DizPUF/s2lhyXHp1AhZhpzMjFUMGFIppJyHhcg3/r1jTHo+RJOjiT4D0g4yameVcp3ELmhyQArde4+gAG9762Zk4eDU+MM4AtOG7jSQk23BFWtYYEtwjXhGCX+L37sMqDdDJd6dLnKD+ghzswVEwAlgwIDAQAB";
	private static final String ALGORITHM = "RSA";

	/**
	 * 得到公钥
	 * 
	 * @param algorithm
	 * @param bysKey
	 * @return
	 */
	private static PublicKey getPublicKeyFromX509(String algorithm,
                                                  String bysKey) throws NoSuchAlgorithmException, Exception {
		byte[] decodedKey = Base64.decode(bysKey, Base64.DEFAULT);
		X509EncodedKeySpec x509 = new X509EncodedKeySpec(decodedKey);

		KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
		return keyFactory.generatePublic(x509);
	}

	/**
	 * 使用公钥加密
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encryptByPublic(String content) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubkey);

			byte plaintext[] = content.getBytes("UTF-8");
			byte[] output = cipher.doFinal(plaintext);

			String s = new String(Base64.encode(output, Base64.DEFAULT));

			return s;

		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 使用公钥解密
	 * 
	 * @param content
	 *            密文
	 * @param key
	 *            商户私钥
	 * @return 解密后的字符串
	 */
	public static String decryptByPublic(String content) {
		try {
			PublicKey pubkey = getPublicKeyFromX509(ALGORITHM, RSA_PUBLICE);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, pubkey);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content,
					Base64.DEFAULT));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;
				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}
				writer.write(cipher.doFinal(block));
			}
			return new String(writer.toByteArray(), "utf-8");
		} catch (Exception e) {
			return null;
		}
	}

	public static String getMD5(String content) {
		String s = null;
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(content.getBytes());
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

}
