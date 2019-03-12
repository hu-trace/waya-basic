package com.waya.support.util;

import org.slf4j.LoggerFactory;

/**
 * <p>公共日志类
 * <p>通过继承直接使用
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @see org.slf4j.Logger
 * @since 1.8
 * @version 1.0
 * @time 2018年8月7日
 */
public class Logger {
	
	protected org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());
	
}
