package com.huawei.ais.common;

public class ProxyHostInfo {

	// proxy host name / ip
	private String hostName = "";

	// proxy host port
	private int port = 8080;

	// proxy user name
	private String userName = "";

	// proxy user password
	private String password = "";

	public ProxyHostInfo(String hostName, int port, String userName, String password) {
		super();
		this.hostName = hostName;
		this.port = port;
		this.userName = userName;
		this.password = password;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
