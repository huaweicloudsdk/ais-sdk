package com.huawei.ais.auth;

import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import com.cloud.sdk.http.HttpMethodName;

public abstract class AccessService {

	protected String serviceName = null;

	protected String region = null;

	protected String ak = null;

	protected String sk = null;

	public AccessService(String serviceName, String region, String ak, String sk) {
		this.region = region;
		this.serviceName = serviceName;
		this.ak = ak;
		this.sk = sk;
	}

	public abstract HttpResponse access(URL url, Map<String, String> header, InputStream content, Long contentLength,
			HttpMethodName httpMethod) throws Exception;

	public abstract HttpResponse accessEntity(URL url, Map<String, String> header, HttpEntity entity,
			Long contentLength, HttpMethodName httpMethod) throws Exception;

	public HttpResponse access(URL url, Map<String, String> header, HttpMethodName httpMethod) throws Exception {
		return this.access(url, header, null, 0L, httpMethod);
	}

	public HttpResponse access(URL url, InputStream content, Long contentLength, HttpMethodName httpMethod)
			throws Exception {
		return this.access(url, null, content, contentLength, httpMethod);
	}

	public HttpResponse access(URL url, HttpMethodName httpMethod) throws Exception {
		return this.access(url, null, null, 0L, httpMethod);
	}

	public abstract void close();

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getAk() {
		return ak;
	}

	public void setAk(String ak) {
		this.ak = ak;
	}

	public String getSk() {
		return sk;
	}

	public void setSk(String sk) {
		this.sk = sk;
	}

}