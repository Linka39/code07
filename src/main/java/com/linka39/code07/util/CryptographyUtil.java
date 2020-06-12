package com.linka39.code07.util;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密工具
 * @author Administrator
 *
 */
public class CryptographyUtil {

	public final static String SALT="Linka39"; // 加密的盐

	/**
	 * Md5加密,全局静态方法
	 * @param str
	 * @param salt
	 * @return
	 */
	public static String md5(String str,String salt){
		return new Md5Hash(str,salt).toString();
	}
	public static String decryMd5(String str,String salt){
		return new Md5Hash(str,salt).toString();
	}

	public static void main(String[] args) {
		String password="0818";
		String encrypPass=CryptographyUtil.md5(password, SALT);
		System.out.println("Md5加密："+encrypPass);
		System.out.println("解密密码："+CryptographyUtil.decryMd5(password, SALT));
		System.out.println("解密密文："+CryptographyUtil.decryMd5(encrypPass, SALT));
	}
}
