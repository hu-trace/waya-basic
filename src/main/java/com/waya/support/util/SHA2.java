package com.waya.support.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>SHA256摘要算法
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.8
 * @version 1.0
 * @time 2018年12月12日
 */
public class SHA2 {
	
	/**
	 * <p>摘要算法
	 * @param str 需要摘要的字符串
	 * @return 摘要后的字符串
	 */
	public String digest(String str) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));
			return byte2Hex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String byte2Hex(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF);
			if (temp.length() == 1) {
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}
	
}
