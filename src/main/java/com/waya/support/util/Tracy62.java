package com.waya.support.util;

/**
 * <p>Tracy62，按照Tracy规定生成指定的62进制，可以加减乘除计算
 * <p>初衷，设定短位数的唯一字符串，量大，至少要满足所有long的最大长度需求，
 * <p>使用long的缺点：1、展示时非常的不安全，2、太长
 * <p>采用0-9、a-z、A-Z、凑成62位字符
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.7
 * @version 1.0
 * @time 2018年11月14日
 */
public class Tracy62 {
	
	public static Tracy62 build(long number) {
		Tracy62 t = new Tracy62(number, null);
		t.analysis();
		return t;
	}
	
	public static Tracy62 build(String tracy62) {
		Tracy62 t = new Tracy62(0, tracy62);
		t.analysis();
		return t;
	}
	/**
	 * <p>初始化Tracy62内部参数
	 * @param number 数字
	 * @param tracy62 {@link Tracy62}字符
	 */
	private Tracy62(long number, String tracy62) {
		if(null == tracy62) {
			this.number = number;
			isTracy62 = false;
		}else {
			this.tracy62 = tracy62;
			isTracy62 = true;
		}
	}
	
	@Override
	public String toString() {
		return tracy62;
	}
	
	public long toLong() {
		return number;
	}
	
	/**
	 * <p>解析数字或者字符
	 */
	public void analysis() {
		if(isTracy62) {
			restore();
		}else {
			transform();
		}
	}
	
	/**
	 * <p>对{@link Tracy62}字符还原成Number类型
	 */
	public void restore() {
		char[] arr = tracy62.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			try {
				int index = 0;
				for (int xx = 0; xx < SUPPORT_CHAR.length; xx++) {
					if (SUPPORT_CHAR[xx] == arr[arr.length - i - 1]) {
						index = xx;
					}
				}
				number += (long) Math.pow(system, i) * index;// 2
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * <p>把10进制数字转换成Tracy62字符
	 */
	public void transform() {
		int digitIndex = 0;
		char[] outDigits = new char[system + 1];
		for (digitIndex = 0; digitIndex <= system; digitIndex++) {
			if (number == 0) {
				break;
			}
			outDigits[outDigits.length - digitIndex - 1] = SUPPORT_CHAR[(int) (number % system)];
			number /= system;
		}
		tracy62 = new String(outDigits, outDigits.length - digitIndex, digitIndex);
	}
	
	/**
	 * <p>初始化时是否是{@link Tracy62}字符串
	 */
	private boolean isTracy62;
	/**
	 * <p>原始数字
	 */
	private long number;
	/**
	 * <p>{@link Tracy62}字符串
	 */
	private String tracy62;
	
	/**
	 * <p>进制数
	 */
	private int system = 62;
	
	/**
	 * <p>作为底层支撑的62个字符
	 */
	private static final char[] SUPPORT_CHAR = {
			'Q','W','0','m','n','E','b','R','v','1','T','Y','c',
			'x','z','U','2','I','O','P','l','k','j','3','h','A',
			'S','D','4','g','F','G','f','d','H','J','s','K','L',
			'a','8','q','M','p','Z','o','w','X','e','i','N','9',
			'u','r','6','B','y','t','5','V','7','C'};

}
