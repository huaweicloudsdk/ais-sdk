package com.huawei.ais.demo;

import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.common.ProxyHostInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 此处为HTTP Client的工具函数，主要用于初始化Client的一些通用信息:
 * 
 * 包括 Endpoint(服务端点), Region(区域)，Access key(接入码) / Secret access key(安全接入码)
 *
 */
public class ClientContextUtils {
	/**
	 *  服务的区域信息: 图像识别服务目前支持华北-北京一(cn-north-1)、亚太-香港(ap-southeast-1)
	 */
	private static final String REGION = "cn-north-1";

	private static Map<String, String> endponitMap = new ConcurrentHashMap<>();
	static {
		/*  图像识别服务的区域和终端节点信息可以从如下地址查询
		 *  http://developer.huaweicloud.com/dev/endpoint
		 * */
		endponitMap.put("cn-north-1", "https://image.cn-north-1.myhuaweicloud.com");
		endponitMap.put("ap-southeast-1", "https://image.ap-southeast-1.myhuaweicloud.com");
	}

	
	private static final AuthInfo HEC_AUTH = new AuthInfo(
			 getCurrentEndpoint(REGION),
			 REGION,
			 "your ak",    /* 请输入你的AK信息 */
			 "your sk"     /* 对应AK的的SK信息 */
			 );
	
	public static AuthInfo getAuthInfo() {
		return HEC_AUTH;
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
	 * 用于根据服务区域信息获取服务域名
	 */
	public static String getCurrentEndpoint(String region){
		return endponitMap.get(region);
	}
}
