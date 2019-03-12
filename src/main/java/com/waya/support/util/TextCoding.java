/**
 * 
 */
package com.waya.support.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.waya.support.exception.MixStringParamsException;

/**
 * <p>对字符串的处理工具类，包含众多方法
 * <pre>
 * 如：
 * 	加密、混淆、摘要等
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.8
 * @time 2019年2月22日
 */
public class TextCoding {
	
	public static TextCoding newInstance() {
		return new TextCoding();
	}
	
	/**
	 * 根据逗号将byte字符串转常规字符串
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param s byte字符串
	 * @return 经过处理还原后的常规字符串
	 */
	public static String byteStrToStr(String s) {
		String[] sarr = s.split(",");
		byte[] b = new byte[sarr.length];
		for(int i = 0; i < sarr.length; i++) {
			b[i] = Byte.parseByte(sarr[i]);
		}
		return new String(b);
	}
	
	/**
	 * 根据自定义分割符将byte字符串转常规字符串
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param s byte字符串
	 * @param delimiter 分割符,用于分割字符串,请传入正确的分割符
	 * @return String 经过处理还原后的常规字符串
	 */
	public static String byteStrToStr(String s, String delimiter) {
		String[] sarr = s.split(delimiter);
		byte[] b = new byte[sarr.length];
		for(int i = 0; i < sarr.length; i++) {
			b[i] = Byte.parseByte(sarr[i]);
		}
		return new String(b);
	}
	
	/**
	 * 按照逗号","对字符串进行转byte拼接
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param s 原始字符串
	 * @return byteString 转码后的字符串
	 */
	public static String strToByteStr(String s) {
		byte[] b = s.getBytes();
		StringBuilder strb = new StringBuilder();
		for(byte c : b) {
			strb.append(c);
			strb.append(COMMA);
		}
		return strb.substring(0, strb.length() - 1);
	}
	
	/**
	 * 按照自定义分割符对字符串进行转byte拼接
	 * @auther HuTrace
	 * @time 2018年6月21日
	 * @param s 原始字符串
	 * @param delimiter 分割符
	 * @return byteString 转码后的字符串
	 */
	public static String strToByteStr(String s, String delimiter) {
		byte[] b = s.getBytes();
		StringBuilder strb = new StringBuilder();
		for(byte c : b) {
			strb.append(c);
			strb.append(delimiter);
		}
		return strb.substring(0, strb.length() - 1);
	}
	
	/**
	 * 转换字符串,把字符串从末尾依次倒序整理
	 * @auther HuTrace
	 * @time 2018年6月22日
	 * @param o 原始字符串
	 * @return String 倒序后的字符串
	 */
	public static String invert(String o) {
		String[] arr = o.split(EMPTY_STR);
		StringBuilder strb = new StringBuilder();
		for(int i = arr.length - 1; i >= 0; i --) {
			strb.append(arr[i]);
		}
		return strb.toString();
	}
	
	/**
	 * 对字符串按照sequentialLetter52形事进行混淆加密<br>
	 * 说明: 机密解密方法最先用replace方法实现,效率很低,改用此方式优化后提升3-4倍,后续优化方向,对indexOf处进行优化,是否有效?未测
	 * @param originalStr 原始字符串
	 * @return 密文
	 */
	public static String mixEncrypt(String originalStr) {
		char[] letter52 = mixRuleOriginal.toCharArray();
		char[] strArr = originalStr.toCharArray();
		StringBuffer strb = new StringBuffer();
		String cache;
		int index;
		for (int i = 0; i < strArr.length; i++) {
			cache = strArr[i] + EMPTY_STR;
			if(cache.equals(lowerA)) {
				strb.append(COMMA);
				continue;
			}
			index = mixRuleOriginal.indexOf(cache);
			if(index == -1) {
				strb.append(cache);
				continue;
			}
			strb.append(letter52[index - 1]);
		}
		return strb.toString();
	}
	
	/**
	 * 对字符串按照sequentialLetter52形事进行混淆加密的字符串进行解密
	 * @param str 密文
	 * @return 原始字符串
	 */
	public static String mixDecode(String str) {
		char[] letter52 = mixRuleOriginal.toCharArray();
		char[] strArr = str.toCharArray();
		StringBuffer strb = new StringBuffer();
		String cache;
		int index;
		for (int i = 0; i < strArr.length; i++) {
			cache = strArr[i] + EMPTY_STR;
			if(COMMA.equals(cache)) {
				strb.append(lowerA);
				continue;
			}
			index = mixRuleOriginal.indexOf(cache);
			if(index == -1) {
				strb.append(cache);
				continue;
			}
			strb.append(letter52[index + 1]);
		}
		return strb.toString();
	}
	
