package com.waya.support.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @classExplain 序列化对象,序列化与反序列化
 * @author HuTrace
 * @javav JDK1.7
 * @version 1.0
 * @addTime 2016年8月23日
 * @fileName Serialize.java
 */
public class Serializer {
	
	private static Logger log = LoggerFactory.getLogger(Serializer.class);
	private static final String FILE_SUFFIX = ".cat";
	
	/**
	 * @methodExplain 序列化对象至本地
	 * @author HuTrace
	 * @param {@link Object} obj
	 * @param {@link String} file
	 */
	public static void serialize(Object obj, String file) {
		try {
			ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(file + FILE_SUFFIX));
			oo.writeObject(obj);
			oo.close();
			log.info("序列化对象成功...");
		} catch (FileNotFoundException e) {
			log.error("文件夹不存在...: " + file);
		} catch (IOException e) {
			log.error("序列化错误...: " + file);
		}
	}
	
	/**
	 * <p>反序列化
	 * @param file {@link String} file
	 * @return 反序列化的对象
	 */
	public static Object unSerialize(String file) {
		ObjectInputStream oi = null;
		try {
			oi = new ObjectInputStream(new FileInputStream(new File(file + FILE_SUFFIX)));
			return oi.readObject();
		} catch (FileNotFoundException e) {
			log.error("文件不存在: " + file);
		} catch (IOException e) {
			log.error("反序列化错误: " + file);
		} catch (ClassNotFoundException e) {
			log.error("没有找到相应的类: " + file);
		} finally {
			try {
				oi.close();
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * @methodExplain 删除序列化文件
	 */
	public static void delSerialize(String file) {
		File f = new File(file + FILE_SUFFIX);
		if(f.exists() && f.isFile()) {
			f.delete();
		}
	}

}
