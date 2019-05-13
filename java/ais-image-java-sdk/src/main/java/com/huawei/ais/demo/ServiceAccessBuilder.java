package com.huawei.ais.demo;

import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.common.ProxyHostInfo;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 此处为服务入口的构建函数，主要用于初始化Service Access的一些通用信息:
 * 
 * 包括 Endpoint(服务端点), Region(区域)，Access key(接入码) / Secret access key(安全接入码),
 * 以及http 请求相关超时参数
 *
 */
public class ServiceAccessBuilder {


	private static Map<String, String> endponitMap = new ConcurrentHashMap<>();
	static {
		/*  图像识别服务的区域和终端节点信息可以从如下地址查询
		 *  http://developer.huaweicloud.com/dev/endpoint
		 * */
		endponitMap.put("cn-north-1", "https://image.cn-north-1.myhuaweicloud.com");
		endponitMap.put("ap-southeast-1", "https://image.ap-southeast-1.myhuaweicloud.com");
	}

	private String region;

	private String endpoint;

	private String ak;

	private String sk;

	private ProxyHostInfo proxy = null;

	private int connectionTimeout = 5000;

	private int connectionRequestTimeout = 1000;

	private int socketTimeout = 5000;

	public static ServiceAccessBuilder builder() {
		return new ServiceAccessBuilder();
	}

	public AisAccess build() {
		if (proxy == null) {
			return new AisAccess(new AuthInfo(endpoint, region, ak, sk), connectionTimeout,connectionRequestTimeout,socketTimeout);
		} else {
			return new AisAccessWithProxy(new AuthInfo(endpoint, region, ak, sk), proxy, connectionTimeout,connectionRequestTimeout, socketTimeout);
		}
	}

	public  ServiceAccessBuilder ak(String ak) {
		this.ak = ak;
		return this;
	}

	public  ServiceAccessBuilder sk(String sk) {
		this.sk = sk;
		return this;
	}

	public  ServiceAccessBuilder region(String region) {
		this.region = region;
		this.endpoint = getCurrentEndpoint(region);
		return this;
	}

	public  ServiceAccessBuilder proxy(ProxyHostInfo proxy) {
		this.proxy = proxy;
		return this;
	}

	public ServiceAccessBuilder connectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
		return this;
	}

	public ServiceAccessBuilder connectionRequestTimeout(int connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
		return this;
	}

	public ServiceAccessBuilder socketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
		return this;
	}

	/**
	 * 用于支持使用代理模式访问网络， 此时使用的代理主机配置信息
	 */
	public static ProxyHostInfo getProxyHost() {

		return new ProxyHostInfo("proxycn2.***.com", /* 代理主机信息 */
				8080,        /* 代理主机的端口 */
				"china/***", /* 代理的用户名 */
				"***"        /* 代理用户对应的密码 */
				);
	}

	/**
	 * 用于根据服务的区域信息获取服务域名
	 */
	public static String getCurrentEndpoint(String region){
		return endponitMap.get(region);
	}


}