	/**
	 * 将字符串按照arg1的长度截取后并进行倒序排列
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static StringBuilder splitInvert(StringBuilder arg0, int arg1) {
		int in = (int) Math.ceil(arg0.length() * one / arg1);
		int last = in - 1;
		StringBuilder resultStrb = new StringBuilder();
		for (int i = 0; i < in; i++) {
			if(i == last) {
				resultStrb.append(invert(arg0.substring(i * arg1)));
			}else {
				resultStrb.append(invert(arg0.substring(i * arg1, (i + 1) * arg1)));
			}
		}
		return resultStrb;
	}
	
	/**
	 * 将字符串按照arg1的长度截取后并进行倒序排列
	 * @auther HuTrace
	 * @time 2018年6月23日
	 * @param arg0
	 * @param arg1
	 * @return 
	 */
	public static StringBuilder splitInvert(String arg0, int arg1) {
		int in = (int) Math.ceil(arg0.length() * one / arg1);
		int last = in - 1;
		StringBuilder resultStrb = new StringBuilder();
		for (int i = 0; i < in; i++) {
			if(i == last) {
				resultStrb.append(invert(arg0.substring(i * arg1)));
			}else {
				resultStrb.append(invert(arg0.substring(i * arg1, (i + 1) * arg1)));
			}
		}
		return resultStrb;
	}
	
	/**
	 * 多层混淆字符串,实现简单自定义加密(内部循环次数越多则复杂度越高)
	 * @param o 支持[a-zA-Z0-9]组合的字符串
	 * @return
	 */
	public static String mix(String o) {
		Pattern p = Pattern.compile(mixStringReg);
		String originalStr = o.toString();
		if(p.matcher(originalStr).matches()) {
			StringBuffer strb = new StringBuffer();
			String[] arr = originalStr.split(EMPTY_STR);
			String[] mixRuleNewArr = mixRuleNew.split(EMPTY_STR);
			for(String string : arr) {
				strb.append(mixRuleNewArr[mixRuleOriginal.indexOf(string)]);
			}
			for(int i = 0; i < 3; i++) {
				arr = strb.toString().split("");
				strb = new StringBuffer();
				for(String string : arr) {
					strb.append(mixRuleNewArr[mixRuleOriginal.indexOf(string)]);
				}
			}
			return strb.toString();
		}else {
			throw new MixStringParamsException("Parameter error: parameter range is [a-zA-Z0-9]");
		}
	}
	
	/**
	 * 将字符串按规则变成32位字符串
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param s
	 * @param len 变换成len长度的字符串
	 * @return
	 */
	public static String deRegleToLengt(String s, int len) {
		int length = s.length();
		if(length >= len) {
			return s.substring(0, len);
		}
		StringBuffer strb = new StringBuffer(s);
		String[] arr = s.split(EMPTY_STR);
		boolean flag = changeRule == 1 ? false : true;
		for(String str : arr) {
			if(flag) {
				flag = false;
				strb.append(str);
				if(strb.length() == len) {
					break;
				}
			}else {
				flag = true;
			}
		}
		return strb.toString();
	}
	
	/**
	 * 把字符串随机处理成len位字符串
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param s
	 * @param len 变换成len长度的字符串
	 * @return
	 */
	public static String randomToLength(String s, int len) {
		int length = s.length();
		if(length >= len) {
			return s.substring(0, len);
		}
		StringBuffer strb = new StringBuffer(s);
		Random random = new Random();
		int last = 0, next;
		while(true) {
			next = random.nextInt(10);
			if(last == next) {
				continue;
			}
			last = next;
			strb.append(last);
			if(strb.length() == len) {
				break;
			}
		}
		return strb.toString();
	}
	
	/**
	 * 按规则改变连续重复的字符
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param str
	 * @return
	 */
	public static String disposeofContinuousReprat(String str) {
		String[] arr = str.split(EMPTY_STR);
		String[] ruleArr32 = repeatRule32.split(EMPTY_STR);
		String temp = null, s;
		StringBuffer strb = new StringBuffer();
		for(int i = 0; i < arr.length; i++) {
			s = arr[i];
			if(s.equals(temp)) {
				strb.append(ruleArr32[i]);
			}else {
				temp = s;
				strb.append(s);
			}
		}
		return strb.toString();
	}
	
