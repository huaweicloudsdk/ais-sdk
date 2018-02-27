package com.huawei.ais.common;

public class AuthInfo {
	
	private String endPoint = "";
	private String region  = "";
	private String ak = "";
	private String sk = "";
	
	public AuthInfo() {
		super();
	}
	
	public AuthInfo(String endPoint, String region, String ak, String sk) {
		super();
		this.endPoint = endPoint;
		this.region = region;
		this.ak = ak;
		this.sk = sk;
	}
	public String getEndPoint() {
		return endPoint;
	}

	public String getRegion() {
		return region;
	}

	public String getAk() {
		return ak;
	}

	public String getSk() {
		return sk;
	}
}
