package com.waya.support.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import com.waya.support.command.LinuxCommand;
import com.waya.support.command.WindowsCommand;

/**
 * <p>获取本机的IP地址
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @see LinuxCommand
 * @see InetAddress
 * @since 1.8
 * @version 1.0
 */
public class GetMeIp extends Logger {
	
	/**
	 * <p>获取外网地址
	 * <p>通过发送命令的方式请求icanhazip.com
	 * @return 外网IP地址
	 */
	public String outerNet() {
		// 获取操作系统类型
		String sysType = System.getProperties().getProperty("os.name");
		if (sysType.toLowerCase().startsWith("win")) { // 如果是Windows系统，获取本地IP地址
			return WindowsCommand.createWindowsCommand().callingSys("curl icanhazip.com");
		} else {
			return LinuxCommand.createLinuxCommand().callingSys("curl icanhazip.com");
		}
	}
	
	/**
	 * <p>获取内网地址
	 * <p>通过JAVA内置API获取
	 * @return 内网IP地址
	 */
	public String intranet() {
		// 获取操作系统类型
		String sysType = System.getProperties().getProperty("os.name");
		if (sysType.toLowerCase().startsWith("win")) { // 如果是Windows系统，获取本地IP地址
			String localIP = null;
			try {
				localIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return localIP;
		} else {
			return getIpByEth0(); // 兼容Linux
		}
	}

	/**
	 * <p>根据网络接口获取LinuxIP地址
	 * @param ethNum 网络接口名,Linux下是eth0
	 * @return linux的eth0地址
	 */
	@SuppressWarnings("rawtypes")
	private String getIpByEth0() {
		try {
			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if ("eth0".equals(netInterface.getName())) {
					Enumeration addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = (InetAddress) addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (SocketException e) {
			log.error(e.getMessage(), e);
		}
		return "172.0.0.1";
	}
	
	private GetMeIp() {}
	
	public static GetMeIp get() {
		return new GetMeIp();
	}
	
}
