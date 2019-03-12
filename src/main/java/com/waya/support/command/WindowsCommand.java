package com.waya.support.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.waya.support.util.Logger;


/**
 * <p>Windows命令操作
 * <p>通过此类可以正常执行Windows系统命令及执行exe等可执行文件,通过{@link Runtime#getRuntime()}来调用Windows方式实现.
 * <br>构造说明: 
 * <pre>
 * 	WindowsCommand lc = WindowsCommand.createWindowsCommand();
 * </pre>
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @see Runtime
 * @since 1.8
 * @version 1.0
 */
public class WindowsCommand extends Logger {
	
	private WindowsCommand() {};
	
	/**
	 * <p>静态构造函数,通过此方式创建对象
	 * @return LinuxCommand对象,从此对象中即可调用方法
	 */
	public static WindowsCommand createWindowsCommand() {
		return new WindowsCommand();
	}
	
	/**
	 * <p>调用Windows系统命令
	 * @param command 需要执行的命令
	 * @return 当调用失败时会返回null,调用成功后返回执行完毕的字符串,执行是否成功需要自行对字符串进行解析判断
	 */
	public String callingSys(String command) {
		String [] cmds = {"cmd", "/C", command};
		Runtime runtime = Runtime.getRuntime();
		try {
			StringBuilder result = new StringBuilder();
			Process process = runtime.exec(cmds);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				log.debug(line);
				result.append(line);
			}
			is.close();
			isr.close();
			br.close();
			return result.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