	/**
	 * 创建32位字符串 推荐使用{@link #createSign()}
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @return 32位字符串
	 */
	@Deprecated
	public static String createSign32() {
		String s = System.currentTimeMillis() + EMPTY_STR;
		String str32 = disposeofContinuousReprat((deRegleToLengt(mix(s), 32)));
		for(int i = 0; i < 6; i++) {
			str32 = disposeofContinuousReprat(disrupt(str32));
		}
		return capital32Md5(str32);
	}
	
	/**
	 * 创建32位字符串
	 * @Auther HuTrace
	 * @time 2018年6月27日
	 * @return 32位字符串
	 */
	public static String createSign() {
		String s = createNumberString(24);
		String str32 = disposeofContinuousReprat((deRegleToLengt(mix(s), 32)));
		for(int i = 0; i < 6; i++) {
			str32 = disposeofContinuousReprat(disrupt(str32));
		}
		return capital32Md5(str32);
	}
	
	/**
	 * 创建唯一的32位字符串(大写)
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @return 32位字符串
	 */
	public static String createCapitalRule32() {
		return capital32Md5(randomToLength(mix(System.currentTimeMillis() + EMPTY_STR), 32));
	}
	
	/**
	 * 创建唯一的32位字符串(小写)
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @return 32位字符串
	 */
	public static String createLowerRule32() {
		return lower32Md5(randomToLength(mix(System.currentTimeMillis() + EMPTY_STR), 32));
	}
	
	/**
	 * 创建唯一的12位字符串
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @return 12位字符串
	 */
	public static String createRule12() {
		Random random = new Random();
		String timestamp = System.currentTimeMillis() + EMPTY_STR;
		StringBuilder strb = new StringBuilder(timestamp);
		//循环打乱时间戳的顺序,此步骤可以取消,无影响
		for (int i = 0; i < 3; i++) {
			strb = splitInvert(strb, random.nextInt(10) + 1);
		}
		String random24 = randomToLength(strb.toString(), 24);
		return numeralToRuleLengthDivideTwo(random24);
	}
	
	/**
	 * <p>通过传入的length创建length长度的数字字符串
	 * <p>此方法是通过系统时间戳拼接随机数字的方式进行创建,当length小于等于13时,
	 * 不能保证唯一性,当length大于13时或更大时,
	 * 可以保证唯一性为99.999999999%,理论上是不会出现重复的情况
	 * @param length 创建length长度的数字字符串
	 * @return 创建后的字符串
	 */
	public static String createNumberString(int length) {
		return randomToLength(System.currentTimeMillis() + EMPTY_STR, length);
	}
	
	/**
	 * 创建64位唯一字符串(理论上唯一(/ω＼))
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @return 64位唯一字符串
	 */
	public static String createCode64() {
		StringBuffer result = new StringBuffer();
		result.append(System.currentTimeMillis());
		char[] arrs = new char[51];
		Double random;
		for(int i = 0; i < arrs.length; i++) {
			random = Math.random()*10;
			arrs[i] = randomArr[random.intValue()];
			if(i != 0) {
				if(arrs[i] == arrs[i-1]) {
					i = i - 1;
					continue;
				}
			}
			result.append(randomArr[random.intValue()]);
		}
		return changeCode(result.toString());
	}
	
	/**
	 * 将数字字符串简单转换成字母
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param c
	 * @return
	 */
	public static String changeCode(String c) {
		StringBuffer r = new StringBuffer();
		char[] arr = c.toCharArray();
		Double random;
		int a;
		for(int i = 0; i < arr.length; i++) {
			if(i%2 == 0) {
				r.append(arr[i]);
				continue;
			}
			a = Integer.parseInt(arr[i]+"");
			random = a == 9 ? Math.random() * 2 : Math.random() * 1;
			r.append(alphabet[a * (random.intValue() + 1)]);
		}
		return r.toString();
	}
	
