package weiying.customlib.security.security;


import weiying.customlib.security.util.MD5Utils;

public class Key {
	/**
	 * 密钥
	 */
	public static final String KEY = "$75k!xxH&$EhQLmv"; // 密钥
	/**
	 * 密钥偏移
	 */
	public static final String KEYIV = MD5Utils.md5(KEY).substring(0, 16); // 密钥偏移
}
