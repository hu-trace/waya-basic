package com.waya.support.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/**
 * <p>
 * 对称字符编码器
 * 
 * <pre>
 * 1.含有AES对称加/解密方法
 * 
 * </pre>
 * 
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @see
 * @since 1.8
 * @version 1.0
 * @time 2018年12月12日
 */
public class SymmetricEncoder {

	private static final String CHARSET = "UTF-8";
	private static final String ALGORITHM = "AES";
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	/**
	 * <p>加密
	 * <pre>
	 * 1.构造密钥生成器
	 * 2.根据ecnodeRules规则初始化密钥生成器 
	 * 3.产生密钥 
	 * 4.创建和初始化密码器 
	 * 5.内容加密
	 * 6.返回字符串
	 * <pre>
	 * @param secret 加解密的密钥
	 * @param content 内容
	 * @return 加密后的字符串
	 */
	public static String aesEncode(String secret, String content) {
		try {
			// 构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			keygen.init(128);
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			// 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), ALGORITHM));
			// 根据密码器的初始化方式--加密：将数据加密
			byte[] byteAes = cipher.doFinal(content.getBytes(CHARSET));
			return new String(Base64.encodeBase64(byteAes), CHARSET);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 如果有错就返加nulll
		return null;
	}

	/**
	 * <p>解密 
	 * <p>解密过程： 
	 * <pre>
	 * 1.同加密1-4步 
	 * 2.将加密后的字符串反纺成byte[]数组 
	 * 3.将加密内容解密
	 * </pre>
	 */
	public static String aesDncode(String secret, String content) {
		try {
			// 构造密钥生成器，指定为AES算法,不区分大小写
			KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secret.getBytes());
			// 生成一个128位的随机源,根据传入的字节数组
			keygen.init(128, secureRandom);
			// 根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), ALGORITHM));
			// 将加密并编码后的内容解码成字节数组 并 解密
			byte[] byteDecode = cipher.doFinal(Base64.decodeBase64(content));
			return new String(byteDecode, CHARSET);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		// 如果有错就返加null
		return null;
	}

}