	/**
	 * 数字字符串安装规则转成新的字符串,并且原始长度的串长度会少一倍
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param numeralString 数字字符串(只能是数字字符串,长度必须为双数为)
	 */
	public static String numeralToRuleLengthDivideTwo(String numeralString) {
		if(numeralString.length() % 2 != 0) {
			throw new RuntimeException("参数错误,参数长度必须为双数位");
		}
		if(!numeralString.matches("[0-9]+")) {
			throw new RuntimeException("参数错误,参数必须为纯数字字符串");
		}
		int len2 = numeralString.length() / 2;
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < len2; i++) {
			strb.append(deRegleDoubleDigitToOneString(numeralString.substring(i*2, i*2 + 2)));
		}
		return strb.toString();
	}
	
	/**
	 * 2位数字字符串转换成1位字符串
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param doubleDigit 2位数字字符串,String类型
	 * @return 1位字符串
	 */
	private static String deRegleDoubleDigitToOneString(String doubleDigit) {
		return deRegleDoubleDigitToOneString(Integer.parseInt(doubleDigit));
	}
	
	/**
	 * 2位数字转换成1位字符串
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param doubleDigit 2位数字,int类型
	 * @return 1位字符串
	 */
	public static String deRegleDoubleDigitToOneString(int doubleDigit) {
		return doubleDigitToOneStringRule[doubleDigit];
	}
	
	/**
	 * 对简单混淆字符串进行打乱排序
	 * 规则为: 提起偶数位倒序拼接上基数位倒序
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param s 原始字符串
	 * @return 打乱后的字符串
	 */
	public static String disrupt(String s) {
		char[] arr = s.toCharArray();
		StringBuilder odd = new StringBuilder();//基数位
		StringBuilder even = new StringBuilder();//偶数位
		for(int i = 0; i < arr.length; i++) {
			if(i%2 == 0) {
				odd.append(arr[i]);
			}else {
				even.append(arr[i]);
			}
		}
		StringBuilder strb = new StringBuilder();
		strb.append(invert(even.toString()));
		strb.append(invert(odd.toString()));
		return strb.toString();
	}
	
	/**
	 * 对字符串进行md5加密（小写32位）
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param str
	 * @return
	 */
	public static String lower32Md5(String str) {
		try {
			return DigestUtils.md5Hex(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str;
		}
	}
	
	/**
	 * 对字符串进行md5加密（大写32位）
	 * @param str
	 * @return
	 */
	public static String capital32Md5(String st) {
		return lower32Md5(st).toUpperCase();
	}
	
	/**
	 * 密码加密
	 * @auther HuTrace
	 * @time 2018年6月27日
	 * @param pwd
	 * @param salt
	 * @return
	 */
	public static String passwordEncrypt(String pwd, String salt) {
		return capital32Md5(strSplice(pwd, salt));
	}
	
	/**
	 * 拼接字符串
	 * @auther HuTrace
	 * @time 2018年7月9日
	 * @param str
	 * @return
	 */
	public static String strSplice(String... str) {
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			strb.append(str[i]);
		}
		return strb.toString();
	}
	
	public static String strSplice(Object... str) {
		StringBuilder strb = new StringBuilder();
		for (int i = 0; i < str.length; i++) {
			strb.append(str[i]);
		}
		return strb.toString();
	}
	
	/**
	 * <p>sha256 摘要
	 * @param str 需要摘要的字符
	 * @return 生成的摘要字符
	 */
	public static String sha256(String str) {
		return new SHA2().digest(str);
	}
	
	/** 逗号","*/
	public static final String COMMA = ",";
	/** 点"."*/
	public static final String DOT = ".";
	/** 混淆字符串,包含26位大小写字母加上0-9的数字*/
	public static String mixRuleOriginal = "a0bcdef8ghijk5lmnop2qrstuvwx4yzABCD6EFGHIJK9LMNOP7QRSTU1VWX3YZ";
	/** 小写字母A*/
	public static String lowerA = "a";
	/** 空字符串*/
	public final static String EMPTY_STR = "";
	/** 空字符串*/
	public final static String NULL_STR = "null";
	/** 空字符串*/
	public final static String UNDEFINED_STR = "undefined";
	/** 空格字符串*/
	public final static String SPACE_CHAR = " ";
	/** 1.0*/
	public static double one = 1.0;
	/**  混淆字符串规则*/
	private static String mixStringReg = "([0-9]|[A-Za-z])+";
	/** 多层自定义混淆的替换规则*/
	private static String mixRuleNew = "QW0mnEbRv1TYcxzU2IOPlkj3hASD4gFGfdHJsKLa8qMpZowXeiN9ur6Byt5V7C";
	/** 字符串补足指定位数时进行补足的规则,1代表提取奇数位,0代表提取偶数位(注:从0开始)*/
	private static int changeRule = 1;
	/** 去32位字符串连续重复规则*/
	private static String repeatRule32 = "JGa5cR8Vs9Ll2fM4n6rW0p1Ty7YaF3uB";
	/** 16进制*/
	static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	/** 两位数字符串转成一位字符串的规则*/
	private static String[] doubleDigitToOneStringRule = "qRgr7it2D*TsvkezNKJHLwamnfjyoCdMAPlhO6EbX^u;QF:85p3@#Y‰S~W/]U4x}√°!$%B(Z&)’″[_1c-○V0{G9,<±I?+÷∞=`>×.".split(EMPTY_STR);
	/** 随机数char数组*/
	private static char[] randomArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	/** 小写字母数字*/
	private static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
}
