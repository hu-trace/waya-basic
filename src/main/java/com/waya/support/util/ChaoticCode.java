/**
 * 
 */
package com.waya.support.util;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * <p>对字符串进行混乱的加密
 * <pre>
 * 加密后字符串长度会增加很多
 *  非中文字符串增加3-4倍长度
 *  中文字符串增加8-15倍长度
 * </pre>
 * <b>采用自定义混乱形式加密，相同的字符串每次加密的结果都会不同</b>
 * <pre>
 * 使用列子：
 *  加密：ChaoticCode.build(str).toString();
 *  解密：ChaoticCode.build(str).string();
 * 在过程中也可以进行字符串拼接
 *  ChaoticCode.build(str).append(obj);
 * </pre>
 * <b>注意：因采用头部加长随机数方式，需要加密的字符串第一个字符不能为数字，否则会执行解密流程</b>
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.8
 * @version 1.0
 * @time 2019年2月22日
 */
public final class ChaoticCode {
	
	/** 常规字符串*/
	private StringBuilder string;
	
	/** 加密后字符串的长度*/
	private int encryptStrLength;
	
	/** 加密后但未混淆的字符串*/
	private StringBuilder encryptStr;
	
	/** 截取倒序后的字符串*/
	private StringBuilder splitInvertStr;
	
	/** 是否是此类字符串*/
	private boolean isWaString;
	
	/** 32位随机整数字符串*/
	private String random10;
	
	/** 验证是否为此类的关键字符,用于取值进行验证,是否为此类的字符,并对字符串进行解密*/
	private String verifyChar;

	/** 混淆加密时的截断位数*/
	private int oneMixLimit;
	
	/** 混淆解密时的截断位数*/
	private int multiMixLimit;
	
	public static ChaoticCode build(String str) {
		return new ChaoticCode(str);
	}
	
	/**
	 * <p>创建对象
	 * @param str 字符串(加密or解密)
	 * @param length 验证头部数字的长度
	 * @return {@link ChaoticCode}
	 */
	public static ChaoticCode build(String str, int length) {
		return new ChaoticCode(str, length);
	}
	
	/**
	 * 构造方法,进行初始化类,判断字符串是否是本类型的字符串,若为本类型的字符串,则解密
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @param str
	 */
	public ChaoticCode(String str) {
		splitInvertStr = new StringBuilder();
		string = new StringBuilder();
		string.append(str);
		isWaString(str);
		init();
	}
	
	private ChaoticCode(String str, int length) {
		this.ten = length;
		splitInvertStr = new StringBuilder();
		string = new StringBuilder();
		string.append(str);
		isWaString(str);
		init();
	}
	
	/**
	 * 初始化方法
	 * @auther HuTrace
	 * @time 2018年6月23日
	 */
	private void init() {
		encryptStr = new StringBuilder();
		//先验证是否为此类字符串,如果是,直接进行解密赋值,如果不是,进行加密赋值
		if(!isWaString) {//加密
			random10 = randomNumber(ten);
			verifyChar = random10.split("")[randomNextInt(ten)];
			encrypt(string.toString());
		}else {//还原
			splitInvertStr = restoreMix(string.substring(ten, string.length()));
			encryptStrLength = splitInvertStr.length();
			int splitLength = splitInvertStr.length() < 100 ? 5 : 50;
			encryptStr = TextCoding.splitInvert(splitInvertStr, splitLength);
			decode();
		}
	}
	
	/**
	 * 验证字符串是加密后的字符串还是原始字符串
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @param str
	 */
	private void isWaString(String str) {
		if(str.length() < ten) {
			isWaString = false;
			return;
		}
		String randomEncrypt = str.substring(0, ten);
		if(!randomEncrypt.matches("[0-9]+")) {
			isWaString = false;
			return;
		}
		isWaString = checkIs(string.substring(ten, string.length()));
		isWaString = true;
		random10 = randomEncrypt;
	}
	
