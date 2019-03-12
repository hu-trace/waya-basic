package com.waya.support.util;


import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 *
 */
/**
 * 
 * <p>非对称加密算法RSA算法组件 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要大费周章的构造各自本地的密钥对了。
 * <p>DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @see 
 * @since 1.8
 * @version 1.0
 * @time 2018年12月14日
 */
public class RSA {
	// 非对称密钥算法
	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
	 */
	private static final int KEY_SIZE = 1024;
	private static final String CHARSET = "UTF-8";
	// 公钥
	private static final String PUBLIC_KEY = "RSAPublicKey";
	// 私钥
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 初始化密钥对
	 * @return Map 甲方密钥的Map
	 */
	public static Map<String, Object> initKey() throws Exception {
		// 实例化密钥生成器
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		// 初始化密钥生成器
		keyPairGenerator.initialize(KEY_SIZE);
		// 生成密钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 甲方公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 甲方私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 将密钥存储在map中
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	/**
	 * 私钥加密
	 * @param content 待加密数据
	 * @param key 密钥
	 * @return String 加密数据
	 */
	public static String encryptByPrivateKey(String content, String key) throws Exception {
		byte[] byt = encryptByPrivateKey(content.getBytes(CHARSET), Base64.decodeBase64(key));
		return Base64.encodeBase64String(byt);
	}

	/**
	 * 私钥加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 公钥加密
	 * @param content 待加密数据
	 * @param key 密钥
	 * @return String 加密数据
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	public static String encryptByPublicKey(String content, String key) throws Exception {
		byte[] byt = encryptByPublicKey(content.getBytes(CHARSET), Base64.decodeBase64(key));
		return Base64.encodeBase64String(byt);
	}
	
	/**
	 * 公钥加密
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 私钥解密
	 * @param content 待解密数据
	 * @param key 密钥
	 * @return String 解密数据
	 */
	public static String decryptByPrivateKey(String content, String key) throws Exception {
		byte[] byt = decryptByPrivateKey(Base64.decodeBase64(content), Base64.decodeBase64(key));
		return new String(byt, CHARSET);
	}
	
	/**
	 * 私钥解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	
	/**
	 * 公钥解密
	 * @param content 待解密数据
	 * @param key 密钥
	 * @return String 解密数据
	 */
	public static String decryptByPublicKey(String content, String key) throws Exception {
		byte[] byt = decryptByPublicKey(Base64.decodeBase64(content), Base64.decodeBase64(key));
		return new String(byt, CHARSET);
	}

	/**
	 * 公钥解密
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 取得私钥
	 * @param keyMap 密钥map
	 * @return byte[] 私钥
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * @param keyMap 密钥map
	 * @return byte[] 公钥
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

}
