package com.waya.support.extend;

import java.text.SimpleDateFormat;

import com.waya.support.exception.DateInputException;

/**
 * <p>扩展{@link java.util.Date},让其支持传入字符串构建时间
 * <pre>
 * 	eg: java.util.Date = new com.hutrace.api.date.HTDate("2000-01-10").getDate();
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @see java.util.Date
 * @since 1.8
 * @time 2019年2月22日
 */
public class Date {
	
	public static Date getInterface(String date) throws DateInputException {
		return new Date(date);
	}
	
	/**
	 * 使用: 
	 * <br>调用getDate方法获取,
	 * <br>eg: java.util.Date = new com.hutrace.api.date.HTDate().getDate();
	 * @author HuTrace
	 * @Time 2017年3月14日
	 * @param str 传入字符串说明,
	 * <br>yyyy-MM-dd HH:mm:ss格式或者yyyy/MM/dd HH:mm:ss格式
	 * <br>参数位数最大可为年,最小为秒
	 * @throws DateInputException
	 */
	private Date(String str) throws DateInputException {
		if(null == str) {
			date = new java.util.Date();
			return;
		}
		length = str.length();
		if(length == 0) {
			date = new java.util.Date();
			return;
		}
		if(length == 4) {
			format = "yyyy";
		}else if(length > 4){
			index = str.indexOf('-');
			if(index > -1) 
				istype = 0;
			else {
				index = str.indexOf('/');
				if(index > -1) {
					istype = 1;
				}else {
					throw new DateInputException("Incoming parameter error");
				}
			}
			try {
				switch (istype) {
					case 0:
						switch (length) {
							case 7:
								format = "yyyy-MM";
								break;
							case 10:
								format = "yyyy-MM-dd";
								break;
							case 13:
								format = "yyyy-MM-dd HH";
								break;
							case 16:
								format = "yyyy-MM-dd HH:mm";
								break;
							case 19:
								format = "yyyy-MM-dd HH:mm:ss";
								break;
							default:
								throw new DateInputException("Incoming parameter error");
						}
						break;
					case 1:
						switch (length) {
							case 7:
								format = "yyyy/MM";
								break;
							case 10:
								format = "yyyy/MM/dd";
								break;
							case 13:
								format = "yyyy/MM/dd HH";
								break;
							case 16:
								format = "yyyy/MM/dd HH:mm";
								break;
							case 19:
								format = "yyyy/MM/dd HH:mm:ss";
								break;
							default:
								throw new DateInputException("Incoming parameter error");
						}
						break;
					default:
						throw new DateInputException("Incoming parameter error");
				}
			} catch (Exception e) {
				throw new DateInputException("Incoming parameter error");
			}
		}else {
			throw new DateInputException("length error: " + length);
		}
		try {
			sdf = new SimpleDateFormat(format);
			date = sdf.parse(str);
		}catch (Exception e) {
			e.printStackTrace();
			throw new DateInputException("Incoming parameter error");
		}
	}
	
	public Date() {
		date = new java.util.Date();
	}
	
	public java.util.Date getDate() {
		return date;
	}
	
	private String format;
	
	private int istype;
	
	private int index, length;
	
	private SimpleDateFormat sdf;
	
	private java.util.Date date;
	
}