	/**
	 * 验证混淆规则,如果符合则为加密后得子符串
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @param arg
	 * @return
	 */
	private boolean checkIs(String arg) {
		oneMixLimit = arg.length() < cutoff ? minMixSplit : maxMixSplit;
		multiMixLimit = oneMixLimit + 1;
		int argL = arg.length();
		if(argL < multiMixLimit) {
			if(arg.substring(arg.length() - 1).matches("[0-9]+")) {
				return true;
			}
			return false;
		}
		int in = (int) Math.ceil(arg.length() * TextCoding.one / multiMixLimit);
		String check = "", cacha;
		for (int i = 0; i < in; i++) {
			if(i == in - 1) {
				cacha = arg.substring(arg.length() - 1);
				if(!check.equals(cacha)) {
					return false;
				}
			}else {
				if("".equals(check)) {
					check = arg.substring(((i + 1) * multiMixLimit) - 1, (i + 1) * multiMixLimit);
					if(!check.matches("[0-9]+")) {
						return false;
					}
				}else {
					cacha = arg.substring(((i + 1) * multiMixLimit) - 1, (i + 1) * multiMixLimit);
					if(!check.equals(cacha)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void append(String arg) {
		string.append(arg);
		encrypt(arg);
	}
	
	public void append(int arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	public void append(char arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	public void append(boolean arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	public void append(float arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	public void append(long arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	public void append(byte arg) {
		string.append(arg);
		encrypt(arg + TextCoding.EMPTY_STR);
	}
	
	/**
	 * 获取随机整数
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param arg 随机数的长度
	 * @return String 随机整数字符串
	 */
	public static String randomNumber(int arg) {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < arg; i++) {
			Random ran = new Random();
			stringBuilder.append(ran.nextInt(9) + 1);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 获取arg内的一个随机数
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param arg 
	 * @return 随机数
	 */
	public static int randomNextInt(int arg) {
		return new Random().nextInt(arg);
	}
	
	/**
	 * 取得加密后的字符串并混淆后进行返回
	 * @auther HuTrace
	 * @time 2018年6月22日
	 */
	public String wyString() {
		return toString();
	}
	
	@Override
	public String toString() {
		oneMixLimit = encryptStrLength < cutoff ? minMixSplit : maxMixSplit;
		multiMixLimit = oneMixLimit + 1;
		return new StringBuilder(random10).append(multiMix(splitInvertStr)).toString();
	}
	
	/**
	 * 对字符串进行转换混淆加密
	 * <br>每次append数据后需要对加密字段进行更新
	 * @auther HuTrace
	 * @time 2018年6月22日
	 */
	private void encrypt(String str) {
		//调用父类的字符串转byteStr方法进行简单转换并去掉负号与逗号,并对其进行混淆加密方式加密字符串
		encryptStr.append(TextCoding.mixEncrypt(strToByteStr(str)));
		encryptStrLength = encryptStr.length();
		int splitLength = encryptStr.length() < 100 ? 5 : 50;
		splitInvertStr = TextCoding.splitInvert(encryptStr, splitLength);
	}
	
	/**
	 * 解密字符串
	 * @auther HuTrace
	 * @time 2018年6月22日
	 */
	private void decode() {
		string = new StringBuilder();
		string.append(byteStrToStr(TextCoding.mixDecode(encryptStr.toString())));
	}
	
	/**
	 * 取得常规字符串
	 * @auther HuTrace
	 * @time 2018年6月22日
	 * @return
	 */
	public String string() {
		return string.toString();
	}
	
	/**
	 * 获取常规字符串的长度
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @return
	 */
	public int length() {
		return string.length();
	}
	
	/**
	 * 对字符串进行多个混淆
	 * @auther HuTrace
	 * @time 2018年6月22日
	 * @param strbLength
	 * @return
	 */
	private StringBuilder multiMix(StringBuilder byteStr) {
		return byteStr;
	}
	
	/**
	 * 对进行多个混淆的字符串还原
	 * @auther HuTrace
	 * @time 2018年6月22日
	 * @param strbLength
	 * @return
	 */
	private StringBuilder restoreMix(String str) {
		return new StringBuilder(str);
	}
	
	/** 
	 * 重写{@link TextCoding}转换方法,在转换的过程中将：
	 * <br>负数符号随机变为26个小写字母
	 * <br>逗号随机变为26个大写字母
	 */
	public String strToByteStr(String s) {
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		StringBuilder strb = new StringBuilder();
		String bys;
		for(byte c : b) {
			bys = c + "";
			if(bys.substring(0, 1).equals(minus)){
				strb.append(lowerChar26[randomNextInt(26)]);
				strb.append(bys.substring(1, bys.length()));
			}else {
				strb.append(c);
			}
			strb.append(capitalChar26[randomNextInt(26)]);
		}
		return strb.toString();
	}
	
	/**
	 * 重写{@link TextCoding}转换方法,在转换的过程中将：
	 * <br>按照大写字母才分字符串
	 * <br>将小写字母转换为"-"
	 */
	public String byteStrToStr(String s) {
		String[] sarr = s.split("[A-Z]+");
		byte[] b = new byte[sarr.length];
		String lowerString26 = String.valueOf(lowerChar26);
		String cacha;
		int a = 0;
		if(sarr[0].equals(TextCoding.EMPTY_STR)) {
			a = 1;
		}
		for(int i = a; i < sarr.length; i++) {
			cacha = sarr[i];
			b[i] = lowerString26.indexOf(cacha.substring(0, 1)) > -1 ? 
					Byte.parseByte(minus + cacha.substring(1, cacha.length())) : Byte.parseByte(cacha);
		}
		return new String(b, StandardCharsets.UTF_8);
	}
	
	/** 获取关键字符*/
	public String verifyChar() {
		return verifyChar;
	}

	/** 10*/
	private int ten = 21;
	/** 最小分割位数*/
	private int minMixSplit = 5;
	/** 最大分割位数*/
	private int maxMixSplit = 50;
	/** 分隔使用最大或最小分割位数的值*/
	private int cutoff = 100;
	/** String类型的减号*/
	private String minus = "-";
	/** 32位小写字母的数组*/
	private char[] lowerChar26 = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
	/** 32位大写字母的数组*/
	private char[] capitalChar26 = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	
}
